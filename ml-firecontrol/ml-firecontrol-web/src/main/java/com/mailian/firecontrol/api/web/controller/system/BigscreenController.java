package com.mailian.firecontrol.api.web.controller.system;

import com.mailian.core.annotation.CurUser;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.manager.SystemManager;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.dto.web.response.AreaResp;
import com.mailian.firecontrol.dto.web.response.PieResp;
import com.mailian.firecontrol.service.AreaService;
import com.mailian.firecontrol.service.UnitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/5
 * @Description:
 */
@RestController
@RequestMapping("/system/bigscreen")
@Api(description = "大屏相关接口")
@WebAPI
public class BigscreenController {
    @Autowired
    private UnitService unitService;
    @Autowired
    private AreaService areaService;

    @ApiOperation(value = "获取省份城市级别，用于大屏运营商展示", httpMethod = "GET")
    @GetMapping(value = "getProvinceAndCityList")
    public ResponseResult<List<AreaResp>> getProvinceAndCityList(@ApiParam(value="区域名") @RequestParam(value = "areaName",required = false) String areaName){
        return ResponseResult.buildOkResult(areaService.selectProvinceAndCityList(areaName));
    }

    @ApiOperation(value = "获取管辖区数结构，用于大屏运营商展示", httpMethod = "GET")
    @GetMapping(value = "getAreaAndPrecinctList")
    public ResponseResult<List<AreaResp>> getAreaAndPrecinctList(@CurUser ShiroUser shiroUser,
                                                                 @ApiParam(value="区域名") @RequestParam(value = "areaName",required = false) String areaName){
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())){
            List<Integer> precinctIds = shiroUser.getPrecinctIds();
            dataScope = new DataScope(precinctIds);
        }
        return ResponseResult.buildOkResult(areaService.selectAreaAndPrecinctList(areaName,dataScope));
    }


    @ApiOperation(value = "获取单位分布(运营商)", httpMethod = "GET")
    @RequestMapping(value="/getUnitDataByArea",method = RequestMethod.GET)
    public ResponseResult<PieResp> getUnitDataByArea(@ApiParam(value = "区域id") @RequestParam(value = "areaId",required = false) Integer areaId){
        return ResponseResult.buildOkResult(unitService.getUnitSpreadByAreaAndScope(areaId,null));
    }

    @ApiOperation(value = "获取单位分布(管辖区)", httpMethod = "GET")
    @RequestMapping(value="/getUnitDataBy",method = RequestMethod.GET)
    public ResponseResult<PieResp> getUnitDataByArea(@CurUser ShiroUser shiroUser,
                                                     @ApiParam(value = "区域id") @RequestParam(value = "areaId",required = false) Integer areaId,
                                                     @ApiParam(value = "管辖区id") @RequestParam(value = "precinctId",required = false) Integer precinctId){
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())) {
            List<Integer> precinctIds = shiroUser.getPrecinctIds();
            if(StringUtils.isNotEmpty(precinctId)) {
                /*无数据权限*/
                if (!precinctIds.contains(precinctId)) {
                    return ResponseResult.buildOkResult();
                }
            }
            dataScope = new DataScope("precinct_id", precinctIds);
        }

        if(StringUtils.isNotEmpty(precinctId)){
            dataScope = new DataScope("precinct_id", Arrays.asList(precinctId));
        }

        return ResponseResult.buildOkResult(unitService.getUnitSpreadByAreaAndScope(areaId,dataScope));
    }

}
