package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "摄像头导航栏")
public class CameraNavigationResp {
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "摄像头列表")
    private List<CameraListResp> cameraLists;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public List<CameraListResp> getCameraLists() {
        return cameraLists;
    }

    public void setCameraLists(List<CameraListResp> cameraLists) {
        this.cameraLists = cameraLists;
    }
}
