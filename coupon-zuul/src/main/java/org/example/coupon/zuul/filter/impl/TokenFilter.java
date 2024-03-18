package org.example.coupon.zuul.filter.impl;

import org.example.coupon.zuul.filter.AbstractPreZuulFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Token验证过滤器
 */
@Component
public class TokenFilter extends AbstractPreZuulFilter {
    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        String token = request.getHeader("token");
        if (null == token) {
            return fail(401, "用户未登录");
        }
        return success();
    }

    @Override
    public int filterOrder() {
        return 1;
    }
}
