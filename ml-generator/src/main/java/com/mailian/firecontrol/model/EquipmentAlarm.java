/*
*
* EquipmentAlarm.java
* Copyright(C) 2018-2099 深圳市脉联电子有限公司
* @date 2018-10-23
*/
package com.mailian.firecontrol.model;

import com.mailian.core.base.model.BaseDomain;
import java.io.Serializable;
import java.util.Date;

public class EquipmentAlarm extends BaseDomain implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 告警id
     */
    private Integer alarmid;

    /**
     * 告警数据项id
     */
    private String alarmitemid;

    /**
     * 设备id
     */
    private Integer equipmentId;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 设备类型id
     */
    private Integer equipmentTypeId;

    /**
     * 位置id
     */
    private Integer siteId;

    /**
     * 站点id
     */
    private Integer sid;

    /**
     * 地址
     */
    private String address;

    /**
     * 告警内容
     */
    private String alarmContent;

    /**
     * 告警等级
     */
    private Byte level;

    /**
     * 告警状态
     */
    private Byte alarmStatus;

    /**
     * 告警时间
     */
    private Date alarmTime;

    /**
     * 告警结束时间
     */
    private Date alarmEndTime;

    /**
     * 处理状态
     */
    private Byte handleStatus;

    /**
     * t_equipment_alarm
     */
    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAlarmid() {
        return alarmid;
    }

    public void setAlarmid(Integer alarmid) {
        this.alarmid = alarmid;
    }

    public String getAlarmitemid() {
        return alarmitemid;
    }

    public void setAlarmitemid(String alarmitemid) {
        this.alarmitemid = alarmitemid == null ? null : alarmitemid.trim();
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName == null ? null : equipmentName.trim();
    }

    public Integer getEquipmentTypeId() {
        return equipmentTypeId;
    }

    public void setEquipmentTypeId(Integer equipmentTypeId) {
        this.equipmentTypeId = equipmentTypeId;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent == null ? null : alarmContent.trim();
    }

    public Byte getLevel() {
        return level;
    }

    public void setLevel(Byte level) {
        this.level = level;
    }

    public Byte getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(Byte alarmStatus) {
        this.alarmStatus = alarmStatus;
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

    public Byte getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Byte handleStatus) {
        this.handleStatus = handleStatus;
    }
}