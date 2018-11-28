package com.mailian.firecontrol.dto.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/13
 * @Description:
 */
@ApiModel(description = "进度详情")
public class ProgressDetailResp implements Serializable {
    private static final long serialVersionUID = 7617642202759056653L;
    @ApiModelProperty(value = "操作人")
    private String optName;
    @ApiModelProperty(value = "角色名")
    private String roleName;
    @ApiModelProperty(value = "操作时间")
    private Date optTime;
    @ApiModelProperty(value = "操作类型 0:告警  1:响应告警  2:确定告警  3:处理告警中 4:告警已处理")
    private Integer optType;
    @ApiModelProperty(value = "操作类型描述")
    private String optTypeDesc;
    @ApiModelProperty(value = "操作内容")
    private String optContent;

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Date getOptTime() {
        return optTime;
    }

    public void setOptTime(Date optTime) {
        this.optTime = optTime;
    }

    public Integer getOptType() {
        return optType;
    }

    public void setOptType(Integer optType) {
        this.optType = optType;
    }

    public String getOptContent() {
        return optContent;
    }

    public void setOptContent(String optContent) {
        this.optContent = optContent;
    }

    public String getOptTypeDesc() {
        return optTypeDesc;
    }

    public void setOptTypeDesc(String optTypeDesc) {
        this.optTypeDesc = optTypeDesc;
    }
}
