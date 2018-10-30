/*
*
* UnitDevice.java
* Copyright(C) 2018-2099 深圳市脉联电子有限公司
* @date 2018-10-30
*/
package com.mailian.firecontrol.dao.auto.model;

import com.mailian.core.base.model.BaseDomain;
import java.io.Serializable;

public class UnitDevice extends BaseDomain implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 网关id
     */
    private String deviceId;

    /**
     * 单位id
     */
    private Integer unitId;

    /**
     * t_unit_device
     */
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }
}