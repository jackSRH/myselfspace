package com.mailian.firecontrol.dto.app.response;

import com.mailian.firecontrol.dto.SelectDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/7
 * @Description:
 */
@ApiModel(description = "开关信息")
public class SwitchResp implements Serializable {
    private static final long serialVersionUID = -2840205108716032925L;
    @ApiModelProperty(value = "开关名称")
    private String switchName;
    @ApiModelProperty(value = "当前状态")
    private String status;
    @ApiModelProperty(value = "可变更遥控数据值")
    private List<SelectDto> selects;

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SelectDto> getSelects() {
        return selects;
    }

    public void setSelects(List<SelectDto> selects) {
        this.selects = selects;
    }
}
