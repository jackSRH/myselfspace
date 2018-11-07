package com.mailian.firecontrol.dto.app.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/7
 * @Description:
 */
@ApiModel(description = "数据项信息")
public class ItemDataResp implements Serializable {
    private static final long serialVersionUID = 1753263244881964561L;
    @ApiModelProperty(value = "英文简称")
    private String displayname;      //简称
    @ApiModelProperty(value = "数据值")
    private Float val;				 //实时数据
    @ApiModelProperty(value = "单位")
    private String unit;             //单位

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public Float getVal() {
        return val;
    }

    public void setVal(Float val) {
        this.val = val;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
