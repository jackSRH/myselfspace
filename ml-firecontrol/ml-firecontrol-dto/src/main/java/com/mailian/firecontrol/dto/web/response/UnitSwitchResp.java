package com.mailian.firecontrol.dto.web.response;

import com.mailian.firecontrol.dto.SelectDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "开关信息")
public class UnitSwitchResp {
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "开关名称")
    private String switchName;
    @ApiModelProperty(value = "开关状态")
    private String switchStatus;
    @ApiModelProperty(value = "可变更遥控数据值")
    private List<SelectDto> selects;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    public String getSwitchStatus() {
        return switchStatus;
    }

    public void setSwitchStatus(String switchStatus) {
        this.switchStatus = switchStatus;
    }

    public List<SelectDto> getSelects() {
        return selects;
    }

    public void setSelects(List<SelectDto> selects) {
        this.selects = selects;
    }
}
