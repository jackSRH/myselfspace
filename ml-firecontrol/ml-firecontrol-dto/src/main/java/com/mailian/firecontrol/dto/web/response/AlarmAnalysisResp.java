package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/8
 * @Description:
 */
@ApiModel(description = "警情分析信息")
public class AlarmAnalysisResp implements Serializable {
    private static final long serialVersionUID = -2485901485833229391L;

    @ApiModelProperty(value = "告警相关信息")
    private NumInfo alarmNum = new NumInfo();
    @ApiModelProperty(value = "预警相关信息")
    private NumInfo earlyWarningNum = new NumInfo();

    public NumInfo getAlarmNum() {
        return alarmNum;
    }

    public void setAlarmNum(NumInfo alarmNum) {
        this.alarmNum = alarmNum;
    }

    public NumInfo getEarlyWarningNum() {
        return earlyWarningNum;
    }

    public void setEarlyWarningNum(NumInfo earlyWarningNum) {
        this.earlyWarningNum = earlyWarningNum;
    }

    @ApiModel(description = "数量信息")
    public class NumInfo {
        @ApiModelProperty(value = "今日数量")
        private Integer tAn = 0;
        @ApiModelProperty(value = "昨日数量")
        private Integer yAn = 0;
        @ApiModelProperty(value = "环比")
        private Integer ringRatio = 0;

        public Integer gettAn() {
            return tAn;
        }

        public void settAn(Integer tAn) {
            this.tAn = tAn;
        }

        public Integer getyAn() {
            return yAn;
        }

        public void setyAn(Integer yAn) {
            this.yAn = yAn;
        }

        public Integer getRingRatio() {
            return ringRatio;
        }

        public void setRingRatio(Integer ringRatio) {
            this.ringRatio = ringRatio;
        }
    }

}
