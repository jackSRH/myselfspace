package com.mailian.firecontrol.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "时间点和值")
public class DateValue {
    @ApiModelProperty(value = "时间")
    private String date;
    @ApiModelProperty(value = "值")
    private Float value;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }
}
