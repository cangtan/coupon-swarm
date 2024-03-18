package org.example.coupon.settlement.executor;

import lombok.extern.slf4j.Slf4j;
import org.example.coupon.bo.CouponTemplateBO;
import org.example.coupon.bo.SettlementInfoBO;
import org.example.coupon.settlement.constant.RuleFlag;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class LiJianExecutor extends AbstractRuleExecutor{

    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.LIJIAN;
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
        BigDecimal quota = new BigDecimal(template.getRule().getDiscount().getQuota());
        BigDecimal discount = goodsSum.subtract(quota);
        settlement.setCost(discount.compareTo(minCost()) < 0 ? minCost() : discount);
        return settlement;
    }
}
