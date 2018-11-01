package com.mailian.firecontrol.api.web.controller.management;

import com.mailian.core.annotation.Log;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.StructType;
import com.mailian.firecontrol.dao.auto.model.Facilities;
import com.mailian.firecontrol.dto.web.FacilitiesInfo;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.FacilitiesListResp;
import com.mailian.firecontrol.service.DiagramStructService;
import com.mailian.firecontrol.service.FacilitiesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/management/facilities")
@Api(description = "设施信息相关接口")
@WebAPI
public class FacilitiesController extends BaseController {
    @Resource
    private FacilitiesService facilitiesService;
    @Resource
    private DiagramStructService diagramStructService;

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
    public ResponseResult<PageBean<FacilitiesListResp>> getFacilitiesList(SearchReq searchReq){
        if(StringUtils.isEmpty(searchReq.getUnitId())){
            return error("单位id不能为空");
        }
        PageBean<FacilitiesListResp> res = facilitiesService.getFacilitiesList(searchReq);
        return ResponseResult.buildOkResult(res);
    }

    @Log(title = "配置管理",action = "获取单位详情")
    @ApiOperation(value = "获取单位详情", httpMethod = "GET")
    @RequestMapping(value="/getFacilitiesInfoById/{faId}",method = RequestMethod.GET)
    public ResponseResult<FacilitiesInfo> getUnitInfoById(@ApiParam(value = "设施id") @PathVariable("faId") Integer faId){
        if(StringUtils.isEmpty(faId)){
            return error("设施id不能为空");
        }
        Facilities facilities = facilitiesService.selectByPrimaryKey(faId);
        if(StringUtils.isNull(facilities)){
            return error("该设施不存在");
        }
        FacilitiesInfo facilitiesInfo = new FacilitiesInfo();
        BeanUtils.copyProperties(facilities,facilitiesInfo);
        //TODO 通过设施型号id获取型号描述
        //facilitiesInfo.setFaTypeDesc("");

        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("facilities_id",faId);
        queryMap.put("structType",StructType.FACILITY.id);
        facilitiesInfo.setDiagramStructs(diagramStructService.getDiagramStructByMap(queryMap));
        return ResponseResult.buildOkResult(facilitiesInfo);
    }
}
