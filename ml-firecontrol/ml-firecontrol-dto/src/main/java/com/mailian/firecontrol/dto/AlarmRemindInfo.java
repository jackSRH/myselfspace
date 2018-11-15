package com.mailian.firecontrol.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/15
 * @Description:
 */
@ApiModel(description = "报警提醒")
public class AlarmRemindInfo implements Serializable {
    private static final long serialVersionUID = 2216515824197683602L;
    @ApiModelProperty(value = "报警id")
    private Integer alarmId;
    @ApiModelProperty(value = "报警时间")
    private Date alarmTime;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "报警内容")
    private String alarmContent;
    @ApiModelProperty(value = "报警位置")
    private String address;
    @ApiModelProperty(value = "责任人")
    private String dutyName;
    @ApiModelProperty(value = "责任人电话")
    private String dutyPhone;
    @ApiModelProperty(value = "单位id")
    private Integer unitId;
    @ApiModelProperty(value = "区域id")
    private Integer areaId;
    @ApiModelProperty(value = "省份id")
    private Integer provinceId;
    @ApiModelProperty(value = "城市id")
    private Integer cityId;
    @ApiModelProperty(value = "管辖区id")
    private Integer precinctId;
    @ApiModelProperty(value = "经度")
    private BigDecimal lng;
    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    public Integer getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
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
}
