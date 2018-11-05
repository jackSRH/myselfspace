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
     * 管辖区id
     */
    private Integer precinctId;

    /**
     * 联系电话(管辖区)
     */
    private String contactPhone;

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
}
