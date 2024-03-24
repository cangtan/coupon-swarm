package org.example.coupon.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.coupon.common.constant.TemplateRule;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponTemplateDTO {
    /**
     * id
     */
    private Integer id;
    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠券logo
     */
    private String logo;

    /**
     * 优惠券介绍
     */
    private String intro;

    /**
     * 优惠券分类
     */
    private String category;

    /**
     * 产品线
     */
    private Integer productLine;

    /**
     * 模板Key
     */
    private String templateKey;

    /**
     * 用途
     */
    private Integer target;

    /**
     * 优惠券规则
     */
    private TemplateRule rule;
}
