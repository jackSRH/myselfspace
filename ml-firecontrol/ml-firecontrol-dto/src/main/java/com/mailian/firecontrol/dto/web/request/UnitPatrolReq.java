package com.mailian.firecontrol.dto.web.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(description = "巡查记录请求参数")
public class UnitPatrolReq {
    @ApiModelProperty(value = "巡查结果")
    private Integer patrolResult;
    @ApiModelProperty(value = "发起时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @ApiModelProperty(value = "状态 0:正常,1:异常")
    private Integer status;
    @ApiModelProperty(value = "摄像头id")
    private Integer cameraId;
    @ApiModelProperty(value = "视频截图")
    private String patrolPic;

    public Integer getPatrolResult() {
        return patrolResult;
    }

    public void setPatrolResult(Integer patrolResult) {
        this.patrolResult = patrolResult;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCameraId() {
        return cameraId;
    }

    public void setCameraId(Integer cameraId) {
        this.cameraId = cameraId;
    }

    public String getPatrolPic() {
        return patrolPic;
    }

    public void setPatrolPic(String patrolPic) {
        this.patrolPic = patrolPic;
    }
}
