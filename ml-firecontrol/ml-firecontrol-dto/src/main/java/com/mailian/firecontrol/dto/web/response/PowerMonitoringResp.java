package com.mailian.firecontrol.dto.web.response;

import com.mailian.core.bean.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
    @ApiModelProperty(value = "电压")
    private String voltage;
    @ApiModelProperty(value = "电流")
    private String eleCurrent;
    @ApiModelProperty(value = "功率因素")
    private String powerFactor;
    @ApiModelProperty(value = "线缆温度")
    private String cableTemp;
    @ApiModelProperty(value = "漏电电流")
    private String leakageCurrent;

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
        this.voltage = voltage;
    }

    public String getEleCurrent() {
        return eleCurrent;
    }

    public void setEleCurrent(String eleCurrent) {
        this.eleCurrent = eleCurrent;
    }

    public String getPowerFactor() {
        return powerFactor;
    }

    public void setPowerFactor(String powerFactor) {
        this.powerFactor = powerFactor;
    }

    public String getCableTemp() {
        return cableTemp;
    }

    public void setCableTemp(String cableTemp) {
        this.cableTemp = cableTemp;
    }

    public String getLeakageCurrent() {
        return leakageCurrent;
    }

    public void setLeakageCurrent(String leakageCurrent) {
        this.leakageCurrent = leakageCurrent;
    }
}
