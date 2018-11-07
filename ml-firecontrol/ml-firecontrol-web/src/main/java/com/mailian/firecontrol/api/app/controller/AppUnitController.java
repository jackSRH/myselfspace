package com.mailian.firecontrol.api.app.controller;

import com.mailian.core.annotation.AppAPI;
import com.mailian.core.annotation.CurUser;
import com.mailian.core.annotation.Log;
import com.mailian.core.bean.BasePage;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.manager.SystemManager;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.dto.app.response.AppUnitDetailResp;
import com.mailian.firecontrol.dto.app.response.AppUnitResp;
import com.mailian.firecontrol.service.UnitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/6
 * @Description:
 */
@RestController
@RequestMapping("/app/unit")
@Api(description = "单位相关接口")
@AppAPI
public class AppUnitController {
    @Autowired
    private UnitService unitService;

    @Log(title = "app单位",action = "搜素单位")
    @ApiOperation(value = "搜索单位列表", httpMethod = "GET")
    @GetMapping(value = "/list")
    public ResponseResult<List<AppUnitResp>> list(@CurUser ShiroUser shiroUser,
                                                  @ApiParam(value = "单位名称") @RequestParam(value = "name",required = false) String name,
                                                  @ApiParam(value = "单位名称") BasePage basePage){
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())){
            if(StringUtils.isEmpty(shiroUser.getPrecinctIds())){
                return ResponseResult.buildOkResult();
            }

            dataScope = new DataScope("precinct_id",shiroUser.getPrecinctIds());
        }
        List<AppUnitResp> unitResps = unitService.selectByNameAndPageScope(name,basePage,dataScope);
        return ResponseResult.buildOkResult(unitResps);
    }


    @Log(title = "app单位",action = "单位详情")
    @ApiOperation(value = "单位详情", httpMethod = "GET")
    @GetMapping(value = "/detail")
    public ResponseResult<AppUnitDetailResp> detail(@CurUser ShiroUser shiroUser,
                                                    @ApiParam(value = "单位id") @RequestParam(value = "unitId") Integer unitId){
        return ResponseResult.buildOkResult(unitService.getAppUnitDetailById(unitId));
    }

}
