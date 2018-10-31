package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "模块数据项返回参数")
public class DiagramItemResp {
    @ApiModelProperty(value = "数据项id")
    private String itemId;
    @ApiModelProperty(value = "数据项名称")
    private String itemName;
    @ApiModelProperty(value = "是否显示")
    private Integer display;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }
}
