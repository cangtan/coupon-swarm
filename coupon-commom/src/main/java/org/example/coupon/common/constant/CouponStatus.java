package org.example.coupon.common.constant;

import com.alibaba.cola.exception.Assert;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CouponStatus {
    USABLE("可用的", 1),
    USRED("已使用的", 2),
    EXPIRED("未使用的", 3),
    ;

    private final String description;
    private final Integer code;

    public static CouponStatus of(Integer code){
        Assert.notNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + "not exist!"));
    }
}
