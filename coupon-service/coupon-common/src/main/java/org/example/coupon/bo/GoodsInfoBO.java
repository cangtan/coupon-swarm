package org.example.coupon.bo;

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
public class GoodsInfoBO {

    /**
     * 商品类型{@link org.example.coupon.constant.GoodsType}
     */
    private Integer type;

    private BigDecimal price;

    private Integer count;

}
