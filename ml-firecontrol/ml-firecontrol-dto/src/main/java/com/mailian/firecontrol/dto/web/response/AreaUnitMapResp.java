package com.mailian.firecontrol.dto.web.response;

import com.mailian.firecontrol.dto.web.UnitInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/6
 * @Description:
 */
@ApiModel(description = "区域单位地图相关信息")
public class AreaUnitMapResp implements Serializable {
    private static final long serialVersionUID = 6810931384642745787L;
    @ApiModelProperty(value = "数量统计信息")
    private CountDataInfo countDataInfo = new CountDataInfo();
    @ApiModelProperty(value = "单位信息")
    private List<UnitInfo> unitInfos;

    public CountDataInfo getCountDataInfo() {
        return countDataInfo;
    }

    public void setCountDataInfo(CountDataInfo countDataInfo) {
        this.countDataInfo = countDataInfo;
    }

    public List<UnitInfo> getUnitInfos() {
        return unitInfos;
    }

    public void setUnitInfos(List<UnitInfo> unitInfos) {
        this.unitInfos = unitInfos;
    }

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
}
