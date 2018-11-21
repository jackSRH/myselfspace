package com.mailian.firecontrol.dto.web.response;

import com.mailian.core.bean.BaseInfo;
import com.mailian.core.util.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApiModel(value = "用电检测参数")
public class PowerMonitoringResp extends BaseInfo {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "单位id")
    private Integer unitId;
    @ApiModelProperty(value = "名称")
    private String pmName;
    @ApiModelProperty(value = "负荷")
    private String totalLoad;
    @ApiModelProperty(value = "负荷数据项id")
    private List<String> loadItemIds = new ArrayList<>();
    @ApiModelProperty(value = "负荷数据项信息")
    private List<DeviceItemResp> loadItemInfos;
    @ApiModelProperty(value = "电压")
    private String voltage;
    @ApiModelProperty(value = "电压数据项id")
    private List<String> voltageItemIds = new ArrayList<>();
    @ApiModelProperty(value = "电压数据项信息")
    private List<DeviceItemResp> voltageItemInfos;
    @ApiModelProperty(value = "电流")
    private String eleCurrent;
    @ApiModelProperty(value = "电流数据项id")
    private List<String> ecItemIds = new ArrayList<>();;
    @ApiModelProperty(value = "电流数据项信息")
    private List<DeviceItemResp> ecItemInfos;
    @ApiModelProperty(value = "功率因素")
    private String powerFactor;
    @ApiModelProperty(value = "功率因素数据项id")
    private List<String> pfItemIds = new ArrayList<>();;
    @ApiModelProperty(value = "功率因素数据项信息")
    private List<DeviceItemResp> pfItemInfos;
    @ApiModelProperty(value = "线缆温度")
    private String cableTemp;
    @ApiModelProperty(value = "线缆温度数据项id")
    private List<String> ctItemIds = new ArrayList<>();;
    @ApiModelProperty(value = "线缆温度数据项信息")
    private List<DeviceItemResp> ctItemInfos;
    @ApiModelProperty(value = "漏电电流")
    private String leakageCurrent;
    @ApiModelProperty(value = "漏电电流数据项id")
    private List<String> lcItemIds = new ArrayList<>();;
    @ApiModelProperty(value = "漏电电流数据项信息")
    private List<DeviceItemResp> lcItemInfos;


    public List<DeviceItemResp> getLoadItemInfos() {
        return loadItemInfos;
    }

    public void setLoadItemInfos(List<DeviceItemResp> loadItemInfos) {
        this.loadItemInfos = loadItemInfos;
    }

    public String getPmName() {
        return pmName;
    }

    public void setPmName(String pmName) {
        this.pmName = pmName;
    }

    public String getTotalLoad() {
        return totalLoad;
    }

    public void setTotalLoad(String totalLoad) {
        if(StringUtils.isNotEmpty(totalLoad)){
            loadItemIds.addAll(Arrays.asList(totalLoad.split(",")));
        }
        this.totalLoad = totalLoad;
    }

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

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        if(StringUtils.isNotEmpty(voltage)){
            voltageItemIds.addAll(Arrays.asList(voltage.split(",")));
        }
        this.voltage = voltage;
    }

    public String getEleCurrent() {
        return eleCurrent;
    }

    public void setEleCurrent(String eleCurrent) {
        if(StringUtils.isNotEmpty(eleCurrent)){
            ecItemIds.addAll(Arrays.asList(eleCurrent.split(",")));
        }
        this.eleCurrent = eleCurrent;
    }

    public String getPowerFactor() {
        return powerFactor;
    }

    public void setPowerFactor(String powerFactor) {
        if(StringUtils.isNotEmpty(powerFactor)){
            pfItemIds.addAll(Arrays.asList(powerFactor.split(",")));
        }
        this.powerFactor = powerFactor;
    }

    public String getCableTemp() {
        return cableTemp;
    }

    public void setCableTemp(String cableTemp) {
        if(StringUtils.isNotEmpty(cableTemp)){
            ctItemIds.addAll(Arrays.asList(cableTemp.split(",")));
        }
        this.cableTemp = cableTemp;
    }

    public String getLeakageCurrent() {
        return leakageCurrent;
    }

    public void setLeakageCurrent(String leakageCurrent) {
        if(StringUtils.isNotEmpty(leakageCurrent)){
            lcItemIds.addAll(Arrays.asList(leakageCurrent.split(",")));
        }
        this.leakageCurrent = leakageCurrent;
    }

    public List<DeviceItemResp> getVoltageItemInfos() {
        return voltageItemInfos;
    }

    public void setVoltageItemInfos(List<DeviceItemResp> voltageItemInfos) {
        this.voltageItemInfos = voltageItemInfos;
    }

    public List<DeviceItemResp> getEcItemInfos() {
        return ecItemInfos;
    }

    public void setEcItemInfos(List<DeviceItemResp> ecItemInfos) {
        this.ecItemInfos = ecItemInfos;
    }

    public List<DeviceItemResp> getPfItemInfos() {
        return pfItemInfos;
    }

    public void setPfItemInfos(List<DeviceItemResp> pfItemInfos) {
        this.pfItemInfos = pfItemInfos;
    }

    public List<DeviceItemResp> getCtItemInfos() {
        return ctItemInfos;
    }

    public void setCtItemInfos(List<DeviceItemResp> ctItemInfos) {
        this.ctItemInfos = ctItemInfos;
    }

    public List<DeviceItemResp> getLcItemInfos() {
        return lcItemInfos;
    }

    public void setLcItemInfos(List<DeviceItemResp> lcItemInfos) {
        this.lcItemInfos = lcItemInfos;
    }

    public List<String> getVoltageItemIds() {
        return voltageItemIds;
    }

    public void setVoltageItemIds(List<String> voltageItemIds) {
        this.voltageItemIds = voltageItemIds;
    }

    public List<String> getEcItemIds() {
        return ecItemIds;
    }

    public List<String> getLoadItemIds() {
        return loadItemIds;
    }

    public void setLoadItemIds(List<String> loadItemIds) {
        this.loadItemIds = loadItemIds;
    }

    public void setEcItemIds(List<String> ecItemIds) {
        this.ecItemIds = ecItemIds;
    }

    public List<String> getPfItemIds() {
        return pfItemIds;
    }

    public void setPfItemIds(List<String> pfItemIds) {
        this.pfItemIds = pfItemIds;
    }

    public List<String> getCtItemIds() {
        return ctItemIds;
    }

    public void setCtItemIds(List<String> ctItemIds) {
        this.ctItemIds = ctItemIds;
    }

    public List<String> getLcItemIds() {
        return lcItemIds;
    }

    public void setLcItemIds(List<String> lcItemIds) {
        this.lcItemIds = lcItemIds;
    }
}
