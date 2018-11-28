package com.mailian.core.util;

import com.mailian.core.bean.BaseUserInfo;
import com.mailian.core.config.SystemConfig;
import com.mailian.core.constants.CoreCommonConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * jwt工具类
 * @author seits
 * @email *.com
 * @date 2017/9/21 22:21
 */
@ConfigurationProperties(prefix = "mailian.jwt")
@Component
public class JwtUtils {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private SystemConfig systemConfig;

    private String secret;
    private long expire;

    /**
     * 生成jwt token
     */
    public String generateTokenByMd5(String subject,String userName,String password) {
        password = MD5Util.md5(password);
        return generateToken(subject,userName,password);
    }

    /**
     * 生成jwt token
     */
    public String generateToken(String subject,String userName,String password) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(subject)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .claim("username",userName)
                .claim("password",password)
                .compact();
        setTokenToRedis(userName,token);
        return token;
    }

    /**
     *
     * @param userName
     * @param token
     */
    public void setTokenToRedis(String userName,String token){
        String redisKey = RedisKeys.getSysConfigKey(systemConfig.serverIdCard,CoreCommonConstant.REDIS_TOKEN_KEY+userName);
        redisUtils.set(redisKey,token,CoreCommonConstant.REFRESH_TOKEN_TIME);
    }

    public Claims getClaimByToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * token是否过期
     * @return  true：过期
     */
    public boolean isTokenExpired(Claims claims) {
        return (System.currentTimeMillis() - claims.getIssuedAt().getTime())/(1000 * 60) > 30;
    }

    /**
     * 是否需要刷新token
     * @param claims
     * @return
     */
    public boolean isRefreshToken(Claims claims){
        return (System.currentTimeMillis() - claims.getIssuedAt().getTime())/(1000 * 60) > 1;
    }

    /**
     * 获取用户名
     * @param claims
     * @return
     */
    public String getUserName(Claims claims){
        return (String) claims.get("username");
    }

    /**
     * 获取密码
     * @param claims
     * @return
     */
    public String getPassword(Claims claims){
        return (String) claims.get("password");
    }


    /**
     * 登录时获取token
     * @param userName
     * @param password
     * @return
     */
    public String getLoginToken(final String userName,final String password){
        String redisKey = RedisKeys.getSysConfigKey(systemConfig.serverIdCard,CoreCommonConstant.REDIS_TOKEN_KEY+userName);
        String token = redisUtils.get(redisKey);
        if(StringUtils.isEmpty(token)){
            token = generateTokenByMd5(UUID.randomUUID().toString(),userName,password);
        }else{
            Claims claims = getClaimByToken(token);
            String claimPassword = getPassword(claims);
            String md5Password = MD5Util.md5(password);
            if(!md5Password.equals(claimPassword)){
                throw new AuthenticationException("账号密码错误");
            }

            if(isRefreshToken(claims)){
                token = generateToken(UUID.randomUUID().toString(),userName,md5Password);
            }
        }
        return token;
    }

    public void loginOut(){
        Subject subject = SecurityUtils.getSubject();
        if(StringUtils.isNotNull(subject)){
            BaseUserInfo baseUserInfo = (BaseUserInfo) subject.getPrincipal();
            String userName = baseUserInfo.getUserName();
            String redisKey = RedisKeys.getSysConfigKey(systemConfig.serverIdCard,CoreCommonConstant.REDIS_TOKEN_KEY+userName);
            redisUtils.delete(redisKey);
            subject.logout();
        }
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
}
