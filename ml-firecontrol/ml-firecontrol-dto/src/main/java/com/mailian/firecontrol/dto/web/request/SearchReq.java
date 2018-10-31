package com.mailian.firecontrol.dto.web.request;

import com.mailian.core.bean.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "查找参数")
public class SearchReq extends BasePage {
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "单位id")
    private Integer unitId;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }
}
