package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/5
 * @Description:
 */
@ApiModel(description = "饼图数据对象")
public class PieResp {
    @ApiModelProperty(value = "数据项集合")
    private List<PieData> pieDataList;
    @ApiModelProperty(value = "总计")
    private Integer total;

    public List<PieData> getPieDataList() {
        return pieDataList;
    }

    public void setPieDataList(List<PieData> pieDataList) {
        this.pieDataList = pieDataList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
