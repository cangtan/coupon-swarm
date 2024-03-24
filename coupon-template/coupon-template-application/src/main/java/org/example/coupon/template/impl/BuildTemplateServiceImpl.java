package org.example.coupon.template.impl;

import com.alibaba.cola.exception.BizException;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.coupon.common.dto.CouponTemplateDTO;
import org.example.coupon.template.api.IAsyncService;
import org.example.coupon.template.api.IBuildTemplateService;
import org.example.coupon.template.dto.TemplateAddCmd;
import org.example.coupon.template.entity.CouponTemplate;
import org.example.coupon.template.gateway.CouponTemplateDao;

/**
 * 构建优惠券模板实现
 */
@DubboService
@AllArgsConstructor
public class BuildTemplateServiceImpl implements IBuildTemplateService {

    private final IAsyncService asyncService;

    private final CouponTemplateDao templateDao;

    @Override
    public CouponTemplateDTO buildTemplate(TemplateAddCmd templateRequest) {
        if (null != templateDao.findByName(templateRequest.getName())) {
            throw new BizException("Exist Same Name Template");
        }
        CouponTemplate template = requestToTemplate(templateRequest);
        template = templateDao.save(template);
        asyncService.asyncConstructCouponByTemplate(templateRequest, template.getId());
        return null;
    }

    private CouponTemplate requestToTemplate(TemplateAddCmd request) {
        return new CouponTemplate(request.getName(), request.getLogo(), request.getIntro(),
                request.getCategory(), request.getProductLine(), request.getCouponCount(), request.getUserId(),
                request.getTarget(), request.getRule());
    }
}
