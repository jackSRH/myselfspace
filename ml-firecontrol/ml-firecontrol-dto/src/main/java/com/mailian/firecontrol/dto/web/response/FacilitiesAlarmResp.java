package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(description = "设施告警详情")
public class FacilitiesAlarmResp {
    @ApiModelProperty(value = "告警id")
    private Integer id;
    @ApiModelProperty(value = "单位id")
    private Integer unitId;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "告警时间")
    private Date alarmTime;
    @ApiModelProperty(value = "受理时间")
    private Date handleTime;
    @ApiModelProperty(value = "受理结束时间")
    private Date handleEndTime;
    @ApiModelProperty(value = "是否误报")
    private Integer misreport;
    @ApiModelProperty(value = "告警内容")
    private String alarmContent;
    @ApiModelProperty(value = "处理结果")
    private String handleResult;
    @ApiModelProperty(value = "处理人")
    private Integer handleUid;
    @ApiModelProperty(value = "向消防通信指挥中心报告时间")
    private Date upCoreTime;
    @ApiModelProperty(value = "消防通信指挥中心反馈确认时间")
    private Date coreConfirmTime;
    @ApiModelProperty(value = "消防通信指挥中心受理人姓名")
    private String coreHandleUname;
    @ApiModelProperty(value = "消防通信指挥中心接管处理情况")
    private String coreHandleResult;
    @ApiModelProperty(value = "报警类型")
    private Integer alarmType;
    @ApiModelProperty(value = "受理人姓名")
    private String handleUserName;

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Date getUpCoreTime() {
        return upCoreTime;
    }

    public void setUpCoreTime(Date upCoreTime) {
        this.upCoreTime = upCoreTime;
    }

    public Date getCoreConfirmTime() {
        return coreConfirmTime;
    }

    public void setCoreConfirmTime(Date coreConfirmTime) {
        this.coreConfirmTime = coreConfirmTime;
    }

    public String getCoreHandleUname() {
        return coreHandleUname;
    }

    public void setCoreHandleUname(String coreHandleUname) {
        this.coreHandleUname = coreHandleUname;
    }

    public String getCoreHandleResult() {
        return coreHandleResult;
    }

    public void setCoreHandleResult(String coreHandleResult) {
        this.coreHandleResult = coreHandleResult;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public Date getHandleEndTime() {
        return handleEndTime;
    }

    public void setHandleEndTime(Date handleEndTime) {
        this.handleEndTime = handleEndTime;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }

    public Integer getMisreport() {
        return misreport;
    }

    public void setMisreport(Integer misreport) {
        this.misreport = misreport;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public Integer getHandleUid() {
        return handleUid;
    }

    public void setHandleUid(Integer handleUid) {
        this.handleUid = handleUid;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
    }

    public String getHandleUserName() {
        return handleUserName;
    }

    public void setHandleUserName(String handleUserName) {
        this.handleUserName = handleUserName;
    }
}
