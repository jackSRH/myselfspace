package com.mailian.firecontrol.dto.web.response;

import com.mailian.firecontrol.dto.app.response.ItemDataResp;
import com.mailian.firecontrol.dto.app.response.SwitchResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/13
 * @Description:
 */
@ApiModel(description = "单位实时数据")
public class UnitRealtimeDataResp {
    @ApiModelProperty(value = "电压项")
    private List<ItemDataResp> voltages;
    @ApiModelProperty(value = "电流项")
    private List<ItemDataResp> electriccurrents;
    @ApiModelProperty(value = "线缆温度")
    private List<ItemDataResp> cableTemperatures;
    @ApiModelProperty(value = "漏电电流")
    private List<ItemDataResp> leakages;

    @ApiModelProperty(value = "烟感状态 0:正常 1:报警")
    private Integer somkeStatus;

    @ApiModelProperty(value = "开关状态集合")
    private List<SwitchResp> switchResps;

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

    public Integer getSomkeStatus() {
        return somkeStatus;
    }

    public void setSomkeStatus(Integer somkeStatus) {
        this.somkeStatus = somkeStatus;
    }

    public List<SwitchResp> getSwitchResps() {
        return switchResps;
    }

    public void setSwitchResps(List<SwitchResp> switchResps) {
        this.switchResps = switchResps;
    }
}
