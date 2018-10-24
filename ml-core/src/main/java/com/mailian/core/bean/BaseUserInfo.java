package com.mailian.core.bean;

import java.io.Serializable;
import java.util.Set;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/22
 * @Description:
 */
public class BaseUserInfo implements Serializable {
    private static final long serialVersionUID = -911363160641023617L;

    /*用户id*/
    private Integer id;
    /*用户名*/
    private String userName;
    /*角色*/
    private Set<String> roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
