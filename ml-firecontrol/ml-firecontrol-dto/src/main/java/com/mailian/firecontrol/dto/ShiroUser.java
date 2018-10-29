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

    public List<Integer> getPrecinctIds() {
        return precinctIds;
    }

    public void setPrecinctIds(List<Integer> precinctIds) {
        this.precinctIds = precinctIds;
    }
}
