package com.mailian.firecontrol.dto.web.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/24
 * @Description:
 */
@ApiModel(description = "火灾信息管理")
public class FireAlarmReq {
    @ApiModelProperty(value = "告警id")
    private Integer id;
    @ApiModelProperty(value = "起火原因")
    private String alarmReason;
    @ApiModelProperty(value = "起火面积")
    private String alarmArea;
    @ApiModelProperty(value = "死亡人数")
    private String dieNum;
    @ApiModelProperty(value = "受伤人数")
    private String injured;
    @ApiModelProperty(value = "财产损失")
    private String propertyLoss;
    @ApiModelProperty(value = "补救概况")
    private String emedialMeasures;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlarmReason() {
        return alarmReason;
    }

    public void setAlarmReason(String alarmReason) {
        this.alarmReason = alarmReason;
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

    public String getEmedialMeasures() {
        return emedialMeasures;
    }

    public void setEmedialMeasures(String emedialMeasures) {
        this.emedialMeasures = emedialMeasures;
    }
}
