/*
*
* Precinct.java
* Copyright(C) 2018-2099 深圳市脉联电子有限公司
* @date 2018-10-31
*/
package com.mailian.firecontrol.dao.auto.model;

import com.mailian.core.base.model.BaseDomain;
import java.io.Serializable;

public class Precinct extends BaseDomain implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 管辖区名称
     */
    private String precinctName;

    /**
     * 负责人
     */
    private String dutyName;

    /**
     * 负责人电话
     */
    private String dutyPhone;

    /**
     * 区域id
     */
    private Integer areaId;

    /**
     * 省份id
     */
    private Integer provinceId;

    /**
     * 城市id
     */
    private Integer cityId;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

    /**
     * t_precinct
     */
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrecinctName() {
        return precinctName;
    }

    public void setPrecinctName(String precinctName) {
        this.precinctName = precinctName == null ? null : precinctName.trim();
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName == null ? null : dutyName.trim();
    }

    public String getDutyPhone() {
        return dutyPhone;
    }

    public void setDutyPhone(String dutyPhone) {
        this.dutyPhone = dutyPhone == null ? null : dutyPhone.trim();
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}