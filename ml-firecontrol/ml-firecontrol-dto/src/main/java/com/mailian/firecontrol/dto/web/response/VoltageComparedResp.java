package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@ApiModel(description = "电压对比信息")
public class VoltageComparedResp {
    @ApiModelProperty(value = "最后更新时间")
    private Date lastUpdateTime;
    @ApiModelProperty(value = "每条数据项信息")
    private List<VoltageItemComparedResp> voltageItemComparedResps;

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public List<VoltageItemComparedResp> getVoltageItemComparedResps() {
        return voltageItemComparedResps;
    }

    public void setVoltageItemComparedResps(List<VoltageItemComparedResp> voltageItemComparedResps) {
        this.voltageItemComparedResps = voltageItemComparedResps;
    }
}
