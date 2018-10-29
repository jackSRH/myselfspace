/*
*
* DiagramItem.java
* Copyright(C) 2018-2099 深圳市脉联电子有限公司
* @date 2018-10-26
*/
package com.mailian.firecontrol.dao.auto.model;

import com.mailian.core.base.model.BaseDomain;
import java.io.Serializable;

public class DiagramItem extends BaseDomain implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 数据(t_diagram_struct)id
     */
    private Integer dsId;

    /**
     * 数据项id
     */
    private String itemId;

    /**
     * 是否显示 0:否  1:是
     */
    private Integer display;

    /**
     * 数据项类型 1:数据项 2:告警
     */
    private Integer itemType;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

    /**
     * t_diagram_item
     */
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDsId() {
        return dsId;
    }

    public void setDsId(Integer dsId) {
        this.dsId = dsId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId == null ? null : itemId.trim();
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}