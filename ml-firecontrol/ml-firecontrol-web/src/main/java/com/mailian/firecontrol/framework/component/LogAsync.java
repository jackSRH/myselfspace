package com.mailian.firecontrol.framework.component;

import com.alibaba.fastjson.JSONObject;
import com.mailian.core.annotation.Log;
import com.mailian.core.enums.Status;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.dao.auto.model.OperLog;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.framework.util.ShiroUtils;
import com.mailian.firecontrol.service.OperLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/9
 * @Description:
 */
@Component
public class LogAsync {
    private static final Logger log = LoggerFactory.getLogger(LogAsync.class);

    @Autowired
    private OperLogService operLogService;

    @Async
    public void handleLog(final String requestUrl,final Map<String, String[]> paramMap, final JoinPoint joinPoint, final Exception e) {
        try {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }

            // 获取当前的用户
            ShiroUser currentUser = ShiroUtils.getCurUser();

            // *========数据库日志=========*//
            OperLog operLog = new OperLog();
            operLog.setStatus(Status.NORMAL.id);
            // 请求的地址
            String ip = ShiroUtils.getIp();
            operLog.setOperIp(ip);
            // 操作地点
            //operLog.setOperLocation(AddressUtils.getRealAddressByIP(ip));

            operLog.setOperUrl(requestUrl);
            if (currentUser != null) {
                operLog.setOperName(currentUser.getFullName());
            }

            if (e != null) {
                operLog.setStatus(Status.DISABLE.id);
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            // 处理设置注解上的参数
            getControllerMethodDescription(paramMap,controllerLog, operLog,joinPoint.getArgs());
            // 保存数据库
            operLogService.insert(operLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:", exp);
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     * @param log
     * @param operLog
     * @throws Exception
     */
    private void getControllerMethodDescription(Map<String, String[]> paramMap,Log log, OperLog operLog,Object [] params) throws Exception {
        // 设置action动作
        operLog.setAction(log.action());
        // 设置标题
        operLog.setTitle(log.title());
        // 设置channel
        operLog.setChannel(log.channel().id);
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(paramMap,operLog,params);
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog
     */
    private void setRequestValue(Map<String, String[]> paramMap,OperLog operLog,Object [] params)
    {
        String paramsStr = "";
        if(StringUtils.isEmpty(paramMap)){
            List<Object> paramList = new ArrayList<>();
            for (Object o : params) {
                if(o instanceof ShiroUser){
                    continue;
                }
                paramList.add(o);
            }
            paramsStr = JSONObject.toJSONString(paramList);
        }else{
            paramsStr = JSONObject.toJSONString(paramMap);
        }
        operLog.setOperParam(StringUtils.substring(paramsStr, 0, 500));
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }
}
