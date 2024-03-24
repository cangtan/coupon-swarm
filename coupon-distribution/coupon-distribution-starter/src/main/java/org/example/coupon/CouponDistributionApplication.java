package org.example.coupon;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class CouponDistributionApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponDistributionApplication.class, args);
    }
}
