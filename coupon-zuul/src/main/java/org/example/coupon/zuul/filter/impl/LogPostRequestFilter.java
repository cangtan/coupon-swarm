package org.example.coupon.zuul.filter.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.coupon.zuul.filter.AbstractPostFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class LogPostRequestFilter extends AbstractPostFilter {
    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        String uri = request.getRequestURI();
        long startTime = (long) context.get("startTime");

        log.info("uri: {}, duration: {}", uri, System.currentTimeMillis() - startTime);
        return success();
    }

    @Override
    public int filterOrder() {
        // 在返回之前
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }
}
