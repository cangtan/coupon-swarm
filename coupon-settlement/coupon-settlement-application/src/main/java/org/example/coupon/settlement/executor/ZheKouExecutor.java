package org.example.coupon.settlement.executor;

import lombok.extern.slf4j.Slf4j;
import org.example.coupon.common.dto.CouponTemplateDTO;
import org.example.coupon.settlement.constant.RuleFlag;
import org.example.coupon.settlement.dto.SettlementInfoDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Component
public class ZheKouExecutor extends AbstractRuleExecutor {

    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.ZHEKOU;
    }

    @Override
    public SettlementInfoDTO computeRule(SettlementInfoDTO settlement) {
        BigDecimal goodsSum = goodsCostSum(settlement.getGoodsInfoList());
        SettlementInfoDTO calcResult = processGoodsTypeNotSatisfy(settlement, goodsSum);
        if (calcResult != null) {
            // 不匹配
            return calcResult;
        }
        // 判断满减是否符合折扣标准
        CouponTemplateDTO template = settlement.getCouponAndTemplateInfoList().get(0).getTemplate();
        BigDecimal quota = new BigDecimal(template.getRule().getDiscount().getQuota());
        BigDecimal discount = goodsSum.multiply(quota).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        settlement.setCost(discount.compareTo(minCost()) < 0 ? minCost() : discount);
        return settlement;
    }
}
