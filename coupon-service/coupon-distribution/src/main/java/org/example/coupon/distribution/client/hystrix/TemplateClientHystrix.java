package org.example.coupon.distribution.client.hystrix;

import com.alibaba.cola.dto.SingleResponse;
import org.example.coupon.bo.CouponTemplateBO;
import org.example.coupon.distribution.client.TemplateClient;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 优惠券模板Feign的熔断降级策略
 */
@Component
public class TemplateClientHystrix implements TemplateClient {

    @Override
    public SingleResponse<List<CouponTemplateBO>> findAllUsableTemplate() {
        return SingleResponse.of(Collections.emptyList());
    }

    @Override
    public SingleResponse<Map<Integer, CouponTemplateBO>> findIdsToTemplate(Collection<Integer> ids) {
        return SingleResponse.of(new HashMap<>());
    }
}
