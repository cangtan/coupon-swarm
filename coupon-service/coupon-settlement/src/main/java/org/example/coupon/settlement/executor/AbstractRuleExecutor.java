package org.example.coupon.settlement.executor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.coupon.bo.GoodsInfoBO;
import org.example.coupon.bo.SettlementInfoBO;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽象规则执行器
 */
public abstract class AbstractRuleExecutor implements RuleExecutor {

    /**
     * 校验商品类型与优惠券是否匹配
     * 需要注意
     * 这里实现的单品类优惠券的校验，多品优惠券重载此方法
     * 商品只需要有一个优惠券要求的商品类型去匹配就可以
     *
     * @param settlement
     * @return
     */
    protected boolean isGoodsTypeSatisfy(SettlementInfoBO settlement) {
        List<Integer> goodsType = settlement.getGoodsInfoList()
                .stream().map(GoodsInfoBO::getType)
                .collect(Collectors.toList());

        try {
            List<Integer> templateGoodsType = new ObjectMapper()
                    .readValue(settlement.getCouponAndTemplateInfoList().get(0)
                            .getTemplate().getRule().getUsage().getGoodsType(), List.class);
            // 判断是否有交集
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected SettlementInfoBO processGoodsTypeNotSatisfy(SettlementInfoBO settlementInfo, BigDecimal goodsSum) {
        boolean isGoodsTypeSatisfy = isGoodsTypeSatisfy(settlementInfo);
        // 当商品类型不满足时，直接返回总价，并清空优惠券
        if (!isGoodsTypeSatisfy) {
            settlementInfo.setCost(goodsSum);
            settlementInfo.setCouponAndTemplateInfoList(Collections.emptyList());
            return settlementInfo;
        }
        return null;
    }

    protected BigDecimal goodsCostSum(List<GoodsInfoBO> goodsInfos) {
        return goodsInfos.stream().map(item -> item.getPrice()
                .multiply(new BigDecimal(item.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }


    protected BigDecimal minCost() {
        return new BigDecimal("0.01");
    }
}
