package org.example.coupon.distribution.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponKafkaMessage {
    private Integer status;
    private List<Integer> ids;
}
