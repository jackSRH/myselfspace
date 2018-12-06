package com.mailian.firecontrol.framework.aspectj;

import com.mailian.core.bean.ResponseResult;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/12/6
 * @Description:
 */
@Aspect
@Component
public class PrecinctUnitAspect {

    // 配置织入点
    @Pointcut(value = "execution(* com.mailian.firecontrol.api.*.controller..*(..))")
    public void scopePointCut() {

    }

    @Around(value = "scopePointCut() && args(com.mailian.core.db.DataScope,..)")
    public Object doAroundService(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        DataScope dataScope = (DataScope) proceedingJoinPoint.getArgs()[0];
        if(StringUtils.isNotNull(dataScope) && StringUtils.isEmpty(dataScope.getDataIds())){
            return ResponseResult.buildOkResult();
        }
        return proceedingJoinPoint.proceed();
    }
}
