package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(description = "火灾告警列表")
public class FireAlarmListResp {
    @ApiModelProperty(value = "告警id")
    private Integer id;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "起火位置")
    private String structAddress;
    @ApiModelProperty(value = "起火时间")
    private Date alarmTime;
    @ApiModelProperty(value = "起火原因")
    private String alarmReason;
    @ApiModelProperty(value = "告警方式")
    private Integer alarmWay;
    @ApiModelProperty(value = "起火面积")
    private String alarmArea;
    @ApiModelProperty(value = "死亡人数")
    private String dieNum;
    @ApiModelProperty(value = "受伤人数")
    private String injured;
    @ApiModelProperty(value = "财产损失")
    private String propertyLoss;

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

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAlarmReason() {
        return alarmReason;
    }

    public void setAlarmReason(String alarmReason) {
        this.alarmReason = alarmReason;
    }

    public Integer getAlarmWay() {
        return alarmWay;
    }

    public void setAlarmWay(Integer alarmWay) {
        this.alarmWay = alarmWay;
    }

    public String getAlarmArea() {
        return alarmArea;
    }

    public void setAlarmArea(String alarmArea) {
        this.alarmArea = alarmArea;
    }

    public String getDieNum() {
        return dieNum;
    }

    public void setDieNum(String dieNum) {
        this.dieNum = dieNum;
    }

    public String getInjured() {
        return injured;
    }

    public void setInjured(String injured) {
        this.injured = injured;
    }

    public String getPropertyLoss() {
        return propertyLoss;
    }

    public void setPropertyLoss(String propertyLoss) {
        this.propertyLoss = propertyLoss;
    }

    public String getStructAddress() {
        return structAddress;
    }

    public void setStructAddress(String structAddress) {
        this.structAddress = structAddress;
    }
}
