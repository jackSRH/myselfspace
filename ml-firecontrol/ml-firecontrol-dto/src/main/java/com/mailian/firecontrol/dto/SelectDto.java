package com.mailian.firecontrol.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/7
 * @Description:
 */
@ApiModel(description = "选择项数据信息")
public class SelectDto implements Serializable {
    private static final long serialVersionUID = -2280764047755187268L;
    @ApiModelProperty(value = "展示名称")
    private String name;
    @ApiModelProperty(value = "对应值")
    private Integer value;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getValue() {
        return value;
    }
    public void setValue(Integer value) {
        this.value = value;
    }
}
