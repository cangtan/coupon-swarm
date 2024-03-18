package org.example.coupon.distribution.client.hystrix;

import com.alibaba.cola.dto.SingleResponse;
import org.example.coupon.bo.SettlementInfoBO;
import org.example.coupon.distribution.client.SettlementClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SettlementClientHystrix implements SettlementClient {
    @Override
    public SingleResponse<SettlementInfoBO> computeRule(SettlementInfoBO settlementInfoBO) {
        SettlementInfoBO data = new SettlementInfoBO();
        data.setEmploy(false);
        data.setCost(new BigDecimal("-1.0"));
        return SingleResponse.of(data);
    }
}
