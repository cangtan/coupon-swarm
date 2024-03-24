package org.example.coupon.distribution.gateway;

import org.example.coupon.common.constant.CouponStatus;
import org.example.coupon.distribution.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCouponDao extends JpaRepository<UserCoupon, Integer> {
    /**
     * 根据用户id和状态查询优惠券
     */
    List<UserCoupon> findAllByUserIdAndStatus(Long userId, CouponStatus status);
}
