package org.example.coupon.distribution.gatewayimpl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.coupon.common.dto.CouponTemplateDTO;
import org.example.coupon.distribution.gateway.CouponTemplateGateway;
import org.example.coupon.template.api.ITemplateBaseService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class CouponTemplateGatewayImpl implements CouponTemplateGateway {
    @DubboReference
    private ITemplateBaseService templateBaseService;

    @Override
    public List<CouponTemplateDTO> findAllUsableTemplate() {
        return templateBaseService.findAllUsableTemplate();
    }

    @Override
    public Map<Integer, CouponTemplateDTO> findIdsToTemplate(Collection<Integer> ids) {
        return templateBaseService.findIdsTemplateSDK(ids);
    }
}
