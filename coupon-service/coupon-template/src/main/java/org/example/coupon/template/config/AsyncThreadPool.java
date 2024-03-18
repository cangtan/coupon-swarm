package org.example.coupon.template.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * 自定义异步任务线程池
 */
@EnableAsync
@Configuration
@Slf4j
public class AsyncThreadPool implements AsyncConfigurer {
    @Override
    @Bean
    public Executor getAsyncExecutor() {
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(30);
        return new ThreadPoolExecutor(4, 8, 60, TimeUnit.SECONDS, blockingQueue);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            throwable.printStackTrace();
            try {
                log.error("AsyncError: {}, Method: {}, Param:{}", throwable.getMessage(), method.getName(),
                        new ObjectMapper().writeValueAsString(objects));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            // 进一步处理 异常通知（邮件、短信等
        };
    }
}
