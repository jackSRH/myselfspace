package com.mailian.firecontrol.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/7
 * @Description:
 */
@ApiModel(value = "数量统计信息")
public class CountDataInfo{
    @ApiModelProperty(value = "单位总数")
    private Integer unitCount;
    @ApiModelProperty(value = "在线率")
    private Integer onlineRate;
    @ApiModelProperty(value = "告警总数")
    private Integer alarmCount;
    @ApiModelProperty(value = "预警总数")
    private Integer earlyWarningCount;

    public Integer getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(Integer unitCount) {
        this.unitCount = unitCount;
    }

    public Integer getOnlineRate() {
        return onlineRate;
    }

    public void setOnlineRate(Integer onlineRate) {
        this.onlineRate = onlineRate;
    }

    public Integer getAlarmCount() {
        return alarmCount;
    }

    public void setAlarmCount(Integer alarmCount) {
        this.alarmCount = alarmCount;
    }

    public Integer getEarlyWarningCount() {
        return earlyWarningCount;
    }

    public void setEarlyWarningCount(Integer earlyWarningCount) {
        this.earlyWarningCount = earlyWarningCount;
    }
}
