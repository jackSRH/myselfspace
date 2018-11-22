package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@ApiModel(description = "负荷对比信息")
public class LoadComparedResp {
    @ApiModelProperty(value = "负荷实时值")
    private Float rtData;
    @ApiModelProperty(value = "最后更新时间")
    private Date lastUpdateTime;
    @ApiModelProperty(value = "历史数据")
    private List<YtHisDataResp> hisDatas;

    public List<YtHisDataResp> getHisDatas() {
        return hisDatas;
    }

    public void setHisDatas(List<YtHisDataResp> hisDatas) {
        this.hisDatas = hisDatas;
    }

    public Float getRtData() {
        return rtData;
    }

    public void setRtData(Float rtData) {
        this.rtData = rtData;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }


}
