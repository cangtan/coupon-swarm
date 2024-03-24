package org.example.coupon.template.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.coupon.common.constant.Constants;
import org.example.coupon.template.api.IAsyncService;
import org.example.coupon.template.dto.TemplateAddCmd;
import org.example.coupon.template.entity.CouponTemplate;
import org.example.coupon.template.gateway.CouponTemplateDao;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StopWatch;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@DubboService
public class AsyncServiceImpl implements IAsyncService {

    private final CouponTemplateDao couponTemplateDao;

    private final StringRedisTemplate redisTemplate;

    @Override
    @Async("getAsyncExecutor")
    public void asyncConstructCouponByTemplate(TemplateAddCmd addCmd, Integer id) {
        StopWatch watch = new StopWatch();
        watch.start();
        Set<String> couponCodes = buildCouponCode(addCmd);
        String redisKey = String.format("%s%s",
                Constants.RedisPrefix.COUPON_TEMPLATE, id.toString());
        log.info("Push CouponCode To Redis: {}",
                redisTemplate.opsForList().rightPushAll(redisKey, couponCodes));
        CouponTemplate couponTemplate = new CouponTemplate();
        couponTemplate.setId(id);
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
    private Set<String> buildCouponCode(TemplateAddCmd couponTemplate) {
        StopWatch watch = new StopWatch();
        watch.start();
        Set<String> result = new HashSet<>();
        String prefix4 = couponTemplate.getProductLine().toString()
                + couponTemplate.getCategory();
        String date = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
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
        return String.valueOf(RandomUtil.randomInt(10000000, 99999999));
    }
}
