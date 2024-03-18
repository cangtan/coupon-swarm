package org.example.coupon.zuul.filter.impl;

import org.example.coupon.zuul.filter.AbstractPreZuulFilter;
import org.springframework.stereotype.Component;

/**
 * 日志前置过滤器
 * 用于记录客户端请求时间
 */
@Component
public class LogPreRequestFilter extends AbstractPreZuulFilter {

    @Override
    protected Object cRun() {
        context.set("startTime", System.currentTimeMillis());
        return success();
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}
