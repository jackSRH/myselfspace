package com.mailian.firecontrol.dto.web.request;

import com.mailian.core.bean.BasePage;
import com.mailian.core.manager.ValidationManager;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/27
 * @Description:
 */
@ApiModel(description = "用户查询参数")
public class UserReq extends BasePage {
    @ApiModelProperty(value = "用户名")
    @NotNull(message = "{user.userName.notNull}",groups = ValidationManager.AddValidation.class)
    private String userName;

    @ApiModelProperty(value = "姓名",notes = "支持模糊查询")
    @NotNull(message = "{user.fullName.notNull}",groups = ValidationManager.CommonValidation.class)
    private String fullName;

    @ApiModelProperty(value = "电话")
    private String phone;

    /*新增参数*/
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "角色id")
    private List<Integer> roleIds;

    @ApiModelProperty(value = "管辖区id")
    private List<Integer> precinctIds;

    /*修改参数*/
    @ApiModelProperty(value = "用户id")
    private Integer id;

    @ApiModelProperty(value = "单位")
    private Integer unitId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }
}
