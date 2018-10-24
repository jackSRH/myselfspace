package com.mailian.core.base.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体基类
 * @author wangqiaoqing
 *
 */
public class BaseDomain implements Serializable {
    private static final long serialVersionUID = 259362625383652444L;

    /*创建时间*/
    private Date createTime;
    /*修改时间*/
    private Date updateTime;
    /*创建人*/
    private String createBy;
    /*修改人*/
    private String updateBy;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
