package org.example.coupon.distribution.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.coupon.bo.CouponTemplateBO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcquireTemplateRequest {
    private Long userId;

    private CouponTemplateBO template;
}
