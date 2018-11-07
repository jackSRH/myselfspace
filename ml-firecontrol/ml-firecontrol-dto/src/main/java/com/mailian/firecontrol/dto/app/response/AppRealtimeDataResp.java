package com.mailian.firecontrol.dto.app.response;

import com.mailian.firecontrol.dto.CountDataInfo;
import com.mailian.firecontrol.dto.web.response.AlarmNumResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/7
 * @Description:
 */
@ApiModel(description = "首页实时监测数据")
public class AppRealtimeDataResp {
    @ApiModelProperty(value = "数量统计信息")
    private CountDataInfo countDataInfo;
    @ApiModelProperty(value = "告警数据")
    private AlarmNumResp alarmNumResp;

    public CountDataInfo getCountDataInfo() {
        return countDataInfo;
    }

    public void setCountDataInfo(CountDataInfo countDataInfo) {
        this.countDataInfo = countDataInfo;
    }

    public AlarmNumResp getAlarmNumResp() {
        return alarmNumResp;
    }

    public void setAlarmNumResp(AlarmNumResp alarmNumResp) {
        this.alarmNumResp = alarmNumResp;
    }
}
