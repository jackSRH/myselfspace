package com.mailian.firecontrol.dto.web.response;

import com.mailian.firecontrol.dto.web.UnitInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/13
 * @Description:
 */
@ApiModel(description = "单位信息")
public class UnitMapResp {
    @ApiModelProperty(value = "警情总数")
    private Integer allCount;
    @ApiModelProperty(value = "当前告警")
    private Integer alarmCount;
    @ApiModelProperty(value = "当前预警")
    private Integer earlyWarningCount;
    @ApiModelProperty(value = "巡查次数")
    private Integer patrol;
    @ApiModelProperty(value = "单位信息")
    private UnitInfo unitInfo;

    @ApiModelProperty(value = "摄像头参数")
    private List<CameraListResp> cameraListResps;

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
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

    public Integer getPatrol() {
        return patrol;
    }

    public void setPatrol(Integer patrol) {
        this.patrol = patrol;
    }

    public UnitInfo getUnitInfo() {
        return unitInfo;
    }

    public void setUnitInfo(UnitInfo unitInfo) {
        this.unitInfo = unitInfo;
    }

    public List<CameraListResp> getCameraListResps() {
        return cameraListResps;
    }

    public void setCameraListResps(List<CameraListResp> cameraListResps) {
        this.cameraListResps = cameraListResps;
    }
}
