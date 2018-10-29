/*
*
* Load.java
* Copyright(C) 2018-2099 深圳市脉联电子有限公司
* @date 2018-10-26
*/
package com.mailian.firecontrol.dao.auto.model;

import com.mailian.core.base.model.BaseDomain;
import java.io.Serializable;

public class Load extends BaseDomain implements Serializable {
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
    private String name;

    /**
     * 容量
     */
    private Integer capacity;

    /**
     * 负载
     */
    private String loadId;

    /**
     * 能耗
     */
    private String energyId;

    /**
     * 功率因数
     */
    private String powerFactorId;

    /**
     * t_load
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getLoadId() {
        return loadId;
    }

    public void setLoadId(String loadId) {
        this.loadId = loadId == null ? null : loadId.trim();
    }

    public String getEnergyId() {
        return energyId;
    }

    public void setEnergyId(String energyId) {
        this.energyId = energyId == null ? null : energyId.trim();
    }

    public String getPowerFactorId() {
        return powerFactorId;
    }

    public void setPowerFactorId(String powerFactorId) {
        this.powerFactorId = powerFactorId == null ? null : powerFactorId.trim();
    }
}