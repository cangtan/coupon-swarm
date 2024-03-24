package org.example.coupon.distribution.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.coupon.common.constant.CouponStatus;
import org.example.coupon.common.dto.CouponTemplateDTO;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCouponDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 优惠券模板id
     */
    private Integer templateId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 优惠券码
     */
    private String couponCode;

    /**
     * 领取时间
     */
    private Date assignTime;

    /**
     * 状态
     */
    private CouponStatus status;

    /**
     * `@Transient`表示不属于该表的字段
     */
    private CouponTemplateDTO couponTemplate;

    public static UserCouponDTO invalidCoupon() {
        UserCouponDTO userCoupon = new UserCouponDTO();
        userCoupon.setId(-1);
        return userCoupon;
    }

    public UserCouponDTO(Integer templateId, Long userId, String couponCode, CouponStatus couponStatus) {
        this.templateId = templateId;
        this.userId = userId;
        this.couponCode = couponCode;
        this.status = couponStatus;

    }
}
