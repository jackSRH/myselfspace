package com.mailian.firecontrol.dto.app.response;

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

    @ApiModel(description = "数据项信息")
    public class ItemDataResp implements Serializable{
        private static final long serialVersionUID = 1753263244881964561L;
        @ApiModelProperty(value = "英文简称")
        private String displayname;      //简称
        @ApiModelProperty(value = "数据值")
        private Float val;				 //实时数据
        @ApiModelProperty(value = "单位")
        private String unit;             //单位

        public String getDisplayname() {
            return displayname;
        }

        public void setDisplayname(String displayname) {
            this.displayname = displayname;
        }

        public Float getVal() {
            return val;
        }

        public void setVal(Float val) {
            this.val = val;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }
}
