package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.firecontrol.dao.auto.mapper.UserMapper;
import com.mailian.firecontrol.dao.auto.model.User;
import com.mailian.firecontrol.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/24
 * @Description:
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User,UserMapper> implements UserService {
    @Override
    public List<User> getUsersByName(String userName) {
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("userName",userName);
        return baseMapper.selectByMap(queryMap);
    }
}
