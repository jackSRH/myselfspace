package com.mailian.firecontrol.dto.web.response;

import com.mailian.firecontrol.dto.DateValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "负荷历史数据")
public class YtHisDataResp {
    @ApiModelProperty(value = "时间类型 0昨天，1今天")
    private Integer dateType;
    @ApiModelProperty(value = "数据")
    private List<DateValue> dateValues;

    public Integer getDateType() {
        return dateType;
    }

    public void setDateType(Integer dateType) {
        this.dateType = dateType;
    }

    public List<DateValue> getDateValues() {
        return dateValues;
    }

    public void setDateValues(List<DateValue> dateValues) {
        this.dateValues = dateValues;
    }
}
