package org.example.coupon.settlement.executor;


import org.example.coupon.common.dto.GoodsInfoDTO;
import org.example.coupon.settlement.dto.SettlementInfoDTO;

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
    protected boolean isGoodsTypeSatisfy(SettlementInfoDTO settlement) {
        List<Integer> goodsType = settlement.getGoodsInfoList()
                .stream().map(GoodsInfoDTO::getType)
                .collect(Collectors.toList());

        try {
            // TODO 序列化
//            List<Integer> templateGoodsType =
//                    .readValue(settlement.getCouponAndTemplateInfoList().get(0)
//                            .getTemplate().getRule().getUsage().getGoodsType(), List.class);
            // 判断是否有交集
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected SettlementInfoDTO processGoodsTypeNotSatisfy(SettlementInfoDTO settlementInfo, BigDecimal goodsSum) {
        boolean isGoodsTypeSatisfy = isGoodsTypeSatisfy(settlementInfo);
        // 当商品类型不满足时，直接返回总价，并清空优惠券
        if (!isGoodsTypeSatisfy) {
            settlementInfo.setCost(goodsSum);
            settlementInfo.setCouponAndTemplateInfoList(Collections.emptyList());
            return settlementInfo;
        }
        return null;
    }

    protected BigDecimal goodsCostSum(List<GoodsInfoDTO> goodsInfos) {
        return goodsInfos.stream().map(item -> item.getPrice()
                .multiply(new BigDecimal(item.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }


    protected BigDecimal minCost() {
        return new BigDecimal("0.01");
    }
}
