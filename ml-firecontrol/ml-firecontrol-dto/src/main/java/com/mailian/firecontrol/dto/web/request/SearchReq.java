package com.mailian.firecontrol.dto.web.request;

import com.mailian.core.bean.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(description = "查找参数")
public class SearchReq extends BasePage {
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "单位id")
    private Integer unitId;
    @ApiModelProperty(value = "巡查状态")
    private Integer patrolStatus;
    @ApiModelProperty(value = "开始时间")
    private Date StartDate;
    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getPatrolStatus() {
        return patrolStatus;
    }

    public void setPatrolStatus(Integer patrolStatus) {
        this.patrolStatus = patrolStatus;
    }
}
