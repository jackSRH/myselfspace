package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/9
 * @Description:
 */
@ApiModel(description = "火警统计信息")
public class FireAlarmCountResp implements Serializable {
    private static final long serialVersionUID = -9139434085335301444L;
    @ApiModelProperty(value = "误报数量")
    private Integer misreportNum = 0;

    @ApiModelProperty(value = "真实数量")
    private Integer effectiveNum = 0;

    public Integer getMisreportNum() {
        return misreportNum;
    }

    public void setMisreportNum(Integer misreportNum) {
        this.misreportNum = misreportNum;
    }

    public Integer getEffectiveNum() {
        return effectiveNum;
    }

    public void setEffectiveNum(Integer effectiveNum) {
        this.effectiveNum = effectiveNum;
    }
}
