package org.example.coupon.distribution.client;

import com.alibaba.cola.dto.SingleResponse;
import org.example.coupon.bo.CouponTemplateBO;
import org.example.coupon.distribution.client.hystrix.TemplateClientHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 模板微服务调用
 */
@FeignClient(value = "eureka-client-coupon-template", fallback = TemplateClientHystrix.class)
public interface TemplateClient {

    @GetMapping("/coupon-template/template/sdk/all")
    SingleResponse<List<CouponTemplateBO>> findAllUsableTemplate();

    @GetMapping("/coupon-template/template/sdk/infos")
    SingleResponse<Map<Integer, CouponTemplateBO>> findIdsToTemplate(@RequestParam Collection<Integer> ids);
}
