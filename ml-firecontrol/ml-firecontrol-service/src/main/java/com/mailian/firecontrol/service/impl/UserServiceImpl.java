package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.firecontrol.dao.auto.mapper.UserMapper;
import com.mailian.firecontrol.dao.auto.mapper.UserPrecinctMapper;
import com.mailian.firecontrol.dao.auto.model.User;
import com.mailian.firecontrol.dao.auto.model.UserPrecinct;
import com.mailian.firecontrol.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/24
 * @Description:
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User,UserMapper> implements UserService {
    @Resource
    private UserPrecinctMapper userPrecinctMapper;

    @Override
    public List<User> getUsersByName(String userName) {
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("userName",userName);
        return baseMapper.selectByMap(queryMap);
    }

    @Override
    public List<Integer> getPrecinctIds(Integer uid) {
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("uid",uid);
        List<UserPrecinct> userPrecincts = userPrecinctMapper.selectByMap(queryMap);

        Set<Integer> precinctIds = new HashSet<>();
        for (UserPrecinct userPrecinct : userPrecincts) {
            precinctIds.add(userPrecinct.getId());
        }
        return new ArrayList<>(precinctIds);
    }
}
