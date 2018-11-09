package com.mailian.firecontrol.framework.aspectj;

import com.mailian.core.util.Tools;
import com.mailian.firecontrol.framework.component.LogAsync;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/24
 * @Description: 操作日志记录处理
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private LogAsync logAsync;

    // 配置织入点
    @Pointcut("@annotation(com.mailian.core.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 前置通知 用于拦截操作
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request = Tools.getRequest();
        logAsync.handleLog(request.getRequestURI(),request.getParameterMap(),joinPoint, null);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfter(JoinPoint joinPoint, Exception e) {
        HttpServletRequest request = Tools.getRequest();
        logAsync.handleLog(request.getRequestURI(),request.getParameterMap(),joinPoint, e);
    }

}
