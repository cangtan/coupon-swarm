package org.example.coupon.distribution.gatewayimpl;

import cn.hutool.core.bean.BeanUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.coupon.distribution.entity.SettlementInfo;
import org.example.coupon.distribution.gateway.SettlementGateway;
import org.example.coupon.settlement.api.ICouponSettlementService;
import org.example.coupon.settlement.dto.SettlementInfoDTO;
import org.springframework.stereotype.Service;

@Service
public class SettlementGatewayImpl implements SettlementGateway {

    @DubboReference
    private ICouponSettlementService couponSettlementService;

    @Override
    public SettlementInfo computeRule(SettlementInfo settlementInfo) {
        SettlementInfoDTO params = BeanUtil.copyProperties(settlementInfo, SettlementInfoDTO.class);
        return BeanUtil.copyProperties(couponSettlementService.computeRule(params), SettlementInfo.class);
    }
}
