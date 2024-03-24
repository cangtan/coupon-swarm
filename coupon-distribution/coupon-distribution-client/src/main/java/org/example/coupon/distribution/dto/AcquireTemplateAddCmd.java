package org.example.coupon.distribution.dto;

import lombok.Data;
import org.example.coupon.common.dto.CouponTemplateDTO;

@Data
public class AcquireTemplateAddCmd {
    private Long userId;

    private CouponTemplateDTO template;
}
