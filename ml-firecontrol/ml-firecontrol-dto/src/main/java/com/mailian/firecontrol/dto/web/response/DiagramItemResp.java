package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "模块数据项返回参数")
public class DiagramItemResp {
    @ApiModelProperty(value = "模块id")
    private Integer dsId;
    @ApiModelProperty(value = "数据项id")
    private String itemId;
    @ApiModelProperty(value = "数据项名称")
    private String itemName;
    @ApiModelProperty(value = "是否显示")
    private Integer isDisplay;
    @ApiModelProperty(value = "类型3遥控，其余为2")
    private Integer itemType;

    @ApiModelProperty(value = "简称")
    private String shortname;

    public Integer getDsId() {
        return dsId;
    }

    public void setDsId(Integer dsId) {
        this.dsId = dsId;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

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

    public Integer getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(Integer isDisplay) {
        this.isDisplay = isDisplay;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

}
