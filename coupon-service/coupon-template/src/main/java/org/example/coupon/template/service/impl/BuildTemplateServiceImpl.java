package org.example.coupon.template.service.impl;

import com.alibaba.cola.exception.BizException;
import lombok.AllArgsConstructor;
import org.example.coupon.template.cmd.TemplateRequest;
import org.example.coupon.template.dao.CouponTemplateDao;
import org.example.coupon.template.dataobject.CouponTemplate;
import org.example.coupon.template.service.IAsyncService;
import org.example.coupon.template.service.IBuildTemplateService;
import org.springframework.stereotype.Service;

/**
 * 构建优惠券模板实现
 */
@Service
@AllArgsConstructor
public class BuildTemplateServiceImpl implements IBuildTemplateService {

    private final IAsyncService asyncService;

    private final CouponTemplateDao templateDao;

    @Override
    public CouponTemplate buildTemplate(TemplateRequest templateRequest) {
        if (null != templateDao.findByName(templateRequest.getName())){
            throw new BizException("Exist Same Name Template");
        }
        CouponTemplate template = requestToTemplate(templateRequest);
        template = templateDao.save(template);
        asyncService.asyncConstructCouponByTemplate(template);
        return template;
    }

    private CouponTemplate requestToTemplate(TemplateRequest request) {
        return new CouponTemplate(request.getName(), request.getLogo(), request.getIntro(),
                request.getCategory(), request.getProductLine(), request.getCouponCount(), request.getUserId(),
                request.getTarget(), request.getRule());
    }
}
