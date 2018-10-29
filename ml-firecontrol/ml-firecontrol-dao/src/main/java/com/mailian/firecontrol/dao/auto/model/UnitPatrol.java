/*
*
* UnitPatrol.java
* Copyright(C) 2018-2099 深圳市脉联电子有限公司
* @date 2018-10-26
*/
package com.mailian.firecontrol.dao.auto.model;

import com.mailian.core.base.model.BaseDomain;
import java.io.Serializable;
import java.util.Date;

public class UnitPatrol extends BaseDomain implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 单位id
     */
    private Integer unitId;

    /**
     * 管辖区id
     */
    private Integer precinctId;

    /**
     * 巡查人
     */
    private Integer uid;

    /**
     * 巡查结果 (0:正常,1:异常)
     */
    private Integer patrolResult;

    /**
     * 发起时间
     */
    private Date startTime;

    /**
     * 结束
     */
    private Date endTime;

    /**
     * 查岗单位(管辖区名称)
     */
    private String precinctName;

    /**
     * 视频截图
     */
    private String patrolPic;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

    /**
     * t_unit_patrol
     */
    private static final long serialVersionUID = 1L;

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

    public Integer getPrecinctId() {
        return precinctId;
    }

    public void setPrecinctId(Integer precinctId) {
        this.precinctId = precinctId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getPatrolResult() {
        return patrolResult;
    }

    public void setPatrolResult(Integer patrolResult) {
        this.patrolResult = patrolResult;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPrecinctName() {
        return precinctName;
    }

    public void setPrecinctName(String precinctName) {
        this.precinctName = precinctName == null ? null : precinctName.trim();
    }

    public String getPatrolPic() {
        return patrolPic;
    }

    public void setPatrolPic(String patrolPic) {
        this.patrolPic = patrolPic == null ? null : patrolPic.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}