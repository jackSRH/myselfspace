package com.mailian.firecontrol.dto.web.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "模块数据项请求参数")
public class DiagramItemReq {
    @ApiModelProperty(value = "模块id")
    private Integer dsId;
    @ApiModelProperty(value = "数据项id")
    private String itemId;
    @ApiModelProperty(value = "是否显示0:否 1:是，可以不传默认为1")
    private Integer display = 1;
    @ApiModelProperty(value = "数据项类型 2：告警数据 3：遥控数据，可以不传默认为2")
    private Integer itemType = 2;
    @ApiModelProperty(value = "数据项类型")
    private Integer status = 0;

    public Integer getDsId() {
        return dsId;
    }

    public void setDsId(Integer dsId) {
        this.dsId = dsId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
