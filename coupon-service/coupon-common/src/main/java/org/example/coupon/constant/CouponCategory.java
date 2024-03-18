package org.example.coupon.constant;

import com.alibaba.cola.exception.Assert;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 优惠券分类
 */
@Getter
@AllArgsConstructor
public enum CouponCategory {
    MANJIAN("满减券", "001"),
    ZHEKOU("折扣券", "002"),
    LIJIAN("立减券", "003");

    /**
     * 优惠券描述（分类）
     */
    private final String description;
    /**
     * 分类编码
     */
    private final String code;

    public static CouponCategory of(String code){
        Assert.notNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + "not exist!"));
    }
}
