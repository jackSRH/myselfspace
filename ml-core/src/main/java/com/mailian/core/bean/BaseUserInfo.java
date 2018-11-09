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
    /*用户密码*/
    private String password;
    /*用户真实姓名*/
    private String fullName;
    /*电话*/
    private String phone;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
