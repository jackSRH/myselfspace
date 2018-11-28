package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(description = "当前告警信息")
public class CurAlarmResp {
    @ApiModelProperty(value = "告警id")
    private Integer id;
    @ApiModelProperty(value = "告警时间")
    private Date alarmTime;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "告警内容")
    private String alarmContent;
    @ApiModelProperty(value = "告警状态")
    private Integer alarmStatus;
    @ApiModelProperty(value = "距离现在告警了多少小时")
    private Integer alarmDuration;
    @ApiModelProperty(value = "告警类型 1:告警，2：预警")
    private Integer alarmType;
    @ApiModelProperty(value = "告警状态描述")
    private String alarmStatusDesc;


    public Integer getAlarmDuration() {
        return alarmDuration;
    }

    public void setAlarmDuration(Integer alarmDuration) {
        this.alarmDuration = alarmDuration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(Integer alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }


    public Integer getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmStatusDesc() {
        return alarmStatusDesc;
    }

    public void setAlarmStatusDesc(String alarmStatusDesc) {
        this.alarmStatusDesc = alarmStatusDesc;
    }
}
