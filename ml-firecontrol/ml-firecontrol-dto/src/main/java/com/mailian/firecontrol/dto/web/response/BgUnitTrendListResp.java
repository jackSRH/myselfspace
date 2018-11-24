package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "大屏单位走势列表")
public class BgUnitTrendListResp {
    @ApiModelProperty(value = "类型 4：电压，5：电流，31：漏电，32：温度")
    private Integer btype;
    @ApiModelProperty(value = "数值")
    private List<BgUnitTrendResp> bgUnitTrends;

    public Integer getBtype() {
        return btype;
    }

    public void setBtype(Integer btype) {
        this.btype = btype;
    }

    public List<BgUnitTrendResp> getBgUnitTrends() {
        return bgUnitTrends;
    }

    public void setBgUnitTrends(List<BgUnitTrendResp> bgUnitTrends) {
        this.bgUnitTrends = bgUnitTrends;
    }
}
