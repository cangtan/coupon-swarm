package org.example.coupon.template.service;

import org.example.coupon.template.dataobject.CouponTemplate;

/**
 * 异步服务接口定义
 */
public interface IAsyncService {
    /**
     * 根据模板异步的创建优惠券码
     * @param couponTemplate
     */
    void asyncConstructCouponByTemplate(CouponTemplate couponTemplate);
}
