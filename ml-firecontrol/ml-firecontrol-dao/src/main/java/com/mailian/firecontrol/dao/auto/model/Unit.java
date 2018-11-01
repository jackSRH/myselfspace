/*
*
* Unit.java
* Copyright(C) 2018-2099 深圳市脉联电子有限公司
* @date 2018-11-01
*/
package com.mailian.firecontrol.dao.auto.model;

import com.mailian.core.base.model.BaseDomain;
import java.io.Serializable;
import java.util.Date;

public class Unit extends BaseDomain implements Serializable {
    /**
     * ID
     */
    private Integer id;

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
     * 单位地址
     */
    private String address;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 单位类型
     */
    private Integer unitType;

    /**
     * 监管等级：1:一级,2:二级,3:三级
     */
    private Integer superviseLevel;

    /**
     * 经营人
     */
    private String transactor;

    /**
     * 经济所有制 1:私营经济、2:个体经济、3:外资经济、4:混合经济、5:国有经济、6:集体经济、7:混合所有制经济
     */
    private Integer economySystem;

    /**
     * 身份证号码
     */
    private String idCard;

    /**
     * 职工人数
     */
    private Integer employeeNum;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 接入时间
     */
    private Date joinTime;

    /**
     * 物品存储
     */
    private String items;

    /**
     * 单位图片
     */
    private String unitPic;

    /**
     * 物联状态（0在线 1离线）
     */
    private Integer iotStatus;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

    /**
     * t_unit
     */
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName == null ? null : unitName.trim();
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

    public Integer getPrecinctId() {
        return precinctId;
    }

    public void setPrecinctId(Integer precinctId) {
        this.precinctId = precinctId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope == null ? null : businessScope.trim();
    }

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }

    public Integer getSuperviseLevel() {
        return superviseLevel;
    }

    public void setSuperviseLevel(Integer superviseLevel) {
        this.superviseLevel = superviseLevel;
    }

    public String getTransactor() {
        return transactor;
    }

    public void setTransactor(String transactor) {
        this.transactor = transactor == null ? null : transactor.trim();
    }

    public Integer getEconomySystem() {
        return economySystem;
    }

    public void setEconomySystem(Integer economySystem) {
        this.economySystem = economySystem;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    public Integer getEmployeeNum() {
        return employeeNum;
    }

    public void setEmployeeNum(Integer employeeNum) {
        this.employeeNum = employeeNum;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items == null ? null : items.trim();
    }

    public String getUnitPic() {
        return unitPic;
    }

    public void setUnitPic(String unitPic) {
        this.unitPic = unitPic == null ? null : unitPic.trim();
    }

    public Integer getIotStatus() {
        return iotStatus;
    }

    public void setIotStatus(Integer iotStatus) {
        this.iotStatus = iotStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}