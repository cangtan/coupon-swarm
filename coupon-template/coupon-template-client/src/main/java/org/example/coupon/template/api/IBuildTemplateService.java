package org.example.coupon.template.api;

import org.example.coupon.common.dto.CouponTemplateDTO;
import org.example.coupon.template.dto.TemplateAddCmd;

public interface IBuildTemplateService {
    /**
     * 创建优惠券模板对象
     *
     * @param templateRequest
     * @return
     */
    CouponTemplateDTO buildTemplate(TemplateAddCmd templateRequest);
}
