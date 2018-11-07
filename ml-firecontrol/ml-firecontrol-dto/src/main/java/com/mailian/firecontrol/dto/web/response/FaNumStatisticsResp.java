package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "设施数量统计")
public class FaNumStatisticsResp {
    @ApiModelProperty(value = "系统类型")
    private Integer faSystemId;
    @ApiModelProperty(value = "设施数量")
    private Integer faNum = 0;

    public Integer getFaSystemId() {
        return faSystemId;
    }

    public void setFaSystemId(Integer faSystemId) {
        this.faSystemId = faSystemId;
    }

    public Integer getFaNum() {
        return faNum;
    }

    public void setFaNum(Integer faNum) {
        this.faNum = faNum;
    }
}
