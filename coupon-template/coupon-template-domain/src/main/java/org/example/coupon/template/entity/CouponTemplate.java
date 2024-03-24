package org.example.coupon.template.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.coupon.common.constant.CouponCategory;
import org.example.coupon.common.constant.DistributeTarget;
import org.example.coupon.common.constant.ProductLine;
import org.example.coupon.common.constant.TemplateRule;
import org.example.coupon.template.converter.CouponCategoryConverter;
import org.example.coupon.template.converter.DistributeTargetConverter;
import org.example.coupon.template.converter.ProductLineConverter;
import org.example.coupon.template.converter.TemplateRuleConverter;
import org.example.coupon.template.serialization.CouponTemplateSerialize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* 优惠券模板表
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "coupon_template")
@JsonSerialize(using = CouponTemplateSerialize.class)
public class CouponTemplate {
    /**
    * id
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
    * 是否可用（1:可用 0:不可用）
    */
    @Column(name = "available", nullable = false)
    private Boolean available;

    /**
    * 是否过期（1:过期 0:未过期）
    */
    @Column(name = "expired", nullable = false)
    private Boolean expired;

    /**
    * 优惠券名称
    */
    @Column(name = "name", nullable = false)
    private String name;

    /**
    * 优惠券logo
    */
    @Column(name = "logo", nullable = false)
    private String logo;

    /**
    * 优惠券介绍
    */
    @Column(name = "intro", nullable = false)
    private String intro;

    /**
    * 优惠券分类
    */
    @Column(name = "category", nullable = false)
    @Convert(converter = CouponCategoryConverter.class)
    private CouponCategory category;

    /**
    * 产品线
    */
    @Column(name = "product_line", nullable = false)
    @Convert(converter = ProductLineConverter.class)
    private ProductLine productLine;

    /**
    * 优惠券个数
    */
    @Column(name = "coupon_count", nullable = false)
    private Integer couponCount;

    /**
    * 创建时间
    */
    @Column(name = "created_time", nullable = false)
    @CreatedDate
    private Date createdTime;

    /**
    * 创建者id
    */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
    * 标识
    */
    @Column(name = "template_key", nullable = false)
    private String templateKey;

    /**
    * 用途
    */
    @Column(name = "target", nullable = false)
    @Convert(converter = DistributeTargetConverter.class)
    private DistributeTarget target;

    /**
    * 优惠券规则
    */
    @Column(name = "rule", nullable = false)
    @Convert(converter = TemplateRuleConverter.class)
    private TemplateRule rule;

    public CouponTemplate(String name, String logo, String intro, String category,
                          Integer productLine, Integer count, Long userId, Integer target, TemplateRule rule){
        this.available = false;
        this.expired = false;
        this.name = name;
        this.logo = logo;
        this.intro = intro;
        this.category = CouponCategory.of(category);
        this.productLine = ProductLine.of(productLine);
        this.couponCount = count;
        this.userId = userId;
        this.target = DistributeTarget.of(target);
        this.templateKey = productLine.toString() + category
                + new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.rule = rule;
    }
}