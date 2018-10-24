package com.mailian.core.handler;

import com.mailian.core.exception.RequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/21
 * @Description: 异步错误处理
 */
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(AsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.info("Async method has uncaught exception, params: " + params);

        if (ex instanceof RequestException) {
            RequestException asyncException = (RequestException) ex;
            log.info("asyncException:"  + asyncException.getMsg());
        }

        log.error("asyncException :", ex);
    }
}
