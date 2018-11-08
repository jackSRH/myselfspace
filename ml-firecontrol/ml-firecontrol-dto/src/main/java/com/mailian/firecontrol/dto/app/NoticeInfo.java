package com.mailian.firecontrol.dto.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/7
 * @Description:
 */
@ApiModel(description = "通知信息")
public class NoticeInfo implements Serializable {
    private static final long serialVersionUID = 1766123247029568357L;
    @ApiModelProperty(value = "通知id")
    private Integer id;
    @ApiModelProperty(value = "通知标题")
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty(value = "通知时间")
    private Date time;
    @ApiModelProperty(value = "通知内容")
    private String content;
    @ApiModelProperty(value = "通知状态 0:待处理 1:已处理")
    private Integer status;
    @ApiModelProperty(value = "通知业务类型 0:告警")
    private Integer type;
    @ApiModelProperty(value = "责任人",hidden = true)
    private Integer uid;
    @ApiModelProperty(value = "管辖区id",hidden = true)
    private Integer precinctId;
    @ApiModelProperty(value = "单位id",hidden = true)
    private Integer unitId;
    @ApiModelProperty(value = "是否已读 0:未读 1:已读")
    private Integer read;
    @ApiModelProperty(value = "具体类型 0:告警通知(告警id)",hidden = true)
    private Integer realType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getPrecinctId() {
        return precinctId;
    }

    public void setPrecinctId(Integer precinctId) {
        this.precinctId = precinctId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getRead() {
        return read;
    }

    public void setRead(Integer read) {
        this.read = read;
    }

    public Integer getRealType() {
        return realType;
    }

    public void setRealType(Integer realType) {
        this.realType = realType;
    }
}
