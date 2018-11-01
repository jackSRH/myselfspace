package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(description = "巡查详情")
public class UnitPatrolResp {
    @ApiModelProperty(value = "巡查id")
    private Integer id;
    @ApiModelProperty(value = "单位id")
    private Integer unitId;
    @ApiModelProperty(value = "管辖区id")
    private Integer precinctId;
    @ApiModelProperty(value = "巡查人")
    private Integer uid;
    @ApiModelProperty(value = "巡查结果")
    private Integer patrolResult;
    @ApiModelProperty(value = "发起时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @ApiModelProperty(value = "查岗单位")
    private String precinctName;
    @ApiModelProperty(value = "视频截图")
    private String patrolPic;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getPrecinctId() {
        return precinctId;
    }

    public void setPrecinctId(Integer precinctId) {
        this.precinctId = precinctId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

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

    public String getPrecinctName() {
        return precinctName;
    }

    public void setPrecinctName(String precinctName) {
        this.precinctName = precinctName;
    }

    public String getPatrolPic() {
        return patrolPic;
    }

    public void setPatrolPic(String patrolPic) {
        this.patrolPic = patrolPic;
    }
}
