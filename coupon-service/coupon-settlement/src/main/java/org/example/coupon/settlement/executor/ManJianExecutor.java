package org.example.coupon.settlement.executor;

import lombok.extern.slf4j.Slf4j;
import org.example.coupon.bo.CouponTemplateBO;
import org.example.coupon.bo.SettlementInfoBO;
import org.example.coupon.settlement.constant.RuleFlag;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;

@Slf4j
@Component
public class ManJianExecutor extends AbstractRuleExecutor{

    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.MANJIAN;
    }

    @Override
    public SettlementInfoBO computeRule(SettlementInfoBO settlement) {
        BigDecimal goodsSum = goodsCostSum(settlement.getGoodsInfoList());
        SettlementInfoBO calcResult = processGoodsTypeNotSatisfy(settlement, goodsSum);
        if (calcResult != null) {
            // 不匹配
            return calcResult;
        }
        // 判断满减是否符合折扣标准
        CouponTemplateBO template = settlement.getCouponAndTemplateInfoList().get(0).getTemplate();
        BigDecimal base = new BigDecimal(template.getRule().getDiscount().getBase());
        BigDecimal quota = new BigDecimal(template.getRule().getDiscount().getQuota());
        if (goodsSum.compareTo(base) < 0) {
            // 没有达到满减情况
            settlement.setCost(goodsSum);
            // 不使用优惠券
            settlement.setCouponAndTemplateInfoList(Collections.emptyList());
            return settlement;
        }
        BigDecimal subtract = goodsSum.subtract(quota);
        settlement.setCost(subtract.compareTo(minCost()) < 0 ? minCost() : subtract);
        return settlement;
    }
}
