package com.mailian.core.shiro;

import com.mailian.core.config.SystemConfig;
import com.mailian.core.constants.CoreCommonConstant;
import com.mailian.core.util.JwtUtils;
import com.mailian.core.util.RedisKeys;
import com.mailian.core.util.RedisUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/21
 * @Description:凭证匹配器
 */
@Component
public class JwtMatcher implements CredentialsMatcher {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private SystemConfig systemConfig;

    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        JwtToken jwtToken = (JwtToken) authenticationToken;
        if(null == jwtToken){
            throw new AuthenticationException("errJwt");
        }
        String token = (String) jwtToken.getPrincipal();
        Claims claims = jwtUtils.getClaimByToken(token);
        String userName = jwtUtils.getUserName(claims);
        String claimPassword = jwtUtils.getPassword(claims);
        String password = authenticationInfo.getCredentials().toString();

        String redisKey = RedisKeys.getSysConfigKey(systemConfig.serverIdCard,CoreCommonConstant.REDIS_TOKEN_KEY+userName);
        if(StringUtils.isEmpty(password) || !password.equals(claimPassword)){
            redisUtils.delete(redisKey);
            throw new AuthenticationException("密码错误"); // 令牌错误
        }

        lock.lock();
        try {
            String redisToken = redisUtils.get(redisKey);
            if (StringUtils.isEmpty(redisToken)) {
                throw new AuthenticationException("token失效"); // token失效
            }

            if (!token.equals(redisToken)) {
                throw new AuthenticationException(CoreCommonConstant.REFRESH_TOKEN + redisToken);
            }

            if (jwtUtils.isRefreshToken(claims)) {
                String newToken = jwtUtils.generateToken(UUID.randomUUID().toString(), userName, password);
                throw new AuthenticationException(CoreCommonConstant.REFRESH_TOKEN + newToken);
            }
        }finally {
            lock.unlock();
        }
        return true;
    }
}
