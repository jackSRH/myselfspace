package com.mailian.firecontrol.api.web.controller.system;

import com.mailian.core.annotation.Log;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.enums.ResponseCode;
import com.mailian.core.shiro.JwtToken;
import com.mailian.core.util.JwtUtils;
import com.mailian.firecontrol.dto.ShiroUser;
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
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;

@RestController
@RequestMapping("/system/login")
@Api(description = "登录模块接口")
@WebAPI
public class LoginController {
    @Autowired
    private JwtUtils jwtUtils;

    @Log(title = "系统",action = "登录")
    @ApiOperation(value = "登录", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value="/ajaxLogin",method = RequestMethod.POST)
    public ResponseResult<ShiroUser> ajaxLogin(String userName, String password){
        Subject subject = SecurityUtils.getSubject();

        String token = jwtUtils.getLoginToken(userName,password);

        JwtToken jwtToken = new JwtToken(token);
        subject.login(jwtToken);

        ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();
        return ResponseResult.buildOkResult(shiroUser);
    }

    @ApiIgnore(value = "未登录") //生成文档忽略方法
    @RequestMapping(value="/unauth",method = RequestMethod.GET)
    public ResponseResult<String> unauth(){
        return ResponseResult.buildResult(ResponseCode.NO_LOGIN);
    }

}
