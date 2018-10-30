package com.mailian.core.shiro;

import com.mailian.core.bean.ResponseResult;
import com.mailian.core.constants.CoreCommonConstant;
import com.mailian.core.enums.ResponseCode;
import com.mailian.core.util.JwtUtils;
import com.mailian.core.util.RedisUtils;
import com.mailian.core.util.StringUtils;
import com.mailian.core.util.Tools;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: mljav
 * @Date: 2018/7/21
 * @Description:
 */
public class JwtFilter extends AuthenticatingFilter {
    private Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private RedisUtils redisUtils;
    private JwtUtils jwtUtils;

    public JwtFilter(RedisUtils redisUtils,JwtUtils jwtUtils) {
        this.redisUtils = redisUtils;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token
        String token = getRequestToken((HttpServletRequest) request);

        if(StringUtils.isEmpty(token)){
            return null;
        }

        return new JwtToken(token);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //获取请求token
        String token = getRequestToken((HttpServletRequest) request);

        if(token==null || "".equals(token.trim())){
            Tools.writeJson(ResponseResult.buildResult(ResponseCode.NO_LOGIN),response);
            return false;
        }

        JwtToken jwtToken = new JwtToken(token);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(jwtToken);

            if(null != mappedValue){
                String[] value = (String[])mappedValue;
                for (String permission : value) {
                    if(permission==null || "".equals(permission.trim())){
                        continue;
                    }
                    if(subject.isPermitted(permission)){
                        return true;
                    }
                }
            }
            return true;
        }catch (AuthenticationException e){
            // 如果需要刷新token
            if (StringUtils.isNotEmpty(e.getMessage()) && e.getMessage().startsWith(CoreCommonConstant.REFRESH_TOKEN)) {
                String newToken = e.getMessage().substring(CoreCommonConstant.REFRESH_TOKEN.length());
                Map<String,String> resultMap = new HashMap<>();
                resultMap.put("token",newToken);
                Tools.writeJson(ResponseResult.buildResult(ResponseCode.REFRESH_TOKEN,resultMap),response);
                return false;
            }else{
                Tools.writeJson(ResponseResult.buildResult(ResponseCode.EXPIRED_TOKEN),response);
                return false;
            }
        }catch (Exception e){
        }
        Tools.writeJson(ResponseResult.buildResult(ResponseCode.INVALID_TOKEN),response);
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token，校验是否存在token
        String token = getRequestToken((HttpServletRequest) request);
        if(StringUtils.isEmpty(token)){
            Tools.writeJson(ResponseResult.buildResult(ResponseCode.INVALID_TOKEN),response);
            return false;
        }

        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        //处理登录失败的异常
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        Tools.writeJson(new ResponseResult(ResponseCode.FAIL.code,throwable.getMessage()),response);

        return false;
    }

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return this.isAccessAllowed(request, response, mappedValue) || this.onAccessDenied(request, response, mappedValue);
    }

    /**
     * 获取请求的token
     */
    private String getRequestToken(HttpServletRequest httpRequest){
        //从header中获取token
        String token = httpRequest.getHeader(CoreCommonConstant.ML_TOKEN);

        //如果header中不存在token，则从参数中获取token
        if(StringUtils.isEmpty(token)){
            token = httpRequest.getParameter(CoreCommonConstant.ML_TOKEN);
        }

        return token;
    }


}
