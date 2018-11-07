package com.mailian.firecontrol.dto.web.request;

import com.mailian.core.bean.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "告警统计请求参数")
public class AlarmStatisticsReq extends BasePage {
    @ApiModelProperty(value = "告警类型 1：报警，2：预警，不传统计所有")
    private Integer alarmType;
    @ApiModelProperty(value = "时间类型 1：周，2：月")
    private Integer dateType;
    @ApiModelProperty(value = "是否误报 1：是，2：否，不传统计所有")
    private Integer misreport;

    public Integer getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
    }

    public Integer getDateType() {
        return dateType;
    }

    public void setDateType(Integer dateType) {
        this.dateType = dateType;
    }

    public Integer getMisreport() {
        return misreport;
    }

    public void setMisreport(Integer misreport) {
        this.misreport = misreport;
    }
}
