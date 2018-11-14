package com.mailian.firecontrol.dto.web.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "计算遥测数据项信息")
public class YcCalcInfo implements Serializable {

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
    /*名称*/
    @ApiModelProperty(value = "名称")
    private String name;
    /*单位*/
    @ApiModelProperty(value = "单位")
    private String unit;
    /*是否告警*/
    @ApiModelProperty(value = "是否告警")
    private Integer palarm;
    /*是否存储*/
    @ApiModelProperty(value = "是否存储")
    private Integer store;
    /*脚本*/
    @ApiModelProperty(value = "脚本")
    private String formula;
    /*业务类型*/
    @ApiModelProperty(value = "业务类型")
    private Integer btype;
    /*关联主变量*/
    @ApiModelProperty(value = "关联主变量")
    private String relid;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Integer getBtype() {
        return btype;
    }

    public void setBtype(Integer btype) {
        this.btype = btype;
    }

    public String getRelid() {
        return relid;
    }

    public void setRelid(String relid) {
        this.relid = relid;
    }
}
