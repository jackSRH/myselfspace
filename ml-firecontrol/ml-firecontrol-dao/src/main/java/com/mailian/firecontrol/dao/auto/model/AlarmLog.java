/*
*
* AlarmLog.java
* Copyright(C) 2018-2099 深圳市脉联电子有限公司
* @date 2018-11-13
*/
package com.mailian.firecontrol.dao.auto.model;

import com.mailian.core.base.model.BaseDomain;
import java.io.Serializable;
import java.util.Date;

public class AlarmLog extends BaseDomain implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 警情id
     */
    private Integer alarmId;

    /**
     * 操作人姓名
     */
    private String optName;

    /**
     * 操作人角色
     */
    private String optRole;

    /**
     * 操作时间
     */
    private Date optTime;

    /**
     * 操作类型  0:告警  1:响应告警  2:确定告警  3:处理告警中 4:告警已处理
     */
    private Integer optType;

    /**
     * 操作内容
     */
    private String optContent;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

    /**
     * t_alarm_log
     */
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName == null ? null : optName.trim();
    }

    public String getOptRole() {
        return optRole;
    }

    public void setOptRole(String optRole) {
        this.optRole = optRole == null ? null : optRole.trim();
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
        this.optContent = optContent == null ? null : optContent.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}