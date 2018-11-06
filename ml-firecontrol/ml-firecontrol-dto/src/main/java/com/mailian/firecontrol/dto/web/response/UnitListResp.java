package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "单位列表信息")
public class UnitListResp {
    @ApiModelProperty(value = "单位id")
    private Integer id;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "单位类型")
    private Integer unitType;
    @ApiModelProperty(value = "单位类型描述")
    private String unitTypeDesc;
    @ApiModelProperty(value = "所属区域")
    private String areaInfo;
    @ApiModelProperty(value = "管辖区")
    private String precinct;
    @ApiModelProperty(value = "监管等级")
    private Integer superviseLevel;
    @ApiModelProperty(value = "监管等级描述")
    private String superviseLevelDesc;
    @ApiModelProperty(value = "在线状态 0:离线 1:在线")
    private Integer onlineStatus=0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getPrecinct() {
        return precinct;
    }

    public void setPrecinct(String precinct) {
        this.precinct = precinct;
    }

    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }

    public Integer getSuperviseLevel() {
        return superviseLevel;
    }

    public void setSuperviseLevel(Integer superviseLevel) {
        this.superviseLevel = superviseLevel;
    }

    public String getUnitTypeDesc() {
        return unitTypeDesc;
    }

    public void setUnitTypeDesc(String unitTypeDesc) {
        this.unitTypeDesc = unitTypeDesc;
    }

    public String getSuperviseLevelDesc() {
        return superviseLevelDesc;
    }

    public void setSuperviseLevelDesc(String superviseLevelDesc) {
        this.superviseLevelDesc = superviseLevelDesc;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
}
