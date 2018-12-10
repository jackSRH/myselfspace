package com.mailian.firecontrol.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.db.DataScope;
import com.mailian.core.enums.Status;
import com.mailian.core.util.JwtUtils;
import com.mailian.core.util.MD5Util;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.dao.auto.mapper.UserMapper;
import com.mailian.firecontrol.dao.auto.model.*;
import com.mailian.firecontrol.dao.manual.mapper.SystemManualMapper;
import com.mailian.firecontrol.dao.manual.model.UserVo;
import com.mailian.firecontrol.dto.web.request.UserReq;
import com.mailian.firecontrol.dto.web.response.UserInfo;
import com.mailian.firecontrol.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/24
 * @Description:
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User,UserMapper> implements UserService {
    @Autowired
    private UserPrecinctService userPrecinctService;
    @Autowired
    private UserRoleService userRoleService;
    @Resource
    private SystemManualMapper systemManualMapper;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PrecinctService precinctService;
    @Autowired
    private RoleService roleService;

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
        List<UserPrecinct> userPrecincts = userPrecinctService.selectByMap(queryMap);

        Set<Integer> precinctIds = new HashSet<>();
        for (UserPrecinct userPrecinct : userPrecincts) {
            precinctIds.add(userPrecinct.getPrecinctId());
        }
        return new ArrayList<>(precinctIds);
    }

    @Override
    public PageBean<UserInfo> selectUsersByPage(UserReq userQueryReq,DataScope dataScope) {
        Map<String,Object> queryMap = new HashMap<>();
        if(StringUtils.isNotEmpty(userQueryReq.getUserName())) {
            queryMap.put("userName",userQueryReq.getUserName());
        }
        if(StringUtils.isNotEmpty(userQueryReq.getFullName())) {
            queryMap.put("fullNameLike",userQueryReq.getFullName());
        }
        if(StringUtils.isNotEmpty(userQueryReq.getPhone())){
            queryMap.put("phone",userQueryReq.getPhone());
        }
        if(StringUtils.isNotNull(dataScope)){
            if("precinct_id".equals(dataScope.getScopeName())){
                queryMap.put("precinctIds",dataScope.getDataIds());
            }else{
                queryMap.put("unitId",dataScope.getDataIds().get(0));
            }
        }
        Page page = PageHelper.startPage(userQueryReq.getCurrentPage(),userQueryReq.getPageSize());
        page.setOrderBy(" u.id desc ");
        List<UserVo> userList = systemManualMapper.selectUsersByMap(queryMap);

        List<UserInfo> userInfoList = new ArrayList<>();
        UserInfo userInfo;
        Map<Integer,String> precinctMap = new HashMap<>();

        List<Role> roleList = roleService.getAllRole();
        Map<Integer,Role> roleMap = new HashMap<>();
        for (Role role : roleList) {
            roleMap.put(role.getId(),role);
        }

        for (UserVo user : userList) {
            userInfo = new UserInfo();
            BeanUtils.copyProperties(user,userInfo);
            userInfoList.add(userInfo);

            String precinctIds = user.getPids();
            if(StringUtils.isNotEmpty(precinctIds)){
                String [] pids = precinctIds.split(",");
                List<Integer> pidList = new ArrayList<>();
                for (String precinctId : pids) {
                    Integer pid = Integer.parseInt(precinctId);
                    precinctMap.put(pid,null);
                    pidList.add(pid);
                }
                userInfo.setPrecinctIds(pidList);
            }

            if(StringUtils.isNotEmpty(user.getRids())) {
                userInfo.setRoleNames(new ArrayList<>());
                String [] roleIds = user.getRids().split(",");
                for (String roleId : roleIds) {
                    userInfo.getRoleNames().add(roleMap.get(Integer.parseInt(roleId)).getRoleName());
                }
            }
        }

        if(StringUtils.isNotEmpty(precinctMap)){
            List<Precinct> precincts = precinctService.selectBatchIds(precinctMap.keySet());

            for (Precinct precinct : precincts) {
                precinctMap.put(precinct.getId(),precinct.getPrecinctName());
            }

            for (UserInfo info : userInfoList) {
                info.setPrecinctNames(new ArrayList<>());
                if(StringUtils.isNotEmpty(info.getPrecinctIds())){
                    for (Integer precinctId : info.getPrecinctIds()) {
                        info.getPrecinctNames().add(precinctMap.get(precinctId));
                    }
                }
            }
        }

        return new PageBean<>(userQueryReq.getCurrentPage(),userQueryReq.getPageSize(),(int)page.getTotal(),userInfoList);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult insertOrUpdateUser(UserReq userReq) {
        int result = 0;
        User user = new User();
        BeanUtils.copyProperties(userReq,user);
        if(StringUtils.isEmpty(userReq.getId())){
            user.setStatus(StringUtils.nvl(user.getStatus(),Status.NORMAL.id));
            user.setPassword(MD5Util.md5(user.getPassword()));
            result = insert(user);

            if(result>0){
                Integer uid = user.getId();
                List<Integer> roleIds = userReq.getRoleIds();
                addUserRoles(uid, roleIds);

                List<Integer> precinctIds = userReq.getPrecinctIds();
                addUserPrecincts(uid,precinctIds);
            }
        }else{
            user.setPassword(null);
            if(StringUtils.isEmpty(user.getUnitId())){
                user.setUnitId(-1);
            }
            result = updateByPrimaryKeySelective(user);
            Integer uid = user.getId();
            if(result>0){
                updateUserRole(userReq, uid);

                updateUserPrecinct(userReq,uid);
            }
        }
        return result>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int changePassword(Integer uid, String userName,String password) {
        String upPassword = MD5Util.md5(password);
        User user = new User();
        user.setId(uid);
        user.setPassword(upPassword);
        int result = updateByPrimaryKeySelective(user);
        if(result>0) {
            jwtUtils.generateToken(UUID.randomUUID().toString(), userName, upPassword);
        }
        return result;
    }

    /**
     * 更新用户管辖区关系
     * @param userReq
     * @param uid
     */
    private void updateUserPrecinct(UserReq userReq, Integer uid) {
        List<Integer> precinctIds = userReq.getPrecinctIds();
        Map<String,Object> map = new HashMap<>();
        map.put("uid",uid);
        if(StringUtils.isEmpty(precinctIds)){
            systemManualMapper.deleteUserPrecinct(map);
        }else{
            List<UserPrecinct> userPrecinctList = userPrecinctService.selectByMap(map);

            Integer oldPrecinctId;
            List<Integer> delUserPrecinctIds = new ArrayList<>();
            for (UserPrecinct userPrecinct : userPrecinctList) {
                oldPrecinctId = userPrecinct.getPrecinctId();
                boolean result = precinctIds.remove(oldPrecinctId);
                if(!result){
                    delUserPrecinctIds.add(userPrecinct.getId());
                }
            }
            if(StringUtils.isNotEmpty(delUserPrecinctIds)){
                userPrecinctService.deleteBatchIds(delUserPrecinctIds);
            }
            addUserPrecincts(uid,precinctIds);
        }

    }

    /**
     * 添加用户管辖区关联
     * @param uid
     * @param precinctIds
     */
    private void addUserPrecincts(Integer uid, List<Integer> precinctIds) {
        if(StringUtils.isEmpty(precinctIds)){
            return;
        }
        UserPrecinct userPrecinct;
        List<UserPrecinct> userPrecinctList = new ArrayList<>();
        for (Integer precinctId : precinctIds) {
            userPrecinct = new UserPrecinct();
            userPrecinct.setUid(uid);
            userPrecinct.setPrecinctId(precinctId);
            userPrecinctList.add(userPrecinct);
        }
        userPrecinctService.insertBatch(userPrecinctList);
    }

    /**
     * 更新用户角色关系
     * @param userReq
     * @param uid
     */
    private void updateUserRole(UserReq userReq, Integer uid) {
        List<Integer> roleIds = userReq.getRoleIds();
        Map<String,Object> map = new HashMap<>();
        map.put("userId",uid);
        if(StringUtils.isEmpty(roleIds)){
            systemManualMapper.deleteUserRoleByMap(map);
        }else{
            List<UserRole> userRoleList = userRoleService.selectByMap(map);

            Integer oldRoleId;
            List<Integer> delUserRoleIds = new ArrayList<>();
            for (UserRole userRole : userRoleList) {
                oldRoleId = userRole.getRoleId();
                boolean result = roleIds.remove(oldRoleId);
                if (!result){
                    delUserRoleIds.add(userRole.getId());
                }
            }
            if(StringUtils.isNotEmpty(delUserRoleIds)) {
                userRoleService.deleteBatchIds(delUserRoleIds);
            }
            addUserRoles(uid, roleIds);
        }
    }

    /**
     * 添加用户角色关系
     * @param uid
     * @param roleIds
     */
    private void addUserRoles(Integer uid, List<Integer> roleIds) {
        if(StringUtils.isEmpty(roleIds)){
            return;
        }
        UserRole userRole;
        List<UserRole> userRoleList = new ArrayList<>();
        for (Integer roleId : roleIds) {
            userRole = new UserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(uid);
            userRoleList.add(userRole);
        }
        userRoleService.insertBatch(userRoleList);
    }
}
