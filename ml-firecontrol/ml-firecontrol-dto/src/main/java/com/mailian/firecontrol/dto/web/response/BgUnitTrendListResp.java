package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "大屏单位走势列表")
public class BgUnitTrendListResp {
    @ApiModelProperty(value = "类型")
    private String btypeDesc;
    @ApiModelProperty(value = "数值")
    private List<BgUnitTrendResp> bgUnitTrends;

    public String getBtypeDesc() {
        return btypeDesc;
    }

    public void setBtypeDesc(String btypeDesc) {
        this.btypeDesc = btypeDesc;
    }

    public List<BgUnitTrendResp> getBgUnitTrends() {
        return bgUnitTrends;
    }

    public void setBgUnitTrends(List<BgUnitTrendResp> bgUnitTrends) {
        this.bgUnitTrends = bgUnitTrends;
    }
}
