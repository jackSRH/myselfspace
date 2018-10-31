package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "单位摄像头列表参数")
public class UnitCameraListResp {
    @ApiModelProperty(value = "单位id")
    private Integer unitId;
    @ApiModelProperty(value = "摄像头列表")
    private List<CameraListResp> cameraLists;

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public List<CameraListResp> getCameraLists() {
        return cameraLists;
    }

    public void setCameraLists(List<CameraListResp> cameraLists) {
        this.cameraLists = cameraLists;
    }
}
