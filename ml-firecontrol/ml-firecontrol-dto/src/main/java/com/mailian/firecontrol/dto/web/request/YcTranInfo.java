package com.mailian.firecontrol.dto.web.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "传输遥测数据项信息")
public class YcTranInfo implements Serializable {
    private static final long serialVersionUID = -8932764908386485411L;

    /*数据项id*/
    @ApiModelProperty(value = "数据项id")
    private String id;
    /*中文简称*/
    @ApiModelProperty(value = "中文简称")
    private String shortname;
    /*基值*/
    @ApiModelProperty(value = "基值")
    private Float basic;
    /*是否告警*/
    @ApiModelProperty(value = "是否告警")
    private Integer palarm;
    /*是否存储*/
    @ApiModelProperty(value = "是否存储")
    private Integer store;
    /*业务类型*/
    @ApiModelProperty(value = "业务类型")
    private Integer btype;
    /*CT变比*/
    @ApiModelProperty(value = "CT变比")
    private Float ctratio;
    /*英文简称*/
    @ApiModelProperty(value = "英文简称")
    private String displayname;

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

    public Float getBasic() {
        return basic;
    }

    public void setBasic(Float basic) {
        this.basic = basic;
    }

    public Integer getPalarm() {
        return palarm;
    }

    public void setPalarm(Integer palarm) {
        this.palarm = palarm;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public Integer getBtype() {
        return btype;
    }

    public void setBtype(Integer btype) {
        this.btype = btype;
    }

    public Float getCtratio() {
        return ctratio;
    }

    public void setCtratio(Float ctratio) {
        this.ctratio = ctratio;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }
}
