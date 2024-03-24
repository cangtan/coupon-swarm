package org.example.coupon.common.constant;

import com.alibaba.cola.exception.Assert;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 产品线
 */
@Getter
@AllArgsConstructor
public enum ProductLine {

    DAMAO("某猫", 1),
    DABAO("某宝", 2);

    /**
     * 产品描述
     */
    private final String description;
    /**
     * 产品线编码
     */
    private final Integer code;


    public static ProductLine of(Integer code) {
        Assert.notNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + "not exist!"));
    }
}
