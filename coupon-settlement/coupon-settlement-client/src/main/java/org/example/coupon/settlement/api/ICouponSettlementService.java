package org.example.coupon.settlement.api;

import org.example.coupon.settlement.dto.SettlementInfoDTO;

public interface ICouponSettlementService {
    SettlementInfoDTO computeRule(SettlementInfoDTO settlement);
}
