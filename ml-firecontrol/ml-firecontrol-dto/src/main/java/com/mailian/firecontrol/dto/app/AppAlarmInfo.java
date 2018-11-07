package com.mailian.firecontrol.dto.app;

import com.mailian.firecontrol.dto.web.response.CameraListResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/16
 * @Description:
 */
@ApiModel(description = "告警详情")
public class AppAlarmInfo {
    @ApiModelProperty(value = "告警id")
    private Integer id;
    @ApiModelProperty(value = "纬度")
    private double lng;
    @ApiModelProperty(value = "经度")
    private double lat;
    @ApiModelProperty(value = "告警时间")
    private Date alarmTime;
    @ApiModelProperty(value = "告警结束时间")
    private Date alarmEndTime;
    @ApiModelProperty(value = "告警内容")
    private String alarmContent;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "单位地址")
    private String unitAddress;
    @ApiModelProperty(value = "单位类型 1:餐饮、2:购物、3:住宿、4:公共娱乐、5:医疗、6:教学、7:休闲健身、8:生产加工、9:易燃易爆危险品")
    private Integer unitType;
    @ApiModelProperty(value = "经营人")
    private String transactor;
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
    @ApiModelProperty(value = "管辖区名称")
    private String precinctName;
    @ApiModelProperty(value = "负责人")
    private String dutyName;
    @ApiModelProperty(value = "负责人电话")
    private String dutyPhone;
    @ApiModelProperty(value = "当前状态 0:未处理、1:已响应、2:处理中、3:已处理")
    private Integer handleStatus;
    @ApiModelProperty(value = "摄像头参数")
    private List<CameraListResp> cameraListResps;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Date getAlarmEndTime() {
        return alarmEndTime;
    }

    public void setAlarmEndTime(Date alarmEndTime) {
        this.alarmEndTime = alarmEndTime;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitAddress() {
        return unitAddress;
    }

    public void setUnitAddress(String unitAddress) {
        this.unitAddress = unitAddress;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }

    public String getTransactor() {
        return transactor;
    }

    public void setTransactor(String transactor) {
        this.transactor = transactor;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getPrecinctName() {
        return precinctName;
    }

    public void setPrecinctName(String precinctName) {
        this.precinctName = precinctName;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getDutyPhone() {
        return dutyPhone;
    }

    public void setDutyPhone(String dutyPhone) {
        this.dutyPhone = dutyPhone;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }

    public List<CameraListResp> getCameraListResps() {
        return cameraListResps;
    }

    public void setCameraListResps(List<CameraListResp> cameraListResps) {
        this.cameraListResps = cameraListResps;
    }
}
