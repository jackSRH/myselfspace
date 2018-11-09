package com.mailian.firecontrol.api.app.controller;

import com.mailian.core.annotation.AppAPI;
import com.mailian.core.annotation.CurUser;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.util.MD5Util;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.service.repository.DeviceItemRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/9
 * @Description:
 */
@RestController
@RequestMapping("/common")
@Api(description = "app和web共有api")
@AppAPI
@WebAPI
public class CommonController {
    @Autowired
    private DeviceItemRepository deviceItemRepository;

    @ApiOperation(value = "设置遥控值（开关控制）", httpMethod = "POST")
    @PostMapping(value = "/setYk")
    public ResponseResult setYk(@ApiParam(value = "数据项id") @RequestParam(value = "itemId") String itemId,
                                @ApiParam(value = "遥控值") @RequestParam(value = "val") Integer val) {
        Boolean updateRes = deviceItemRepository.setYk(itemId,val);
        if(!updateRes) {
            return ResponseResult.buildFailResult();
        }
        return ResponseResult.buildOkResult();
    }


    @ApiOperation(value = "校验登录密码", httpMethod = "POST")
    @PostMapping(value = "/checkLoginPassword")
    public ResponseResult checkLoginPassword(@CurUser ShiroUser shiroUser, @ApiParam(value = "密码") @RequestParam(value = "password") String password) {
        String userPassword = shiroUser.getPassword();
        if(MD5Util.md5(password).equals(userPassword)){
            return ResponseResult.buildOkResult();
        }
        return ResponseResult.buildFailResult();
    }
}
