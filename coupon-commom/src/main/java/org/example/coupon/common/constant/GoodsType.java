package org.example.coupon.common.constant;

import com.alibaba.cola.exception.Assert;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 商品类型枚举
 */
@Getter
@AllArgsConstructor
public enum GoodsType {

    WENYU("文娱", 1),

    SHENGXIAN("生鲜", 2),

    JIAJU("家居", 3),

    OTHERS("其他", 4),

    ALL("全品类", 5);

    private final String description;

    private final Integer code;

    public static GoodsType of(Integer code) {
        Assert.notNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + "not exist!"));
    }
}
