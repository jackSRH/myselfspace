package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "设施列表信息")
public class FacilitiesListResp {
    @ApiModelProperty(value = "设施id")
    private Integer id;
    @ApiModelProperty(value = "设施型号")
    private Integer faTypeId;
    @ApiModelProperty(value = "设施型号描述")
    private String faTypeDesc;
    @ApiModelProperty(value = "所属单位名称")
    private String unitName;
    @ApiModelProperty(value = "设施数量")
    private Integer faNumber;
    @ApiModelProperty(value = "服务状态 0:有效,1:无效")
    private Integer serviceStatus;
    @ApiModelProperty(value = "服务状态描述")
    private String serviceStatusDesc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFaTypeId() {
        return faTypeId;
    }

    public void setFaTypeId(Integer faTypeId) {
        this.faTypeId = faTypeId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getFaNumber() {
        return faNumber;
    }

    public void setFaNumber(Integer faNumber) {
        this.faNumber = faNumber;
    }

    public Integer getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(Integer serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getServiceStatusDesc() {
        return serviceStatusDesc;
    }

    public void setServiceStatusDesc(String serviceStatusDesc) {
        this.serviceStatusDesc = serviceStatusDesc;
    }

    public String getFaTypeDesc() {
        return faTypeDesc;
    }

    public void setFaTypeDesc(String faTypeDesc) {
        this.faTypeDesc = faTypeDesc;
    }
}
