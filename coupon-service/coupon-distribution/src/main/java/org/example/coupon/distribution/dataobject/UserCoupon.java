package org.example.coupon.distribution.dataobject;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.coupon.bo.CouponTemplateBO;
import org.example.coupon.distribution.constant.CouponStatus;
import org.example.coupon.distribution.converter.CouponStatusConverter;
import org.example.coupon.distribution.serialization.UserCouponSerialize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户优惠券
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_coupon")
@JsonSerialize(using = UserCouponSerialize.class)
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
    @CreatedDate
    @Column(name = "assign_time", nullable = false)
    private Date assignTime;

    /**
     * 状态
     */
    @Column(name = "status", nullable = false)
    @Convert(converter = CouponStatusConverter.class)
    private CouponStatus status;

    /**
     * `@Transient`表示不属于该表的字段
     */
    @Transient
    private CouponTemplateBO couponTemplate;

    public static UserCoupon invalidCoupon() {
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setId(-1);
        return userCoupon;
    }

    public UserCoupon(Integer templateId, Long userId, String couponCode, CouponStatus couponStatus) {
        this.templateId = templateId;
        this.userId = userId;
        this.couponCode = couponCode;
        this.status = couponStatus;

    }
}