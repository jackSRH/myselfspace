package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(description = "火灾自动告警列表")
public class FireAutoAlarmResp {
    @ApiModelProperty(value = "告警id")
    private Integer id;
    @ApiModelProperty(value = "告警类型")
    private String alarmType;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "设施名称")
    private String facilitiesName;
    @ApiModelProperty(value = "是否误报")
    private Integer misreport;
    @ApiModelProperty(value = "处理状态")
    private Integer handleStatus;
    @ApiModelProperty(value = "告警时间")
    private Date alarmTime;
    @ApiModelProperty(value = "告警内容")
    private String alarmContent;
    @ApiModelProperty(value = "受理时间")
    private Date handleTime;
    @ApiModelProperty(value = "处理结果")
    private String handleResult;
    @ApiModelProperty(value = "处理时间")
    private Date handleEndTime;


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

    public Integer getMisreport() {
        return misreport;
    }

    public void setMisreport(Integer misreport) {
        this.misreport = misreport;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
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

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public Date getHandleEndTime() {
        return handleEndTime;
    }

    public void setHandleEndTime(Date handleEndTime) {
        this.handleEndTime = handleEndTime;
    }
}
