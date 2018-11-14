package com.mailian.firecontrol.dto.web.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "传输遥控数据项信息")
public class YkTranInfo implements Serializable {

    private static final long serialVersionUID = -3239296152736255710L;

    /*数据项id*/
    @ApiModelProperty(value = "数据项id")
    private String id;
    /*中文简称*/
    @ApiModelProperty(value = "中文简称")
    private String shortname;
    /*英文简称*/
    @ApiModelProperty(value = "英文简称")
    private String displayname;
    /*描述*/
    @ApiModelProperty(value = "描述")
    private String desc;


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

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
