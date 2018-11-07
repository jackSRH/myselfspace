package com.mailian.firecontrol.dto.app;

import com.mailian.core.bean.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/3
 * @Description:
 */
@ApiModel(description = "用户信息")
public class AppUser extends BaseInfo implements Serializable {

    @ApiModelProperty(value = "用户id")
    private Integer id;
    @ApiModelProperty(value = "用户全称")
    private String fullName;
    @ApiModelProperty(value = "电话")
    private String phone;
    @ApiModelProperty(value = "token")
    private String token;
    @ApiModelProperty(value = "用户唯一键")
    private String uniqueKey;
    @ApiModelProperty(value = "所属单位")
    private Integer unitId;

    /*详细信息*/
    @ApiModelProperty(value = "用户名称首字母")
    private String nameInitials;
    @ApiModelProperty(value = "是否管理员")
    private boolean isManager;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getNameInitials() {
        return nameInitials;
    }

    public void setNameInitials(String nameInitials) {
        this.nameInitials = nameInitials;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }
}
