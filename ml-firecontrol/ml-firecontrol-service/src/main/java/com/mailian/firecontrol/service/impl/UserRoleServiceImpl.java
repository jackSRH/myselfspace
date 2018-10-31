package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.firecontrol.dao.auto.mapper.UserRoleMapper;
import com.mailian.firecontrol.dao.auto.model.UserRole;
import com.mailian.firecontrol.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/31
 * @Description:
 */
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole,UserRoleMapper> implements UserRoleService {

    @Override
    public List<Integer> selectRoleIdsByUid(Integer uid) {
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("userId",uid);
        List<UserRole> userRoleList = selectByMap(queryMap);
        List<Integer> roleIds = new ArrayList<>();
        for (UserRole userRole : userRoleList) {
            roleIds.add(userRole.getRoleId());
        }
        return roleIds;
    }
}
