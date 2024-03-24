package org.example.coupon.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * fake 商品信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsInfoDTO {

    /**
     *
     */
    private Integer type;

    private BigDecimal price;

    private Integer count;

}
