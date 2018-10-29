/*
*
* Facilities.java
* Copyright(C) 2018-2099 深圳市脉联电子有限公司
* @date 2018-10-26
*/
package com.mailian.firecontrol.dao.auto.model;

import com.mailian.core.base.model.BaseDomain;
import java.io.Serializable;
import java.util.Date;

public class Facilities extends BaseDomain implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 单位id
     */
    private Integer unitId;

    /**
     * 管辖区id
     */
    private Integer precinctId;

    /**
     * 设施名称
     */
    private String faName;

    /**
     * 设施系统类型
     */
    private Integer faSystemId;

    /**
     * 设施型号
     */
    private Integer faTypeId;

    /**
     * 设施数量
     */
    private Integer faNumber;

    /**
     * 投入使用时间
     */
    private Date useDate;

    /**
     * 生产单位名称
     */
    private String unitName;

    /**
     * 生产单位电话
     */
    private String unitPhone;

    /**
     * 维修保养单位
     */
    private String maintainName;

    /**
     * 维修保养电话
     */
    private String maintainPhone;

    /**
     * 服务状态（0有效 1无效）
     */
    private Integer serviceStatus;

    /**
     * 服务状态变化时间
     */
    private Date upstatusTime;

    /**
     * 绑定摄像头名称
     */
    private String cameraName;

    /**
     * 摄像头拍摄位置
     */
    private String cameraAddress;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

    /**
     * t_facilities
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

    public Integer getPrecinctId() {
        return precinctId;
    }

    public void setPrecinctId(Integer precinctId) {
        this.precinctId = precinctId;
    }

    public String getFaName() {
        return faName;
    }

    public void setFaName(String faName) {
        this.faName = faName == null ? null : faName.trim();
    }

    public Integer getFaSystemId() {
        return faSystemId;
    }

    public void setFaSystemId(Integer faSystemId) {
        this.faSystemId = faSystemId;
    }

    public Integer getFaTypeId() {
        return faTypeId;
    }

    public void setFaTypeId(Integer faTypeId) {
        this.faTypeId = faTypeId;
    }

    public Integer getFaNumber() {
        return faNumber;
    }

    public void setFaNumber(Integer faNumber) {
        this.faNumber = faNumber;
    }

    public Date getUseDate() {
        return useDate;
    }

    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName == null ? null : unitName.trim();
    }

    public String getUnitPhone() {
        return unitPhone;
    }

    public void setUnitPhone(String unitPhone) {
        this.unitPhone = unitPhone == null ? null : unitPhone.trim();
    }

    public String getMaintainName() {
        return maintainName;
    }

    public void setMaintainName(String maintainName) {
        this.maintainName = maintainName == null ? null : maintainName.trim();
    }

    public String getMaintainPhone() {
        return maintainPhone;
    }

    public void setMaintainPhone(String maintainPhone) {
        this.maintainPhone = maintainPhone == null ? null : maintainPhone.trim();
    }

    public Integer getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(Integer serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public Date getUpstatusTime() {
        return upstatusTime;
    }

    public void setUpstatusTime(Date upstatusTime) {
        this.upstatusTime = upstatusTime;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName == null ? null : cameraName.trim();
    }

    public String getCameraAddress() {
        return cameraAddress;
    }

    public void setCameraAddress(String cameraAddress) {
        this.cameraAddress = cameraAddress == null ? null : cameraAddress.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}