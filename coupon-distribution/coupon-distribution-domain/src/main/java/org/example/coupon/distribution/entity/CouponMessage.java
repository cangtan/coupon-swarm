package org.example.coupon.distribution.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CouponMessage {
    private Integer status;
    private List<Integer> ids;
}
