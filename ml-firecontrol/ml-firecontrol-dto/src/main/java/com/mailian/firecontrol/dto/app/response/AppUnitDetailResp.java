package com.mailian.firecontrol.dto.app.response;

import com.mailian.firecontrol.dto.web.response.CameraListResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/6
 * @Description:
 */
@ApiModel(description = "app单位详情信息")
public class AppUnitDetailResp implements Serializable {
    private static final long serialVersionUID = -5736081963039623444L;

    @ApiModelProperty(value = "单位id")
    private Integer unitId;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "单位类型 1:餐饮、2:购物、3:住宿、4:公共娱乐、5:医疗、6:教学、7:休闲健身、8:生产加工、9:易燃易爆危险品")
    private Integer unitType;
    @ApiModelProperty(value = "经营人")
    private String transactor;
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
    @ApiModelProperty(value = "单位地址")
    private String address;
    @ApiModelProperty(value = "经营范围")
    private String businessScope;
    @ApiModelProperty(value = "物品存储")
    private String items;
    @ApiModelProperty(value = "接入时间")
    private Date joinTime;
    @ApiModelProperty(value = "管辖区名称")
    private String precinctName;
    @ApiModelProperty(value = "负责人")
    private String dutyName;
    @ApiModelProperty(value = "负责人电话")
    private String dutyPhone;

    @ApiModelProperty(value = "开关状态集合")
    private List<SwitchResp> switchResps;

    @ApiModelProperty(value = "烟感状态 0:正常 1:报警")
    private Integer somkeStatus;
    @ApiModelProperty(value = "漏电状态 0:正常 1:预警")
    private Integer leakageStatus;

    @ApiModelProperty(value = "电压项")
    private List<ItemDataResp> voltages;
    @ApiModelProperty(value = "电流项")
    private List<ItemDataResp> electriccurrents;
    @ApiModelProperty(value = "线缆温度")
    private List<ItemDataResp> cableTemperatures;
    @ApiModelProperty(value = "漏电电流")
    private List<ItemDataResp> leakages;

    @ApiModelProperty(value = "摄像头参数")
    private List<CameraListResp> cameraListResps;
    @ApiModelProperty(value = "今日报警")
    private Integer alarmNum;
    @ApiModelProperty(value = "今日预警")
    private Integer earlyWarningNum;

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
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

    public Integer getSomkeStatus() {
        return somkeStatus;
    }

    public void setSomkeStatus(Integer somkeStatus) {
        this.somkeStatus = somkeStatus;
    }

    public Integer getLeakageStatus() {
        return leakageStatus;
    }

    public void setLeakageStatus(Integer leakageStatus) {
        this.leakageStatus = leakageStatus;
    }

    public List<ItemDataResp> getVoltages() {
        return voltages;
    }

    public void setVoltages(List<ItemDataResp> voltages) {
        this.voltages = voltages;
    }

    public List<ItemDataResp> getElectriccurrents() {
        return electriccurrents;
    }

    public void setElectriccurrents(List<ItemDataResp> electriccurrents) {
        this.electriccurrents = electriccurrents;
    }

    public List<ItemDataResp> getCableTemperatures() {
        return cableTemperatures;
    }

    public void setCableTemperatures(List<ItemDataResp> cableTemperatures) {
        this.cableTemperatures = cableTemperatures;
    }

    public List<ItemDataResp> getLeakages() {
        return leakages;
    }

    public void setLeakages(List<ItemDataResp> leakages) {
        this.leakages = leakages;
    }

    public List<SwitchResp> getSwitchResps() {
        return switchResps;
    }

    public void setSwitchResps(List<SwitchResp> switchResps) {
        this.switchResps = switchResps;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }

    public List<CameraListResp> getCameraListResps() {
        return cameraListResps;
    }

    public void setCameraListResps(List<CameraListResp> cameraListResps) {
        this.cameraListResps = cameraListResps;
    }

    public Integer getAlarmNum() {
        return alarmNum;
    }

    public void setAlarmNum(Integer alarmNum) {
        this.alarmNum = alarmNum;
    }

    public Integer getEarlyWarningNum() {
        return earlyWarningNum;
    }

    public void setEarlyWarningNum(Integer earlyWarningNum) {
        this.earlyWarningNum = earlyWarningNum;
    }
}
