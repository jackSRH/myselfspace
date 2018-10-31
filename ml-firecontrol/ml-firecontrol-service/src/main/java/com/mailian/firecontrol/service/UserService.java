package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.firecontrol.dao.auto.model.User;
import com.mailian.firecontrol.dto.web.request.UserReq;
import com.mailian.firecontrol.dto.web.response.UserInfo;

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

    /**
     * 获取用户的管辖区
     * @param uid
     * @return
     */
    List<Integer> getPrecinctIds(Integer uid);

    /**
     * 根据条件查询
     * @param userQueryReq
     * @return
     */
    PageBean<UserInfo> selectUsersByPage(UserReq userQueryReq);

    /**
     * 新增或修改用户
     * @param userReq
     * @return
     */
    ResponseResult insertOrUpdateUser(UserReq userReq);

    /**
     * 修改用户密码
     * @param uid
     * @param password
     * @return
     */
    int changePassword(Integer uid,String userName,String password);

}
