/*
*
* FacilitiesAlarm.java
* Copyright(C) 2018-2099 深圳市脉联电子有限公司
* @date 2018-10-26
*/
package com.mailian.firecontrol.dao.auto.model;

import com.mailian.core.base.model.BaseDomain;
import java.io.Serializable;
import java.util.Date;

public class FacilitiesAlarm extends BaseDomain implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 单位id
     */
    private Integer unitId;

    /**
     * 地址(编号)
     */
    private String structAddress;

    /**
     * 设施id
     */
    private Integer facilitiesId;

    /**
     * 告警id
     */
    private Integer alarmId;

    /**
     * 告警数据项id
     */
    private String alarmItemId;

    /**
     * 告警内容
     */
    private String alarmContent;

    /**
     * 告警等级
     */
    private Integer alarmLevel;

    /**
     * 告警状态
     */
    private Integer alarmStatus;

    /**
     * 告警类型
     */
    private Integer alarmType;

    /**
     * 告警时间
     */
    private Date alarmTime;

    /**
     * 告警结束时间
     */
    private Date alarmEndTime;

    /**
     * 告警方式 0:自动 1:人工
     */
    private Integer alarmWay;

    /**
     * 受理时间
     */
    private Date handleTime;

    /**
     * 受理结束时间
     */
    private Date handleEndTime;

    /**
     * 当前状态 0:未处理、1:已响应、2:处理中、3:已处理
     */
    private Integer handleStatus;

    /**
     * 处理结果
     */
    private String handleResult;

    /**
     * 处理人
     */
    private Integer handleUid;

    /**
     * 是否误报 1:误报 2:有效 3:其它
     */
    private Integer misreport;

    /**
     * 起火原因
     */
    private String alarmReason;

    /**
     * 起火面积
     */
    private String alarmArea;

    /**
     * 死亡人数
     */
    private String dieNum;

    /**
     * 受伤人数
     */
    private String injured;

    /**
     * 财产损失
     */
    private String propertyLoss;

    /**
     * 补救概况
     */
    private String emedialMeasures;

    /**
     * 向消防通信指挥中心报告时间
     */
    private Date upCoreTime;

    /**
     * 消防通信指挥中心反馈确认时间
     */
    private Date coreConfirmTime;

    /**
     * 消防通信指挥中心受理人姓名
     */
    private String coreHandleUname;

    /**
     * 消防通信指挥中心接管处理情况
     */
    private String coreHandleResult;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

    /**
     * t_facilities_alarm
     */
    private static final long serialVersionUID = 1L;

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

    public String getStructAddress() {
        return structAddress;
    }

    public void setStructAddress(String structAddress) {
        this.structAddress = structAddress == null ? null : structAddress.trim();
    }

    public Integer getFacilitiesId() {
        return facilitiesId;
    }

    public void setFacilitiesId(Integer facilitiesId) {
        this.facilitiesId = facilitiesId;
    }

    public Integer getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmItemId() {
        return alarmItemId;
    }

    public void setAlarmItemId(String alarmItemId) {
        this.alarmItemId = alarmItemId == null ? null : alarmItemId.trim();
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent == null ? null : alarmContent.trim();
    }

    public Integer getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(Integer alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public Integer getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(Integer alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public Integer getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Date getAlarmEndTime() {
        return alarmEndTime;
    }

    public void setAlarmEndTime(Date alarmEndTime) {
        this.alarmEndTime = alarmEndTime;
    }

    public Integer getAlarmWay() {
        return alarmWay;
    }

    public void setAlarmWay(Integer alarmWay) {
        this.alarmWay = alarmWay;
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

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult == null ? null : handleResult.trim();
    }

    public Integer getHandleUid() {
        return handleUid;
    }

    public void setHandleUid(Integer handleUid) {
        this.handleUid = handleUid;
    }

    public Integer getMisreport() {
        return misreport;
    }

    public void setMisreport(Integer misreport) {
        this.misreport = misreport;
    }

    public String getAlarmReason() {
        return alarmReason;
    }

    public void setAlarmReason(String alarmReason) {
        this.alarmReason = alarmReason == null ? null : alarmReason.trim();
    }

    public String getAlarmArea() {
        return alarmArea;
    }

    public void setAlarmArea(String alarmArea) {
        this.alarmArea = alarmArea == null ? null : alarmArea.trim();
    }

    public String getDieNum() {
        return dieNum;
    }

    public void setDieNum(String dieNum) {
        this.dieNum = dieNum == null ? null : dieNum.trim();
    }

    public String getInjured() {
        return injured;
    }

    public void setInjured(String injured) {
        this.injured = injured == null ? null : injured.trim();
    }

    public String getPropertyLoss() {
        return propertyLoss;
    }

    public void setPropertyLoss(String propertyLoss) {
        this.propertyLoss = propertyLoss == null ? null : propertyLoss.trim();
    }

    public String getEmedialMeasures() {
        return emedialMeasures;
    }

    public void setEmedialMeasures(String emedialMeasures) {
        this.emedialMeasures = emedialMeasures == null ? null : emedialMeasures.trim();
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
        this.coreHandleUname = coreHandleUname == null ? null : coreHandleUname.trim();
    }

    public String getCoreHandleResult() {
        return coreHandleResult;
    }

    public void setCoreHandleResult(String coreHandleResult) {
        this.coreHandleResult = coreHandleResult == null ? null : coreHandleResult.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}