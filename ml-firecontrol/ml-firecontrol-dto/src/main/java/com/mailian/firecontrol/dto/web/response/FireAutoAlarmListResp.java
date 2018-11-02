package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(description = "火灾自动告警列表")
public class FireAutoAlarmListResp {
    @ApiModelProperty(value = "告警id")
    private Integer id;
    @ApiModelProperty(value = "告警类型")
    private String alarmType;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "设施名称")
    private String facilitiesName;
    @ApiModelProperty(value = "是否误报")
    private String misreport;
    @ApiModelProperty(value = "当前状态")
    private String curStatus;
    @ApiModelProperty(value = "告警时间")
    private Date alarmTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getFacilitiesName() {
        return facilitiesName;
    }

    public void setFacilitiesName(String facilitiesName) {
        this.facilitiesName = facilitiesName;
    }

    public String getMisreport() {
        return misreport;
    }

    public void setMisreport(String misreport) {
        this.misreport = misreport;
    }

    public String getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(String curStatus) {
        this.curStatus = curStatus;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }
}
