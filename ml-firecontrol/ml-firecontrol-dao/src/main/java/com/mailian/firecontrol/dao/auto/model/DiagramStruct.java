/*
*
* DiagramStruct.java
* Copyright(C) 2018-2099 深圳市脉联电子有限公司
* @date 2018-10-26
*/
package com.mailian.firecontrol.dao.auto.model;

import com.mailian.core.base.model.BaseDomain;
import java.io.Serializable;

public class DiagramStruct extends BaseDomain implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 数据类型 1:遥控数据 2:开关配置 3:设施监测
     */
    private Integer structType;

    /**
     * 数据名称
     */
    private String structName;

    /**
     * 数据描述
     */
    private String structDesc;

    /**
     * 单位id
     */
    private Integer unitId;

    /**
     * 管辖区id
     */
    private Integer precinctId;

    /**
     * 设施id
     */
    private Integer facilitiesId;

    /**
     * 设施系统类型
     */
    private Integer faSystemId;

    /**
     * 地址(编号)
     */
    private String structAddress;

    /**
     * 图片路径
     */
    private String structPic;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

    /**
     * t_diagram_struct
     */
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getStructType() {
        return structType;
    }

    public void setStructType(Integer structType) {
        this.structType = structType;
    }

    public String getStructName() {
        return structName;
    }

    public void setStructName(String structName) {
        this.structName = structName == null ? null : structName.trim();
    }

    public String getStructDesc() {
        return structDesc;
    }

    public void setStructDesc(String structDesc) {
        this.structDesc = structDesc == null ? null : structDesc.trim();
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getPrecinctId() {
        return precinctId;
    }

    public void setPrecinctId(Integer precinctId) {
        this.precinctId = precinctId;
    }

    public Integer getFacilitiesId() {
        return facilitiesId;
    }

    public void setFacilitiesId(Integer facilitiesId) {
        this.facilitiesId = facilitiesId;
    }

    public Integer getFaSystemId() {
        return faSystemId;
    }

    public void setFaSystemId(Integer faSystemId) {
        this.faSystemId = faSystemId;
    }

    public String getStructAddress() {
        return structAddress;
    }

    public void setStructAddress(String structAddress) {
        this.structAddress = structAddress == null ? null : structAddress.trim();
    }

    public String getStructPic() {
        return structPic;
    }

    public void setStructPic(String structPic) {
        this.structPic = structPic == null ? null : structPic.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}