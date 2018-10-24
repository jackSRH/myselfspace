package com.mailian.core.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Auther: mljav
 * @Date: 2018/7/21
 * @Description:
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
