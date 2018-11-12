package com.mailian.firecontrol.api.app.controller;

import com.mailian.core.annotation.AppAPI;
import com.mailian.core.annotation.Log;
import com.mailian.core.bean.ResponseResult;
import com.mailian.firecontrol.dto.app.AppInfo;
import com.mailian.firecontrol.service.AppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/app/versionmanage")
@Api(description = "app版本管理接口")
@AppAPI
public class AppVersionManageController {

    @Resource
    private AppService appService;

    @Log(title = "app管理端",action = "管理端获取最新的版本信息")
    @ApiOperation(value = "管理端获取最新的版本信息", httpMethod = "GET")
    @RequestMapping(value = "/getConfigAppVersion", method = RequestMethod.GET)
    public ResponseResult<AppInfo> getConfigAppVersion(){
        return ResponseResult.buildOkResult(appService.selectLatest(0));
    }

    @Log(title = "app用户端",action = "用户端获取最新的版本信息")
    @ApiOperation(value = "用户端获取最新的版本信息", httpMethod = "GET")
    @RequestMapping(value = "/getUserAppVersion", method = RequestMethod.GET)
    public ResponseResult<AppInfo> getUserAppVersion(){
        return ResponseResult.buildOkResult(appService.selectLatest(1));
    }

    @Log(title = "app",action = "app下载")
    @ApiOperation(value = "app下载", httpMethod = "GET")
    @RequestMapping(value = "/downLoadApp", method = RequestMethod.GET)
    public ResponseResult downLoadApp(@ApiParam(value = "下载路径") @RequestParam(value = "filePath")String filePath, HttpServletResponse response) throws Exception {
        return appService.downLoadApp(filePath,response);
    }


}
