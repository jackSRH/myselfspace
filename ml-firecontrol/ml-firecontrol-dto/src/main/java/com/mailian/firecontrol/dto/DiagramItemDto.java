package com.mailian.firecontrol.dto;

import java.io.Serializable;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/7
 * @Description:
 */
public class DiagramItemDto implements Serializable {
    /**
     * 数据项id
     */
    private String itemId;
    /**
     * 数据项类型 1:数据项 2:告警
     */
    private Integer itemType;

    /**
     * 数据类型 1:遥控数据 2:开关配置 3:设施监测
     */
    private Integer structType;

    /**
     * 数据名称
     */
    private String structName;

    /**
     * 数据(t_diagram_struct)id
     */
    private Integer dsId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
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
        this.structName = structName;
    }

    public Integer getDsId() {
        return dsId;
    }

    public void setDsId(Integer dsId) {
        this.dsId = dsId;
    }
}
