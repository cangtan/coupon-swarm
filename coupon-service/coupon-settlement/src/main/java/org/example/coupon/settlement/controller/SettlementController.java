package org.example.coupon.settlement.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.coupon.bo.SettlementInfoBO;
import org.example.coupon.settlement.executor.ExecutorManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class SettlementController {

    private ExecutorManager executorManager;

    @PostMapping("/settlement/compute")
    public SettlementInfoBO computeRule(@RequestBody SettlementInfoBO settlement) {
        return executorManager.computeRule(settlement);
    }
}
