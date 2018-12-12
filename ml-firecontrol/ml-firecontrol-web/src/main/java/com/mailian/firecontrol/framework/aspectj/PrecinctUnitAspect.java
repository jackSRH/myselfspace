package com.mailian.firecontrol.framework.aspectj;

import com.mailian.core.bean.ResponseResult;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.framework.annotation.PrecinctUnitScope;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

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
        if(StringUtils.isNotNull(dataScope)) {
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = signature.getMethod();
            Annotation[][] methodAnnotations = method.getParameterAnnotations();
            PrecinctUnitScope precinctUnitScope = null;
            for (Annotation[] methodAnnotation : methodAnnotations) {
                for (Annotation annotation : methodAnnotation) {
                    if(annotation.annotationType().getName().equals(PrecinctUnitScope.class.getName())){
                        precinctUnitScope = (PrecinctUnitScope) annotation;
                        break;
                    }
                }
            }
            if (StringUtils.isNotNull(precinctUnitScope) && precinctUnitScope.needDealResult()) {
                if (StringUtils.isEmpty(dataScope.getDataIds())) {
                    Class tClass = precinctUnitScope.resultType();
                    if (!tClass.getName().equals(Object.class.getName())) {
                        return ResponseResult.buildOkResult(tClass.newInstance());
                    } else {
                        return ResponseResult.buildOkResult();
                    }
                }
            }
        }
        return proceedingJoinPoint.proceed();
    }
}
