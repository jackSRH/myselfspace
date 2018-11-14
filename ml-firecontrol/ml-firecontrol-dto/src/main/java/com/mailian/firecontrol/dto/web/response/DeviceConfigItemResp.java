package com.mailian.firecontrol.dto.web.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "配置数据项信息")
public class DeviceConfigItemResp {
    @ApiModelProperty(value = "数据项id")
    private String id;
    @ApiModelProperty(value = "简称")
    private String shortname;
    @ApiModelProperty(value = "英文简称")
    private String displayname;

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

}
