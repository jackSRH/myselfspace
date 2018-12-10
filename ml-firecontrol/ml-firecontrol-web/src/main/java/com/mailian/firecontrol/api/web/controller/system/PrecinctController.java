package com.mailian.firecontrol.api.web.controller.system;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Filter;
import com.mailian.core.annotation.Log;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.db.DataScope;
import com.mailian.core.manager.ValidationManager;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.dao.auto.model.Precinct;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dto.web.request.PrecinctQueryReq;
import com.mailian.firecontrol.dto.web.request.PrecinctReq;
import com.mailian.firecontrol.dto.web.response.PrecinctResp;
import com.mailian.firecontrol.framework.annotation.PrecinctUnitScope;
import com.mailian.firecontrol.service.PrecinctService;
import com.mailian.firecontrol.service.UnitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    @Autowired
    private UnitService unitService;

    @Log(title = "系统",action = "获取管辖区列表")
    @ApiOperation(value = "获取管辖区列表", httpMethod = "POST")
    @PostMapping(value = "getList")
    public ResponseResult<PageBean<PrecinctResp>> getList(@PrecinctUnitScope DataScope dataScope, @ApiParam(value = "管辖区查询信息") PrecinctQueryReq queryReq){
        return ResponseResult.buildOkResult(precinctService.queryByPage(queryReq,dataScope));
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
    public ResponseResult<List<PrecinctResp>> getPrecinctByAreaId(@PrecinctUnitScope DataScope dataScope, @ApiParam(value = "区域id") @RequestParam(value = "areaId") Integer areaId){
        Map<String,Object> queryMap = new HashMap<>();
        List<Precinct> precincts;
        if(StringUtils.isNotNull(dataScope)) {
            if ("precinct_id".equals(dataScope.getScopeName())) {
                precincts = precinctService.selectBatchIds(dataScope.getDataIds());
                precincts = (List<Precinct>)CollectionUtil.filter(precincts, new Filter<Precinct>() {
                    @Override
                    public boolean accept(Precinct precinct) {
                        return precinct.getAreaId().equals(areaId);
                    }
                });
            }else{
                Integer unitId = dataScope.getDataIds().get(0);
                Unit unit = unitService.selectByPrimaryKey(unitId);
                Precinct precinct = precinctService.selectByPrimaryKey(unit.getPrecinctId());
                precincts = Arrays.asList(precinct);
            }
        }else{
            queryMap.put("areaId", areaId);
            precincts = precinctService.selectByMap(queryMap);
        }
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
