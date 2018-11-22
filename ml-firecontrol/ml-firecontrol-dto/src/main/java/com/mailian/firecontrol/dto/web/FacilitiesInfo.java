package com.mailian.firecontrol.dto.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mailian.firecontrol.dto.web.response.DiagramStructResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@ApiModel(description = "单位信息参数")
public class FacilitiesInfo {
    @ApiModelProperty(value = "设施id")
    private Integer id;
    @ApiModelProperty(value = "单位id")
    private Integer unitId;
    @ApiModelProperty(value = "设施名称")
    private String faName;
    @ApiModelProperty(value = "设施系统类型 1:火灾自动报警系统 2:智慧用电检测系统")
    private Integer faSystemId;
    @ApiModelProperty(value = "设施型号")
    private Integer faTypeId;
    @ApiModelProperty(value = "设施数量")
    private Integer faNumber;
    @ApiModelProperty(value = "投入使用时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date useDate;
    @ApiModelProperty(value = "生产单位名称")
    private String unitName;
    @ApiModelProperty(value = "生产单位电话")
    private String unitPhone;
    @ApiModelProperty(value = "维修保养单位名称")
    private String maintainName;
    @ApiModelProperty(value = "维修保养电话")
    private String maintainPhone;
    @ApiModelProperty(value = "绑定摄像头名称")
    private String cameraName;
    @ApiModelProperty(value = "摄像头拍摄位置")
    private String cameraAddress;
    @ApiModelProperty(value = "监控设施")
    private List<DiagramStructResp> diagramStructs;
    @ApiModelProperty(value = "服务状态 0:有效,1:无效")
    private Integer serviceStatus;
    @ApiModelProperty(value = "设施型号描述")
    private String faTypeDesc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getFaName() {
        return faName;
    }

    public void setFaName(String faName) {
        this.faName = faName;
    }

    public Integer getFaSystemId() {
        return faSystemId;
    }

    public void setFaSystemId(Integer faSystemId) {
        this.faSystemId = faSystemId;
    }

    public Integer getFaTypeId() {
        return faTypeId;
    }

    public void setFaTypeId(Integer faTypeId) {
        this.faTypeId = faTypeId;
    }

    public Integer getFaNumber() {
        return faNumber;
    }

    public void setFaNumber(Integer faNumber) {
        this.faNumber = faNumber;
    }

    public Date getUseDate() {
        return useDate;
    }

    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitPhone() {
        return unitPhone;
    }

    public void setUnitPhone(String unitPhone) {
        this.unitPhone = unitPhone;
    }

    public String getMaintainName() {
        return maintainName;
    }

    public void setMaintainName(String maintainName) {
        this.maintainName = maintainName;
    }

    public String getMaintainPhone() {
        return maintainPhone;
    }

    public void setMaintainPhone(String maintainPhone) {
        this.maintainPhone = maintainPhone;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getCameraAddress() {
        return cameraAddress;
    }

    public void setCameraAddress(String cameraAddress) {
        this.cameraAddress = cameraAddress;
    }

    public List<DiagramStructResp> getDiagramStructs() {
        return diagramStructs;
    }

    public void setDiagramStructs(List<DiagramStructResp> diagramStructs) {
        this.diagramStructs = diagramStructs;
    }

    public Integer getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(Integer serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getFaTypeDesc() {
        return faTypeDesc;
    }

    public void setFaTypeDesc(String faTypeDesc) {
        this.faTypeDesc = faTypeDesc;
    }
}
