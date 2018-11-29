package com.mailian.firecontrol.api.web.controller.system;

import com.fasterxml.jackson.annotation.JsonView;
import com.mailian.core.annotation.CurUser;
import com.mailian.core.annotation.Log;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.manager.ValidationManager;
import com.mailian.core.manager.ViewManager;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.manager.SystemManager;
import com.mailian.firecontrol.dao.auto.model.User;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.dto.web.request.UserReq;
import com.mailian.firecontrol.dto.web.response.UserInfo;
import com.mailian.firecontrol.framework.util.ShiroUtils;
import com.mailian.firecontrol.service.RoleService;
import com.mailian.firecontrol.service.UserRoleService;
import com.mailian.firecontrol.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/27
 * @Description:
 */
@RestController
@RequestMapping("/system/user")
@Api(description = "用户相关接口")
@WebAPI
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;


    @Log(title = "系统",action = "获取用户列表")
    @ApiOperation(value = "获取用户列表", httpMethod = "POST")
    @ApiImplicitParam(name = "userQueryReq", value = "用户查询参数", required = false, dataType = "UserReq")
    @RequestMapping(value="/getList",method = RequestMethod.POST)
    @JsonView(value = ViewManager.WebSimpleView.class)
    public ResponseResult<PageBean<UserInfo>> getList(UserReq userQueryReq){
        return ResponseResult.buildOkResult(userService.selectUsersByPage(userQueryReq));
    }


    @Log(title = "系统",action = "新增修改用户")
    @ApiOperation(value = "新增修改用户", httpMethod = "POST")
    @ApiImplicitParam(name = "user", value = "新增修改用户参数", required = true, dataType = "UserReq")
    @RequestMapping(value="/insertOrUpdateUser",method = RequestMethod.POST)
    public ResponseResult insertOrUpdateUser(@RequestBody @Validated(ValidationManager.AddValidation.class) UserReq user){
        ResponseResult result = userService.insertOrUpdateUser(user);

        ShiroUtils.clearCachedAuthorizationInfo();
        return result;
    }


    @Log(title = "系统",action = "用户详情")
    @ApiOperation(value = "用户详情", httpMethod = "GET",notes = "根据用户id获取用户详细信息")
    @RequestMapping(value="/userDetail/{uid}",method = RequestMethod.GET)
    public ResponseResult<UserInfo> userDetail(@ApiParam(name="uid",value = "用户id",required = true) @PathVariable("uid") Integer uid){
        User user = userService.selectByPrimaryKey(uid);
        if(StringUtils.isNull(user)){
            return error("用户id不存在");
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(user,userInfo);

        //获取拥有的角色id
        List<Integer> roleIds = userRoleService.selectRoleIdsByUid(uid);
        userInfo.setRoleIds(roleIds);

        List<Integer> precinctIds = userService.getPrecinctIds(uid);
        userInfo.setPrecinctIds(precinctIds);
        return ResponseResult.buildOkResult(userInfo);
    }

    @Log(title = "系统",action = "修改密码")
    @ApiOperation(value = "修改密码", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "uid", value = "用户id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value="/changePassword",method = RequestMethod.POST)
    public ResponseResult changePassword(@CurUser ShiroUser shiroUser, Integer uid, String password){
        if(StringUtils.isNull(uid)){
            return error("用户id不能为空");
        }

        if(StringUtils.isNull(password)){
            return error("密码不能为空");
        }

        User userDb = userService.selectByPrimaryKey(uid);
        if(StringUtils.isNull(userDb)){
            return error("用户不存在");
        }

        Set<String> roleSet = roleService.selectRoleNamesByUserId(uid);
        if(!shiroUser.getId().equals(uid) && SystemManager.isAdminRole(roleSet)){
            return error("不允许修改超级管理员用户");
        }

        int result = userService.changePassword(uid,userDb.getUserName(),password);

        ShiroUtils.clearCachedAuthorizationInfo();
        return result>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }


    @Log(title = "系统",action = "删除用户")
    @ApiOperation(value = "删除用户", httpMethod = "POST")
    @RequestMapping(value="/delete/{uid}",method = RequestMethod.POST)
    public ResponseResult delete(@CurUser ShiroUser shiroUser,@ApiParam(name="uid",value = "用户id",required = true) @PathVariable("uid") Integer uid){
        User userDb = userService.selectByPrimaryKey(uid);
        if(StringUtils.isNull(userDb)){
            return error("用户不存在");
        }

        Set<String> roleSet = roleService.selectRoleNamesByUserId(uid);
        if(!shiroUser.getId().equals(uid) && SystemManager.isAdminRole(roleSet)){
            return error("不允许修改超级管理员用户");
        }

        int result = userService.deleteByPrimaryKey(uid);
        ShiroUtils.clearCachedAuthorizationInfo();
        return result>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

}
