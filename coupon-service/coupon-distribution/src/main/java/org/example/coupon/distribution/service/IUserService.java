package org.example.coupon.distribution.service;

import org.example.coupon.bo.CouponTemplateBO;
import org.example.coupon.bo.SettlementInfoBO;
import org.example.coupon.distribution.cmd.AcquireTemplateRequest;
import org.example.coupon.distribution.dataobject.UserCoupon;

import java.util.List;

/**
 * 用户服务相关的接口定义
 * 1.用户三类状态优惠券信息展示服务
 * 2.擦好看用户当前可以领取的优惠券模板
 * 3.用户领取优惠券服务
 * 4.用户消费优惠券服务 - coupon-settlement 配合实现
 */
public interface IUserService {

    /**
     * 根据用户id和状态查询优惠券id
     * @param userId 用户id
     * @param status 优惠券状态
     * @return
     */
    List<UserCoupon> findCouponsByStatus(Long userId, Integer status);

    /**
     * 根据用户id查找当前可以领取的优惠券模板
     * @param userId
     * @return
     */
    List<CouponTemplateBO> findAvailable(Long userId);

    /**
     * 用户领取优惠券
     * @param request
     * @return
     */
    UserCoupon acquireTemplate(AcquireTemplateRequest request);

    /**
     * 结算（核销）优惠券
     * @param settlementInfo
     * @return
     */
    SettlementInfoBO settlement(SettlementInfoBO settlementInfo);
}
