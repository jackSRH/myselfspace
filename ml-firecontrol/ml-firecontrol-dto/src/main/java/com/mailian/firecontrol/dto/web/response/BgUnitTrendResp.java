package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "大屏单位走势")
public class BgUnitTrendResp {
    @ApiModelProperty(value = "日期")
    private String date;
    @ApiModelProperty(value = "数值")
    private Double num;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }
}
