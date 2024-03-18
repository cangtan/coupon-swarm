package org.example.coupon.distribution.client;

import com.alibaba.cola.dto.SingleResponse;
import org.example.coupon.bo.SettlementInfoBO;
import org.example.coupon.distribution.client.hystrix.SettlementClientHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "nacos-client-coupon-settlement", fallback = SettlementClientHystrix.class)
public interface SettlementClient {

    @PostMapping(value = "/coupon_settlement/settlement/compute")
    SingleResponse<SettlementInfoBO> computeRule(SettlementInfoBO settlementInfoBO);
}
