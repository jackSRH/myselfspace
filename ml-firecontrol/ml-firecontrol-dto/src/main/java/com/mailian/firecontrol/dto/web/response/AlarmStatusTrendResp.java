package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "告警状态趋势")
public class AlarmStatusTrendResp {
    @ApiModelProperty(value = "告警类型")
    private String alarmType;
    @ApiModelProperty(value = "时间和值")
    private List<AlarmStatisticsResp> dateValues;

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public List<AlarmStatisticsResp> getDateValues() {
        return dateValues;
    }

    public void setDateValues(List<AlarmStatisticsResp> dateValues) {
        this.dateValues = dateValues;
    }
}
