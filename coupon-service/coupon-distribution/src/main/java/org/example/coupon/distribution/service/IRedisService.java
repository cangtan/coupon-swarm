package org.example.coupon.distribution.service;

import org.example.coupon.distribution.dataobject.UserCoupon;

import java.util.List;

/**
 * 用户的三个状态优惠券
 * 优惠券模板生成的优惠券码
 */
public interface IRedisService {

    /**
     * 根据用户id和状态查询缓存的优惠券数据
     * @param userId
     * @param status
     * @return
     */
    List<UserCoupon> getCachedCoupons(Long userId, Integer status);

    /**
     * 保存空的优惠券列表到缓存中
     * 用来解决缓存穿透（当用户查询不存在的时候，会缓存一个空列表在Redis当中
     * 避免查询Redis后再次查询数据库造成性能浪费
     * @param userId
     * @param status
     */
    void saveEmptyCouponListToCache(Long userId, List<Integer> status);

    /**
     * 获取优惠券码
     * 当无优惠券码的时候将会返回空
     * @param templateId
     * @return
     */
    String tryToAcquireCouponCodeFromCache(Integer templateId);

    /**
     * 将优惠券保存到Cache中
     * @param userId 用户id
     * @param coupons 优惠券
     * @param status 优惠券状态
     * @return 保存成功的个数
     */
    Integer addCouponToCache(Long userId, List<UserCoupon> coupons, Integer status);
}
