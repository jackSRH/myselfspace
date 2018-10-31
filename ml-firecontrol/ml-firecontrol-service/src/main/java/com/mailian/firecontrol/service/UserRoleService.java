package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.firecontrol.dao.auto.model.UserRole;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/31
 * @Description:
 */
public interface UserRoleService extends BaseService<UserRole> {
    /**
     * 通过用户id 获取拥有的角色id
     * @param uid
     * @return
     */
    List<Integer> selectRoleIdsByUid(Integer uid);
}
