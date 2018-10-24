package com.mailian.core.bean;

import com.fasterxml.jackson.annotation.JsonView;
import com.mailian.core.manager.ViewManager;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体基类
 * @author wangqiaoqing
 *
 */
public class BaseInfo implements Serializable {
    private static final long serialVersionUID = 259362625383652444L;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    @ApiModelProperty(value = "创建人")
    private String createBy;
    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @JsonView(value = ViewManager.SimpleView.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String cleanCode(String value){
        if(value != null){
            value = value.replaceAll("\\s|-|_", "");
        }
        return value;
    }
}
