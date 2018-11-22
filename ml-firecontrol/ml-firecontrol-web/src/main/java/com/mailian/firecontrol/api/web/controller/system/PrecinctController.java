package com.mailian.firecontrol.api.web.controller.system;

import com.mailian.core.annotation.Log;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.manager.ValidationManager;
import com.mailian.firecontrol.dao.auto.model.Precinct;
import com.mailian.firecontrol.dto.web.request.PrecinctQueryReq;
import com.mailian.firecontrol.dto.web.request.PrecinctReq;
import com.mailian.firecontrol.dto.web.response.PrecinctResp;
import com.mailian.firecontrol.service.PrecinctService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/31
 * @Description:
 */
@RestController
@RequestMapping("/system/precinct")
@Api(description = "管辖区相关接口")
@WebAPI
public class PrecinctController {
    @Autowired
    private PrecinctService precinctService;

    @Log(title = "系统",action = "获取管辖区列表")
    @ApiOperation(value = "获取管辖区列表", httpMethod = "POST")
    @PostMapping(value = "getList")
    public ResponseResult<PageBean<PrecinctResp>> getList(@ApiParam(value = "管辖区查询信息") PrecinctQueryReq queryReq){
        return ResponseResult.buildOkResult(precinctService.queryByPage(queryReq));
    }

    @ApiOperation(value = "获取所有管辖区", httpMethod = "POST")
    @PostMapping(value = "getAllList")
    public ResponseResult<List<PrecinctResp>> getAllList(){
        List<Precinct> precincts = precinctService.selectByMap(null);
        List<PrecinctResp> precinctRespList = new ArrayList<>();
        for (Precinct precinct : precincts) {
            PrecinctResp precinctResp = new PrecinctResp();
            precinctResp.setId(precinct.getId());
            precinctResp.setPrecinctName(precinct.getPrecinctName());
            precinctRespList.add(precinctResp);
        }
        return ResponseResult.buildOkResult(precinctRespList);
    }


    @ApiOperation(value = "根据区域获取管辖区", httpMethod = "GET")
    @GetMapping(value = "getPrecinctByAreaId")
    public ResponseResult<List<PrecinctResp>> getPrecinctByAreaId(@ApiParam(value = "区域id") @RequestParam(value = "areaId") Integer areaId){
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("areaId",areaId);
        List<Precinct> precincts = precinctService.selectByMap(queryMap);
        List<PrecinctResp> precinctRespList = new ArrayList<>();
        for (Precinct precinct : precincts) {
            PrecinctResp precinctResp = new PrecinctResp();
            precinctResp.setId(precinct.getId());
            precinctResp.setPrecinctName(precinct.getPrecinctName());
            precinctRespList.add(precinctResp);
        }
        return ResponseResult.buildOkResult(precinctRespList);
    }


    @Log(title = "系统",action = "新增或修改管辖区")
    @ApiOperation(value = "新增或修改管辖区", httpMethod = "POST")
    @PostMapping(value = "insertOrUpdate")
    public ResponseResult insertOrUpdate(@ApiParam(value = "管辖区信息") @RequestBody @Validated(ValidationManager.CommonValidation.class) PrecinctReq precinctReq){
        int result = precinctService.insertOrUpdate(precinctReq);
        return result>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    @Log(title = "系统",action = "删除管辖区")
    @ApiOperation(value = "删除管辖区", httpMethod = "POST")
    @PostMapping(value = "delete")
    public ResponseResult delete(@ApiParam(value = "管辖区id") @RequestParam(value = "precinctId") Integer precinctId){
        int result = precinctService.deleteByPrimaryKey(precinctId);
        return result>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

}
