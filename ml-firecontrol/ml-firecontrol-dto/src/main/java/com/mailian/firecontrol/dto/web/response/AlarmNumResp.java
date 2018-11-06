package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/6
 * @Description:
 */
@ApiModel(description = "警情信息")
public class AlarmNumResp implements Serializable {
    private static final long serialVersionUID = -319232459710029496L;
    @ApiModelProperty(value = "告警数量")
    private AlarmNum alarmNum = new AlarmNum();
    @ApiModelProperty(value = "预警数量")
    private AlarmNum earlyWarningNum = new AlarmNum();

    public AlarmNum getAlarmNum() {
        return alarmNum;
    }

    public void setAlarmNum(AlarmNum alarmNum) {
        this.alarmNum = alarmNum;
    }

    public AlarmNum getEarlyWarningNum() {
        return earlyWarningNum;
    }

    public void setEarlyWarningNum(AlarmNum earlyWarningNum) {
        this.earlyWarningNum = earlyWarningNum;
    }

    @ApiModel(description = "数量信息")
    public class AlarmNum{
        @ApiModelProperty(value = "未响应")
        private Integer untreated=0;
        @ApiModelProperty(value = "处理中")
        private Integer underWay=0;
        @ApiModelProperty(value = "已处理")
        private Integer completed=0;

        public Integer getUntreated() {
            return untreated;
        }

        public void setUntreated(Integer untreated) {
            this.untreated = untreated;
        }

        public Integer getUnderWay() {
            return underWay;
        }

        public void setUnderWay(Integer underWay) {
            this.underWay = underWay;
        }

        public Integer getCompleted() {
            return completed;
        }

        public void setCompleted(Integer completed) {
            this.completed = completed;
        }
    }
}
