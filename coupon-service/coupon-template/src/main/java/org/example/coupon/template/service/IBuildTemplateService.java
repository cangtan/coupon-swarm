package org.example.coupon.template.service;

import org.example.coupon.template.cmd.TemplateRequest;
import org.example.coupon.template.dataobject.CouponTemplate;

public interface IBuildTemplateService {
    /**
     * 创建优惠券模板对象
     *
     * @param templateRequest
     * @return
     */
    CouponTemplate buildTemplate(TemplateRequest templateRequest);
}
