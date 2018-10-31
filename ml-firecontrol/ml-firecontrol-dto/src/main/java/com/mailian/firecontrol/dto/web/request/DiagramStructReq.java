package com.mailian.firecontrol.dto.web.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "监控模块请求参数")
public class DiagramStructReq {
    @ApiModelProperty(value = "模块id")
    private Integer id;
    @ApiModelProperty(value = "数据名称")
    private String structName;
    @ApiModelProperty(value = "设施id")
    private Integer facilitiesId;
    @ApiModelProperty(value = "数据项id列表")
    private List<DiagramItemReq> diagramItems;
    @ApiModelProperty(value = "数据描述")
    private String structDesc;
    @ApiModelProperty(value = "单位id")
    private Integer unitId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStructName() {
        return structName;
    }

    public void setStructName(String structName) {
        this.structName = structName;
    }

    public Integer getFacilitiesId() {
        return facilitiesId;
    }

    public void setFacilitiesId(Integer facilitiesId) {
        this.facilitiesId = facilitiesId;
    }

    public List<DiagramItemReq> getDiagramItems() {
        return diagramItems;
    }

    public void setDiagramItems(List<DiagramItemReq> diagramItems) {
        this.diagramItems = diagramItems;
    }

    public String getStructDesc() {
        return structDesc;
    }

    public void setStructDesc(String structDesc) {
        this.structDesc = structDesc;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }
}
