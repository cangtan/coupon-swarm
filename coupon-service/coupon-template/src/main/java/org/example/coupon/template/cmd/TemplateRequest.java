package org.example.coupon.template.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.coupon.bo.TemplateRule;
import org.example.coupon.constant.CouponCategory;
import org.example.coupon.constant.DistributeTarget;
import org.example.coupon.constant.ProductLine;
import org.example.coupon.template.converter.CouponCategoryConverter;
import org.example.coupon.template.converter.DistributeTargetConverter;
import org.example.coupon.template.converter.ProductLineConverter;
import org.example.coupon.template.converter.TemplateRuleConverter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateRequest {

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
