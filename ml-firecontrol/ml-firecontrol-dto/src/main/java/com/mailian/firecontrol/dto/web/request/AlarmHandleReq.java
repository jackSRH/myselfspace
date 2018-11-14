package com.mailian.firecontrol.dto.web.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(description = "告警处理请求参数")
public class AlarmHandleReq {
    @ApiModelProperty(value = "告警id")
    private Integer id;
    @ApiModelProperty(value = "处理结果")
    private String handleResult;
    @ApiModelProperty(value = "是否误报 1:误报 2:有效 3:其它")
    private Integer misreport;
    @ApiModelProperty(value = "是否已处理 0:未处理  1:已处理")
    private Integer handleStatus = 0;
    @ApiModelProperty(value = "处理时间")
    private Date handleEndTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public Integer getMisreport() {
        return misreport;
    }

    public void setMisreport(Integer misreport) {
        this.misreport = misreport;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }

    public Date getHandleEndTime() {
        return handleEndTime;
    }

    public void setHandleEndTime(Date handleEndTime) {
        this.handleEndTime = handleEndTime;
    }
}
