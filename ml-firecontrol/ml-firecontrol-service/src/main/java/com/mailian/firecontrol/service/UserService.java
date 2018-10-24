package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.firecontrol.dao.auto.model.User;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/24
 * @Description:
 */
public interface UserService extends BaseService<User> {

    /**
     * 根据用户名称获取用户
     * @param userName
     * @return
     */
    List<User> getUsersByName(String userName);
}
