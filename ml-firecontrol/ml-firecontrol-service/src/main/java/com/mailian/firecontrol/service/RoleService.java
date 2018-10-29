package com.mailian.firecontrol.service;


import com.mailian.core.base.service.BaseService;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.firecontrol.dao.auto.model.Role;
import com.mailian.firecontrol.dto.web.request.RoleReq;
import com.mailian.firecontrol.dto.web.response.RoleResp;

import java.util.List;
import java.util.Set;

public interface RoleService extends BaseService<Role> {

    List<Role> getAllRole();

    /**
     * 根据 userId 获取对应角色权限字符
     * @param userId
     * @return
     */
    Set<String> selectRoleNamesByUserId(Integer userId);

    /**
     * 根据 条件 查询角色
     * @param releReq
     * @return
     */
    PageBean<RoleResp> queryListByRole(RoleReq releReq);

    /**
     * 根据用户id 获取角色列表
     * @param uid
     * @return
     */
    List<Role> selectRolesByUserId(Integer uid);


    ResponseResult insertOrUpdate(RoleReq releReq);

    /**
     * 根据角色id获取菜单id列表
     * @param roleId
     * @return
     */
    List<Integer> getMenuIdsByRoleId(Integer roleId);
}
