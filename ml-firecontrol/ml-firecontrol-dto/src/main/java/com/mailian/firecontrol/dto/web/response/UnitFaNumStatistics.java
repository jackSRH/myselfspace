package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "单位设施数量统计")
public class UnitFaNumStatistics {
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "管辖区名称")
    private String precinctName;
    @ApiModelProperty(value = "设施数量")
    private Integer faNum;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getPrecinctName() {
        return precinctName;
    }

    public void setPrecinctName(String precinctName) {
        this.precinctName = precinctName;
    }

    public Integer getFaNum() {
        return faNum;
    }

    public void setFaNum(Integer faNum) {
        this.faNum = faNum;
    }
}
