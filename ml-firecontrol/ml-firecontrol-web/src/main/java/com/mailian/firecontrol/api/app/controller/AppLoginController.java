package com.mailian.firecontrol.api.app.controller;

import com.mailian.core.annotation.AppAPI;
import com.mailian.core.annotation.Log;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.config.SystemConfig;
import com.mailian.core.shiro.JwtToken;
import com.mailian.core.util.JwtUtils;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.common.manager.SystemManager;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.dto.app.AppUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/3
 * @Description:
 */
@RestController
@RequestMapping("/app/login")
@Api(description = "app登录模块接口")
@AppAPI
public class AppLoginController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private SystemConfig systemConfig;

    @Log(title = "app管理端",action = "app管理端登录")
    @ApiOperation(value = "App管理端登录", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value="/ajaxLogin",method = RequestMethod.POST)
    public ResponseResult<AppUser> ajaxLogin(String userName, String password){
        Subject subject = SecurityUtils.getSubject();
        String token = jwtUtils.getLoginToken(userName,password);
        JwtToken jwtToken = new JwtToken(token);
        subject.login(jwtToken);

        ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();
        AppUser appUser = new AppUser();
        appUser.setToken(jwtToken.getPrincipal().toString());
        appUser.setId(shiroUser.getId());
        appUser.setFullName(shiroUser.getFullName());
        appUser.setManager(SystemManager.isAdminRole(shiroUser.getRoles()));
        appUser.setUniqueKey(String.format(CommonConstant.USRE_KEY,systemConfig.serverIdCard,shiroUser.getId()));
        return ResponseResult.buildOkResult(appUser);
    }


    @Log(title = "app用户端",action = "app用户端登录")
    @ApiOperation(value = "App用户端登录", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value="/ajaxUserLogin",method = RequestMethod.POST)
    public ResponseResult<AppUser> ajaxUserLogin(String userName, String password){
        Subject subject = SecurityUtils.getSubject();
        String token = jwtUtils.getLoginToken(userName,password);
        JwtToken jwtToken = new JwtToken(token);
        subject.login(jwtToken);

        ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();
        AppUser appUser = new AppUser();
        appUser.setToken(jwtToken.getPrincipal().toString());
        appUser.setId(shiroUser.getId());
        appUser.setFullName(shiroUser.getFullName());
        appUser.setUnitId(shiroUser.getUnitId());
        appUser.setManager(SystemManager.isAdminRole(shiroUser.getRoles()));
        appUser.setUniqueKey(String.format(CommonConstant.USRE_KEY,systemConfig.serverIdCard,shiroUser.getId()));
        return ResponseResult.buildOkResult(appUser);
    }
}
