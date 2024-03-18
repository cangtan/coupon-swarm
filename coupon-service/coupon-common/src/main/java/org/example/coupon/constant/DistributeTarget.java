package org.example.coupon.constant;

import com.alibaba.cola.exception.Assert;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 分发目标
 */
@Getter
@AllArgsConstructor
public enum DistributeTarget {
    SINGLE("单用户", 1),
    MULTI("多用户", 2);

    private final String description;
    private final Integer code;

    public static DistributeTarget of(Integer code){
        Assert.notNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + "not exist!"));
    }
}
