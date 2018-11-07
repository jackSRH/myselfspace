package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(description = "误告列表")
public class MisreportAlarmReap {
    @ApiModelProperty(value = "单位类型")
    private String unitTypeDesc;
    @ApiModelProperty(value = "报警类型")
    private String alarmTypeDesc;
    @ApiModelProperty(value = "告警时间")
    private Date alarmTime;
    @ApiModelProperty(value = "告警描述")
    private String alarmContent;
    @ApiModelProperty(value = "负责人")
    private String userName;

    public String getUnitTypeDesc() {
        return unitTypeDesc;
    }

    public void setUnitTypeDesc(String unitTypeDesc) {
        this.unitTypeDesc = unitTypeDesc;
    }

    public String getAlarmTypeDesc() {
        return alarmTypeDesc;
    }

    public void setAlarmTypeDesc(String alarmTypeDesc) {
        this.alarmTypeDesc = alarmTypeDesc;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
