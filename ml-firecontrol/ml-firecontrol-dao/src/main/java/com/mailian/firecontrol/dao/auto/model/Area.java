/*
*
* Area.java
* Copyright(C) 2018-2099 深圳市脉联电子有限公司
* @date 2018-11-05
*/
package com.mailian.firecontrol.dao.auto.model;

import com.mailian.core.base.model.BaseDomain;
import java.io.Serializable;

public class Area extends BaseDomain implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 区域名
     */
    private String areaName;

    /**
     * 区域编码
     */
    private String areaCode;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 级别 0:省,1:市,2:区域
     */
    private Integer areaRank;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

    /**
     * t_sys_area
     */
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? null : areaName.trim();
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode == null ? null : areaCode.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getAreaRank() {
        return areaRank;
    }

    public void setAreaRank(Integer areaRank) {
        this.areaRank = areaRank;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}