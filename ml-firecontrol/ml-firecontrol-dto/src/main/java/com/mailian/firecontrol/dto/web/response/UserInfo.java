package com.mailian.firecontrol.dto.web.response;

import com.fasterxml.jackson.annotation.JsonView;
import com.mailian.core.bean.BaseInfo;
import com.mailian.core.manager.ViewManager;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/29
 * @Description:
 */
@ApiModel(description = "用户信息")
public class UserInfo extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 5691500204271550385L;

    @ApiModelProperty(value = "用户id")
    private Integer id;
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "用户全称")
    private String fullName;
    @ApiModelProperty(value = "电话")
    private String phone;
    @ApiModelProperty(value = "token")
    private String token;
    @ApiModelProperty(value = "菜单信息")
    private List<MenuResp> menuInfoList;
    @ApiModelProperty(value = "拥有的角色列表")
    private List<Integer> roleIds;
    @ApiModelProperty(value = "拥有的管辖区列表")
    private List<Integer> precinctIds;

    @JsonView(value = ViewManager.SimpleView.class)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public List<MenuResp> getMenuInfoList() {
        return menuInfoList;
    }

    public void setMenuInfoList(List<MenuResp> menuInfoList) {
        this.menuInfoList = menuInfoList;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public List<Integer> getPrecinctIds() {
        return precinctIds;
    }

    public void setPrecinctIds(List<Integer> precinctIds) {
        this.precinctIds = precinctIds;
    }
}
