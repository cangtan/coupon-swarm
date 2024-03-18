package org.example.coupon.zuul.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractZuulFilter extends ZuulFilter {
    // 用于在过滤器之间传递消息，数据保存在每个请求的ThreadLocal中
    // 扩展了ConcurrentHashMap
    protected RequestContext context;

    private static final String NEXT="next";

    @Override
    public boolean shouldFilter() {
        // 获取当前线程的request context
        RequestContext currentContext = RequestContext.getCurrentContext();
        return (boolean) currentContext.getOrDefault(NEXT, true);
    }

    @Override
    public Object run() throws ZuulException {
        context = RequestContext.getCurrentContext();
        return cRun();
    }

    protected abstract Object cRun();

    protected Object fail(int code, String message)  {
        context.set(NEXT, false);
        context.setSendZuulResponse(false);
        context.getResponse().setContentType("text/html;charset=UTF-8");
        context.setResponseStatusCode(code);
        Map<String, String> map = new HashMap<>();
        map.put("result", message);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            context.setResponseBody(objectMapper.writeValueAsString(map));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Object success() {
        context.set(NEXT, true);
        return null;
    }
}
