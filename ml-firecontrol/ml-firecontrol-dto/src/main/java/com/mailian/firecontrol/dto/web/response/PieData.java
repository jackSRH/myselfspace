package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/5
 * @Description:
 */
@ApiModel(description = "饼图数据对象")
public class PieData {
    @ApiModelProperty(value = "数据值")
    private Integer value;
    @ApiModelProperty(value = "数据名称")
    private String name;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
