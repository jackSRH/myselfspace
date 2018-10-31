package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "摄像头信息参数")
public class CameraListResp {
    @ApiModelProperty(value = "摄像头id")
    private Integer id;
    @ApiModelProperty(value = "单位id")
    private Integer unitId;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "监控点名称")
    private String monitorName;
    @ApiModelProperty(value = "appKey")
    private String appKey;
    @ApiModelProperty(value = "appSecret")
    private String appSecret;
    @ApiModelProperty(value = "视频名称")
    private String cameraName;
    @ApiModelProperty(value = "设备序列号")
    private String deviceSerial;
    @ApiModelProperty(value = "通道号")
    private Integer channelNo;
    @ApiModelProperty(value = "直播链接")
    private String cameraUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public Integer getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(Integer channelNo) {
        this.channelNo = channelNo;
    }

    public String getCameraUrl() {
        return cameraUrl;
    }

    public void setCameraUrl(String cameraUrl) {
        this.cameraUrl = cameraUrl;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }


    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }
}
