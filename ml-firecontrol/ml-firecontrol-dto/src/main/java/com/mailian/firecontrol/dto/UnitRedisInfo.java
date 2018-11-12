package com.mailian.firecontrol.dto;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/1
 * @Description: 用于redis缓存 （勿改）
 */
public class UnitRedisInfo {
    /**
     * ID
     */
    private Integer id;
    /**
     * 网关id
     */
    private String deviceId;
    /**
     * 单位名称
     */
    private String unitName;
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
     * 管辖区id
     */
    private Integer precinctId;

    /**
     * 联系电话(管辖区)
     */
    private String contactPhone;

    /**
     * 单位类型
     */
    private Integer unitType;

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }

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
        this.deviceId = deviceId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getPrecinctId() {
        return precinctId;
    }

    public void setPrecinctId(Integer precinctId) {
        this.precinctId = precinctId;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
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
}
