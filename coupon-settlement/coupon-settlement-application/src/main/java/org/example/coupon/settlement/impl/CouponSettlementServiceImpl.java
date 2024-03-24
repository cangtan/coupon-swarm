package org.example.coupon.settlement.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.coupon.settlement.api.ICouponSettlementService;
import org.example.coupon.settlement.dto.SettlementInfoDTO;
import org.example.coupon.settlement.executor.ExecutorManager;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class CouponSettlementServiceImpl implements ICouponSettlementService {

    @Autowired
    private ExecutorManager executorManager;

    @Override
    public SettlementInfoDTO computeRule(SettlementInfoDTO settlement) {
        return executorManager.computeRule(settlement);
    }
}
