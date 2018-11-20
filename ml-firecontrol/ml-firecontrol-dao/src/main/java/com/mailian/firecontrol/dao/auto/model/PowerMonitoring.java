/*
*
* PowerMonitoring.java
* Copyright(C) 2018-2099 深圳市脉联电子有限公司
* @date 2018-11-19
*/
package com.mailian.firecontrol.dao.auto.model;

import com.mailian.core.base.model.BaseDomain;
import java.io.Serializable;

public class PowerMonitoring extends BaseDomain implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 单位id
     */
    private Integer unitId;

    /**
     * 名称
     */
    private String pmName;

    /**
     * 总负荷
     */
    private String totalLoad;

    /**
     * 电压
     */
    private String voltage;

    /**
     * 电流
     */
    private String eleCurrent;

    /**
     * 功率因素
     */
    private String powerFactor;

    /**
     * 线缆温度
     */
    private String cableTemp;

    /**
     * 漏电电流
     */
    private String leakageCurrent;

    /**
     * t_power_monitoring
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

    public String getPmName() {
        return pmName;
    }

    public void setPmName(String pmName) {
        this.pmName = pmName == null ? null : pmName.trim();
    }

    public String getTotalLoad() {
        return totalLoad;
    }

    public void setTotalLoad(String totalLoad) {
        this.totalLoad = totalLoad == null ? null : totalLoad.trim();
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage == null ? null : voltage.trim();
    }

    public String getEleCurrent() {
        return eleCurrent;
    }

    public void setEleCurrent(String eleCurrent) {
        this.eleCurrent = eleCurrent == null ? null : eleCurrent.trim();
    }

    public String getPowerFactor() {
        return powerFactor;
    }

    public void setPowerFactor(String powerFactor) {
        this.powerFactor = powerFactor == null ? null : powerFactor.trim();
    }

    public String getCableTemp() {
        return cableTemp;
    }

    public void setCableTemp(String cableTemp) {
        this.cableTemp = cableTemp == null ? null : cableTemp.trim();
    }

    public String getLeakageCurrent() {
        return leakageCurrent;
    }

    public void setLeakageCurrent(String leakageCurrent) {
        this.leakageCurrent = leakageCurrent == null ? null : leakageCurrent.trim();
    }
}