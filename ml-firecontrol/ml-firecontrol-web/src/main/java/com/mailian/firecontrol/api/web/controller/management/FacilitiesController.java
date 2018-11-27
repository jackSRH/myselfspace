package com.mailian.firecontrol.api.web.controller.management;

import com.mailian.core.annotation.CurUser;
import com.mailian.core.annotation.Log;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.FaSystemType;
import com.mailian.firecontrol.common.manager.SystemManager;
import com.mailian.firecontrol.dao.auto.model.Facilities;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.dto.web.FacilitiesInfo;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.DictDataResp;
import com.mailian.firecontrol.dto.web.response.FacilitiesListResp;
import com.mailian.firecontrol.service.DiagramStructService;
import com.mailian.firecontrol.service.FacilitiesService;
import com.mailian.firecontrol.service.component.DictDataComponent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/management/facilities")
@Api(description = "设施信息相关接口")
@WebAPI
public class FacilitiesController extends BaseController {
    @Resource
    private FacilitiesService facilitiesService;
    @Resource
    private DiagramStructService diagramStructService;
    @Autowired
    private DictDataComponent dictDataComponent;

    @Log(title = "配置管理",action = "新增或者更新设施")
    @ApiOperation(value = "新增或者更新设施", httpMethod = "POST")
    @RequestMapping(value="/insertOrUpdate",method = RequestMethod.POST)
    public ResponseResult insertOrUpdate(@RequestBody FacilitiesInfo facilitiesInfo){
        if(StringUtils.isNull(facilitiesInfo)){
            return error("参数不能为空");
        }
        Boolean insertOrUpdateRes = facilitiesService.insertOrUpdate(facilitiesInfo);
        return insertOrUpdateRes?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    @Log(title = "配置管理",action = "获取设施列表")
    @ApiOperation(value = "获取设施列表", httpMethod = "GET",notes = "支持分页")
    @RequestMapping(value="/getFacilitiesList",method = RequestMethod.GET)
    public ResponseResult<PageBean<FacilitiesListResp>> getFacilitiesList(@CurUser ShiroUser shiroUser, SearchReq searchReq){
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())){
            List<Integer> precinctIds = shiroUser.getPrecinctIds();
            if(StringUtils.isEmpty(precinctIds)){
                return ResponseResult.buildOkResult();
            }
            dataScope = new DataScope("precinct_id",precinctIds);
        }
        PageBean<FacilitiesListResp> res = facilitiesService.getFacilitiesList(searchReq,dataScope);
        return ResponseResult.buildOkResult(res);
    }

    @Log(title = "配置管理",action = "获取设施详情")
    @ApiOperation(value = "获取设施详情", httpMethod = "GET")
    @RequestMapping(value="/getFacilitiesInfoById/{faId}",method = RequestMethod.GET)
    public ResponseResult<FacilitiesInfo> getUnitInfoById(@ApiParam(value = "设施id",required = true) @PathVariable("faId") Integer faId){
        if(StringUtils.isEmpty(faId)){
            return error("设施id不能为空");
        }
        Facilities facilities = facilitiesService.selectByPrimaryKey(faId);
        if(StringUtils.isNull(facilities)){
            return error("该设施不存在");
        }
        FacilitiesInfo facilitiesInfo = new FacilitiesInfo();
        BeanUtils.copyProperties(facilities,facilitiesInfo);
        if(StringUtils.isNotEmpty(facilities.getFaSystemId()) && StringUtils.isNotEmpty(facilities.getFaTypeId())) {
            String dictType = FaSystemType.getCodeById(facilities.getFaSystemId());
            if(StringUtils.isNotEmpty(dictType)) {
                DictDataResp dictDataResp = dictDataComponent.getDictDataByTypeAndValue(dictType, facilities.getFaTypeId().toString());
                if(StringUtils.isNotNull(dictDataResp)) {
                    facilitiesInfo.setFaTypeDesc(dictDataResp.getDictLabel());
                }
            }
        }
        return ResponseResult.buildOkResult(facilitiesInfo);
    }

    @ApiOperation(value = "删除设施", httpMethod = "POST")
    @PostMapping(value = "/delFacilitiesById")
    public ResponseResult delFacilitiesById(@ApiParam(value = "设施id") @RequestParam(value = "faId") Integer faId){
        int result = facilitiesService.delFacilitiesById(faId);
        return result>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }
}
