package org.example.coupon.zuul.filter.impl;

import com.google.common.util.concurrent.RateLimiter;
import org.example.coupon.zuul.filter.AbstractPreZuulFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 限流过滤器
 */
@Component
public class RateLimiterFilter extends AbstractPreZuulFilter {

    RateLimiter rateLimiter = RateLimiter.create(2.0d);

    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        if (rateLimiter.tryAcquire()) {
            return success();
        } else {
            return fail(402, "ERROR: RATE LIMIT");
        }
    }

    @Override
    public int filterOrder() {
        return 2;
    }
}
