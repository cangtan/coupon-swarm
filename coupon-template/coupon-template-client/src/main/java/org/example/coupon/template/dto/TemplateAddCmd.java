package org.example.coupon.template.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.coupon.common.constant.TemplateRule;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateAddCmd {

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
     * 优惠券个数
     */
    private Integer couponCount;

    /**
     * 创建者id
     */
    private Long userId;

    /**
     * 用途
     */
    private Integer target;

    /**
     * 优惠券规则
     */
    private TemplateRule rule;

    public boolean validate() {
        // 校验...懒得写了
        return true;
    }
}
