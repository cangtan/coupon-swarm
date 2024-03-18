package org.example.coupon.template.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.example.coupon.constant.Constants;
import org.example.coupon.template.dao.CouponTemplateDao;
import org.example.coupon.template.dataobject.CouponTemplate;
import org.example.coupon.template.service.IAsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class AsyncServiceImpl implements IAsyncService {

    private final CouponTemplateDao couponTemplateDao;

    private final StringRedisTemplate redisTemplate;

    @Override
    @Async("getAsyncExecutor")
    public void asyncConstructCouponByTemplate(CouponTemplate couponTemplate) {
        StopWatch watch = new StopWatch();
        watch.start();
        Set<String> couponCodes = buildCouponCode(couponTemplate);
        String redisKey = String.format("%s%s",
                Constants.RedisPrefix.COUPON_TEMPLATE, couponTemplate.getId().toString());
        log.info("Push CouponCode To Redis: {}",
                redisTemplate.opsForList().rightPushAll(redisKey, couponCodes));
        couponTemplate.setAvailable(true);
        couponTemplateDao.save(couponTemplate);
        watch.stop();
        log.info("Construct CouponCode By Template Cost: {}", watch.getTotalTimeSeconds());
        // ... 发送通知
    }

    /**
     * 构造优惠券码
     * 前四位：产品线+类型
     * 中间六位：日期随机（190101
     *
     * @return
     */
    private Set<String> buildCouponCode(CouponTemplate couponTemplate) {
        StopWatch watch = new StopWatch();
        watch.start();
        Set<String> result = new HashSet<>();
        String prefix4 = couponTemplate.getProductLine().getCode().toString()
                + couponTemplate.getCategory().getCode();
        String date = new SimpleDateFormat("yyyyMMdd")
                .format(couponTemplate.getCreatedTime());
        while (result.size() < couponTemplate.getCouponCount()) {
            String templateCode = prefix4 + date + roundCouponSuffix();
            result.add(templateCode);
        }
        assert result.size() == couponTemplate.getCouponCount();
        watch.stop();
        log.info("Build Coupon Code cost:" + watch.getTotalTimeSeconds());
        return result;
    }

    private String roundCouponSuffix() {
        char[] firsts = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        String suffix = RandomStringUtils.random(1, firsts);
        suffix += RandomStringUtils.randomNumeric(7);
        return suffix;
    }
}
