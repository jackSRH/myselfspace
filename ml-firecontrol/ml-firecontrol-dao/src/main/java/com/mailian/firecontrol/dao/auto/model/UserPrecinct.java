/*
*
* UserPrecinct.java
* Copyright(C) 2018-2099 深圳市脉联电子有限公司
* @date 2018-10-26
*/
package com.mailian.firecontrol.dao.auto.model;

import com.mailian.core.base.model.BaseDomain;
import java.io.Serializable;

public class UserPrecinct extends BaseDomain implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 管辖区id
     */
    private Integer precinctId;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * t_user_precinct
     */
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}