package org.example.coupon.template.controller;

import com.alibaba.cola.exception.BizException;
import com.netflix.appinfo.InstanceInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 健康检查接口
 */
@Slf4j
@RestController
@RequestMapping("/health_check")
@AllArgsConstructor
public class HealthController {

    /**
     * 服务发现客户端，可以获取自身或其他服务的元信息
     */
    private final DiscoveryClient client;

    /**
     * 服务注册接口
     * 提供了获取服务id的方法
     */
    private final Registration registration;

    @GetMapping
    public String health() {
        return "CouponTemplate Is OK!";
    }

    @GetMapping("/error")
    public String exception() {
        throw new BizException("CouponTemplate Has Some Problem");
    }

    @GetMapping("/meta")
    public List<Map<String, Object>> info() {
        List<ServiceInstance> infoList = client.getInstances(registration.getServiceId());
        List<Map<String, Object>> result = new ArrayList<>(infoList.size());
        infoList.forEach(item -> {
            Map<String, Object> info = new HashMap<>();
            info.put("serviceId", item.getServiceId());
            info.put("instanceId", item.getInstanceId());
        });
        return result;
    }
}
