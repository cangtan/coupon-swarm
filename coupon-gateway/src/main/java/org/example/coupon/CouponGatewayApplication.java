package org.example.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CouponGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponGatewayApplication.class, args);
    }
}
