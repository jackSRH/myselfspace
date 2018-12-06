package com.mailian.firecontrol.framework.resolver;

import com.alibaba.fastjson.JSONObject;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.manager.SystemManager;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.framework.annotation.PrecinctUnitScope;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/12/6
 * @Description:
 */
public class PrecinctScopeHandlerResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(DataScope.class) && methodParameter.hasParameterAnnotation(PrecinctUnitScope.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        PrecinctUnitScope precinctUnitScope = methodParameter.getParameterAnnotation(PrecinctUnitScope.class);
        DataScope dataScope = null;
        Integer pPrecinctId = null;
        Integer pUnitId = null;
        if(precinctUnitScope.hasPrecinctOrUnit()){
            if(precinctUnitScope.isJson()){
                String jsonBody = request.getParameter(precinctUnitScope.reqParamName());
                if(StringUtils.isNotEmpty(jsonBody)) {
                    JSONObject jsonObject = JSONObject.parseObject(jsonBody);
                    if(jsonObject.containsKey(precinctUnitScope.precinctParamName())){
                        pPrecinctId = jsonObject.getInteger(precinctUnitScope.precinctParamName());
                    }
                    if(jsonObject.containsKey(precinctUnitScope.unitParamName())){
                        pUnitId = jsonObject.getInteger(precinctUnitScope.unitParamName());
                    }
                }
            }else{
                String pValue = request.getParameter(precinctUnitScope.precinctParamName());
                if(StringUtils.isNotEmpty(pValue)){
                    pPrecinctId = Integer.parseInt(pValue);
                }

                String uValue = request.getParameter(precinctUnitScope.unitParamName());
                if(StringUtils.isNotEmpty(uValue)){
                    pUnitId = Integer.parseInt(uValue);
                }
            }
        }

        if(!SystemManager.isAdminRole(shiroUser.getRoles())) {
            String scopeName = precinctUnitScope.palias();
            List<Integer> dataIds = new ArrayList<>();
            if(StringUtils.isNotEmpty(shiroUser.getUnitId())){
                scopeName = precinctUnitScope.ualias();
                if(StringUtils.isEmpty(pUnitId) || pUnitId.equals(pUnitId)){
                    dataIds = Arrays.asList(shiroUser.getUnitId());
                }
            }else {
                if(StringUtils.isNotEmpty(shiroUser.getPrecinctIds())){
                    if(StringUtils.isNotEmpty(pPrecinctId)){
                        if(shiroUser.getPrecinctIds().contains(pPrecinctId)){
                            dataIds = Arrays.asList(pPrecinctId);
                        }
                    }else{
                        dataIds = shiroUser.getPrecinctIds();
                    }
                }

            }
            dataScope = new DataScope(scopeName,dataIds);
        }else{
            if(StringUtils.isNotEmpty(pPrecinctId)){
                dataScope = new DataScope(precinctUnitScope.palias(),Arrays.asList(pPrecinctId));
            }
            if(StringUtils.isNotEmpty(pUnitId)){
                dataScope = new DataScope(precinctUnitScope.ualias(), Arrays.asList(pUnitId));
            }
        }
        return dataScope;
    }
}
