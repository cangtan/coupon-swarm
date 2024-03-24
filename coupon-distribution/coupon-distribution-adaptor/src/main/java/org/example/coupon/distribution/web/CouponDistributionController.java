package org.example.coupon.distribution.web;

import com.alibaba.cola.dto.MultiResponse;
import org.example.coupon.common.dto.CouponTemplateDTO;
import org.example.coupon.distribution.gateway.CouponTemplateGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/distribution")
public class CouponDistributionController {

    @Autowired
    private CouponTemplateGateway couponTemplateGateway;

    @GetMapping("/hello")
    public MultiResponse<CouponTemplateDTO> hello() {
        List<CouponTemplateDTO> allUsableTemplate = couponTemplateGateway.findAllUsableTemplate();
        return MultiResponse.of(allUsableTemplate, allUsableTemplate.size());
    }
}
