package com.mailian.firecontrol.framework.config;

import com.mailian.core.shiro.JwtFilter;
import com.mailian.core.shiro.JwtMatcher;
import com.mailian.core.shiro.JwtToken;
import com.mailian.core.util.JwtUtils;
import com.mailian.core.util.RedisUtils;
import com.mailian.firecontrol.framework.shiro.MlShiroRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 权限配置加载
 * 
 * @author wangqiaoqing
 */
@Configuration
public class ShiroConfig {
    private final static Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${shiro.spring.redis.expire}")
    private int expire;

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager, RedisUtils redisUtils, JwtUtils jwtUtils) {
        logger.info("ShiroConfiguration.shirFilter()");

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
        //filters.put("corsFilter", new RestFilter());
        filters.put("authc",new JwtFilter(redisUtils,jwtUtils));

        shiroFilterFactoryBean.setFilters(filters);
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //注意过滤器配置顺序 不能颠倒
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        filterChainDefinitionMap.put("/logout", "logout");
        // 配置不会被拦截的链接 顺序判断
        /* 不拦截 swagger */
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/webjars/springfox-swagger-ui/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");

        /* 不拦截静态资源*/
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/docs/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");

        filterChainDefinitionMap.put("/system/login/ajaxLogin", "anon");
        filterChainDefinitionMap.put("/app/login/ajaxLogin", "anon");
        filterChainDefinitionMap.put("/app/login/ajaxUserLogin", "anon");

        /* 不拦截导入导出接口*/
        filterChainDefinitionMap.put("/management/deviceItem/exportItemByName", "anon");
        filterChainDefinitionMap.put("/management/deviceItem/exportItem", "anon");
        filterChainDefinitionMap.put("/management/deviceItem/importItem", "anon");

        filterChainDefinitionMap.put("/app/versionmanage/downLoadApp", "anon");

        filterChainDefinitionMap.put("/**", "authc");
        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl("/system/login/unauth");
        // 登录成功后要跳转的链接
//        shiroFilterFactoryBean.setSuccessUrl("/index");
        //未授权界面;
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public MlShiroRealm mlShiroRealm(JwtMatcher jwtMatcher) {
        MlShiroRealm mlShiroRealm = new MlShiroRealm();
        //mlShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        mlShiroRealm.setCredentialsMatcher(jwtMatcher);
        mlShiroRealm.setAuthenticationTokenClass(JwtToken.class);
        //开启shiro认证缓存
        mlShiroRealm.setAuthenticationCachingEnabled(true);
        //开启shiro授权缓存
        mlShiroRealm.setAuthorizationCachingEnabled(true);
        return mlShiroRealm;
    }


    @Bean
    public SecurityManager securityManager(JwtMatcher jwtMatcher) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        securityManager.setRealm(mlShiroRealm(jwtMatcher));
        return securityManager;
    }

    /**
     * 配置shiro redisManager
     * <p>
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    private RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setExpire(expire);// 配置缓存过期时间*/
        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * <p>
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    private RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * AOP生效
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

}
