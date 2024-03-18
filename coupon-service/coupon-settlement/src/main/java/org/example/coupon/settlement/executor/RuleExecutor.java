package org.example.coupon.settlement.executor;

import org.example.coupon.bo.SettlementInfoBO;
import org.example.coupon.settlement.constant.RuleFlag;

/**
 * 优惠券模板规则处理器接口定义
 */
public interface RuleExecutor {

    /**
     * 规则类型枚举标记
     * @return
     */
    RuleFlag ruleConfig();

    /**
     * 优惠券的规则计算
     * @param settlement 包含了选择的优惠券
     * @return 修正过的结算信息
     */
    SettlementInfoBO computeRule(SettlementInfoBO settlement);


}
