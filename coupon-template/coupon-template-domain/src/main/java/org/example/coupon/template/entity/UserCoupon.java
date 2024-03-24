package org.example.coupon.template.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户优惠券表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "user_coupon")
public class UserCoupon {
    /**
    * id
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
    * 优惠券模板id
    */
    @Column(name = "template_id", nullable = false)
    private Integer templateId;

    /**
    * 用户id
    */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
    * 优惠券码
    */
    @Column(name = "coupon_code", nullable = false)
    private String couponCode;

    /**
    * 领取时间
    */
    @Column(name = "assign_time", nullable = false)
    private Date assignTime;

    /**
    * 状态
    */
    @Column(name = "status", nullable = false)
    private Integer status;
}