package com.mailian.firecontrol.dto;

import com.mailian.core.bean.BaseUserInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/23
 * @Description: 用户信息
 */
public class ShiroUser extends BaseUserInfo implements Serializable {
    private static final long serialVersionUID = 3443052449056630354L;

    /*管辖区id*/
    private List<Integer> precinctIds;
    /*所属单位*/
    private Integer unitId;
    /*权限等级*/
    private Integer rank;

    public List<Integer> getPrecinctIds() {
        return precinctIds;
    }

    public void setPrecinctIds(List<Integer> precinctIds) {
        this.precinctIds = precinctIds;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
