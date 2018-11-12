package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "警情行业占比信息")
public class AlarmIndustryShareResp {
    @ApiModelProperty(value = "单位类型（行业）")
    private Integer unitType;
    @ApiModelProperty(value = "告警数量")
    private Integer alarmNum;

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }

    public Integer getAlarmNum() {
        return alarmNum;
    }

    public void setAlarmNum(Integer alarmNum) {
        this.alarmNum = alarmNum;
    }
}
