package com.mailian.firecontrol.dto.web.response;

import com.mailian.firecontrol.common.util.StringUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "单条电压数据项对比信息")
public class VoltageItemComparedResp {
    @ApiModelProperty(value = "数据项名称")
    private String itemName;
    @ApiModelProperty(value = "数据项简称")
    private String shortName;
    @ApiModelProperty(value = "实时数据")
    private Float rtData;
    @ApiModelProperty(value = "历史数据")
    private List<YtHisDataResp> ytHisDatas;

    public Float getRtData() {
        return rtData;
    }

    public void setRtData(Float rtData) {
        this.rtData = rtData;
    }

    public List<YtHisDataResp> getYtHisDatas() {
        return ytHisDatas;
    }

    public void setYtHisDatas(List<YtHisDataResp> ytHisDatas) {
        this.ytHisDatas = ytHisDatas;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
