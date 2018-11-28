package com.mailian.firecontrol.dto.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiModel(description = "单位信息参数")
public class UnitInfo {
    @ApiModelProperty(value = "单位id")
    private Integer id;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "区域id")
    private Integer areaId;
    @ApiModelProperty(value = "管辖区id")
    private Integer precinctId;
    @ApiModelProperty(value = "单位地址")
    private String address;
    @ApiModelProperty(value = "经营范围")
    private String businessScope;
    @ApiModelProperty(value = "单位类型 1:餐饮、2:购物、3:住宿、4:公共娱乐、5:医疗、6:教学、7:休闲健身、8:生产加工、9:易燃易爆危险品")
    private Integer unitType;
    @ApiModelProperty(value = "监管等级 1:一级、2:二级、3:三级")
    private Integer superviseLevel;
    @ApiModelProperty(value = "经营人")
    private String transactor;
    @ApiModelProperty(value = "经济所有制 1:私营经济、2:个体经济、3:外资经济、4:混合经济、5:国有经济、6:集体经济、7:混合所有制经济")
    private Integer economySystem;
    @ApiModelProperty(value = "身份证号码")
    private String idCard;
    @ApiModelProperty(value = "职工人数")
    private Integer employeeNum;
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
    @ApiModelProperty(value = "接入时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date joinTime;
    @ApiModelProperty(value = "物品存储")
    private String items;
    @ApiModelProperty(value = "单位图片")
    private String unitPic;
    @ApiModelProperty(value = "省份id")
    private Integer provinceId;
    @ApiModelProperty(value = "城市id")
    private Integer cityId;
    @ApiModelProperty(value = "网关id")
    private List<String> deviceIds;
    @ApiModelProperty(value = "经度")
    private BigDecimal lng;
    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;
    @ApiModelProperty(value = "管辖区名称")
    private String precinctName;
    @ApiModelProperty(value = "负责人")
    private String dutyName;
    @ApiModelProperty(value = "负责人电话")
    private String dutyPhone;
    @ApiModelProperty(value = "单位类型 1:餐饮、2:购物、3:住宿、4:公共娱乐、5:医疗、6:教学、7:休闲健身、8:生产加工、9:易燃易爆危险品")
    private String unitTypeDesc;

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
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
        this.unitName = unitName;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
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
        this.address = address;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
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
        this.idCard = idCard;
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
        this.contactPhone = contactPhone;
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
        this.items = items;
    }

    public String getUnitPic() {
        return unitPic;
    }

    public void setUnitPic(String unitPic) {
        this.unitPic = unitPic;
    }

    public String getTransactor() {
        return transactor;
    }

    public void setTransactor(String transactor) {
        this.transactor = transactor;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public String getPrecinctName() {
        return precinctName;
    }

    public void setPrecinctName(String precinctName) {
        this.precinctName = precinctName;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getDutyPhone() {
        return dutyPhone;
    }

    public void setDutyPhone(String dutyPhone) {
        this.dutyPhone = dutyPhone;
    }


    public String getUnitTypeDesc() {
        return unitTypeDesc;
    }

    public void setUnitTypeDesc(String unitTypeDesc) {
        this.unitTypeDesc = unitTypeDesc;
    }

}
