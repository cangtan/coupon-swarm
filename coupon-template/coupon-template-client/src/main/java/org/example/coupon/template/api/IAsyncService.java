package org.example.coupon.template.api;


import org.example.coupon.template.dto.TemplateAddCmd;

/**
 * 异步服务接口定义
 */
public interface IAsyncService {
    /**
     * 根据模板异步的创建优惠券码
     *
     * @param couponTemplate
     * @param id
     */
    void asyncConstructCouponByTemplate(TemplateAddCmd couponTemplate, Integer id);
}
