package org.example.coupon.distribution.gateway;


import org.example.coupon.distribution.entity.SettlementInfo;

public interface SettlementGateway {

    SettlementInfo computeRule(SettlementInfo settlementInfoBO);
}
