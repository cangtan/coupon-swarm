package org.example.coupon.settlement.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  RuleFlag {
    MANJIAN("满减券的计算规则"),
    ZHEKOU("折扣券的计算规则"),
    LIJIAN("立减券的计算规则"),

    MANJIAN_ZHEKOU("满减券 + 折扣券的计算规则")
    ;
    /**
     * 规则描述
     */
    private final String description;
}
