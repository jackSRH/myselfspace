package com.mailian.firecontrol.dao.manual.model;

import com.mailian.firecontrol.dao.auto.model.User;

import java.io.Serializable;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/12/7
 * @Description:
 */
public class UserVo extends User implements Serializable {
    private static final long serialVersionUID = -6424268807785627309L;

    /**
     * 管辖区ids
     */
    private String pids;
    /**
     * 角色ids
     * */
    private String rids;

    public String getPids() {
        return pids;
    }

    public void setPids(String pids) {
        this.pids = pids;
    }

    public String getRids() {
        return rids;
    }

    public void setRids(String rids) {
        this.rids = rids;
    }
}
