package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "数据项信息")
public class DeviceItemResp {
    @ApiModelProperty(value = "数据项id")
    private String id;
    @ApiModelProperty(value = "数据项名称")
    private String name;
    @ApiModelProperty(value = "数据项简称")
    private String shortname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }
}
