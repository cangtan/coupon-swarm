package org.example.coupon.settlement.executor;

import lombok.extern.slf4j.Slf4j;
import org.example.coupon.common.constant.CouponCategory;
import org.example.coupon.settlement.constant.RuleFlag;
import org.example.coupon.settlement.dto.SettlementInfoDTO;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 优惠券结算规则管理器
 * 根据用户的请求找到对应的Executor，去做结算
 * BeanPostProcessor Bean后置处理器
 */
@Slf4j
@Component
public class ExecutorManager implements BeanPostProcessor {

    private static Map<RuleFlag, RuleExecutor> executorMap =
            new HashMap<>(RuleFlag.values().length);

    public SettlementInfoDTO computeRule(SettlementInfoDTO settlement) {
        SettlementInfoDTO result = null;
        CouponCategory category = CouponCategory.of(
                settlement.getCouponAndTemplateInfoList().get(0).getTemplate().getCategory());
        switch (category) {
            case MANJIAN:
                result = executorMap.get(RuleFlag.MANJIAN).computeRule(settlement);
                break;
            case LIJIAN:
                result = executorMap.get(RuleFlag.LIJIAN).computeRule(settlement);
                break;
            case ZHEKOU:
                result = executorMap.get(RuleFlag.ZHEKOU).computeRule(settlement);
                break;
        }
        return result;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (!(bean instanceof RuleExecutor)) {
            return bean;
        }
        RuleExecutor executor = (RuleExecutor) bean;
        RuleFlag ruleFlag = executor.ruleConfig();
        if (executorMap.containsKey(ruleFlag)) {
            throw new IllegalStateException("Executor已存在");
        }
        executorMap.put(ruleFlag, executor);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
