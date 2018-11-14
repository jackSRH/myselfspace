package com.mailian.firecontrol.dto.web.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "传输遥信数据项信息")
public class YxTranInfo implements Serializable {


    private static final long serialVersionUID = 5621149916525774427L;

    /*数据项id*/
    @ApiModelProperty(value = "数据项id")
    private String id;
    /*中文简称*/
    @ApiModelProperty(value = "中文简称")
    private String shortname;
    /*英文简称*/
    @ApiModelProperty(value = "英文简称")
    private String displayname;
    /*0描述*/
    @ApiModelProperty(value = "0描述")
    private String desc0;
    /*1描述*/
    @ApiModelProperty(value = "1描述")
    private String desc1;
    /*是否告警*/
    @ApiModelProperty(value = "是否告警")
    private Integer palarm;
    /*业务类型*/
    @ApiModelProperty(value = "业务类型")
    private Integer btype;



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

    public String getDesc0() {
        return desc0;
    }

    public void setDesc0(String desc0) {
        this.desc0 = desc0;
    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public Integer getPalarm() {
        return palarm;
    }

    public void setPalarm(Integer palarm) {
        this.palarm = palarm;
    }

    public Integer getBtype() {
        return btype;
    }

    public void setBtype(Integer btype) {
        this.btype = btype;
    }
}
