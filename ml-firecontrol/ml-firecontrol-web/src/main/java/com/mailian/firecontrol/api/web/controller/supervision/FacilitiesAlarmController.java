package com.mailian.firecontrol.api.web.controller.supervision;

import com.mailian.core.annotation.CurUser;
import com.mailian.core.annotation.Log;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.manager.SystemManager;
import com.mailian.firecontrol.dao.auto.model.FacilitiesAlarm;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.FacilitiesAlarmListResp;
import com.mailian.firecontrol.dto.web.response.FacilitiesAlarmResp;
import com.mailian.firecontrol.dto.web.response.FireAlarmListResp;
import com.mailian.firecontrol.dto.web.response.FireAlarmResp;
import com.mailian.firecontrol.service.FacilitiesAlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/supervision/facilitiesalarm")
@Api(description = "设施告警相关接口")
@WebAPI
public class FacilitiesAlarmController extends BaseController {
    @Resource
    private FacilitiesAlarmService facilitiesAlarmService;

    @Log(title = "单位监控",action = "查找设施告警列表")
    @ApiOperation(value = "查找设施告警列表", httpMethod = "GET")
    @RequestMapping(value="/getFacilitiesAlarmList",method = RequestMethod.GET)
    public ResponseResult<PageBean<FacilitiesAlarmListResp>> getFacilitiesAlarmList(@CurUser ShiroUser shiroUser, SearchReq searchReq){
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())){
            dataScope = new DataScope("precinct_id", shiroUser.getPrecinctIds());
        }
        PageBean<FacilitiesAlarmListResp> res =  facilitiesAlarmService.getFacilitiesAlarmList(dataScope,searchReq);
        return ResponseResult.buildOkResult(res);
    }

    @Log(title = "单位监控",action = "设施告警详情")
    @ApiOperation(value = "设施告警详情", httpMethod = "GET")
    @RequestMapping(value="/getFacilitiesAlarmInfoByAlarmId/{alarmId}",method = RequestMethod.GET)
    public ResponseResult<FacilitiesAlarmResp> getFacilitiesAlarmInfoByAlarmId(@ApiParam(value = "告警id") @PathVariable("alarmId") Integer alarmId){
        if(StringUtils.isEmpty(alarmId)){
            return error("告警id不能为空");
        }
        FacilitiesAlarm facilitiesAlarm = facilitiesAlarmService.selectByPrimaryKey(alarmId);
        if(StringUtils.isNull(facilitiesAlarm)){
            return error("该告警不存在");
        }

        FacilitiesAlarmResp facilitiesAlarmResp = new FacilitiesAlarmResp();
        BeanUtils.copyProperties(facilitiesAlarm,facilitiesAlarmResp);
        return ResponseResult.buildOkResult(facilitiesAlarmResp);
    }

    @Log(title = "单位监控",action = "查找火灾告警列表")
    @ApiOperation(value = "查找火灾告警列表", httpMethod = "GET")
    @RequestMapping(value="/getFireAlarmList",method = RequestMethod.GET)
    public ResponseResult<PageBean<FireAlarmListResp>> getFireAlarmList(@CurUser ShiroUser shiroUser, SearchReq searchReq){
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())){
            dataScope = new DataScope("precinct_id", shiroUser.getPrecinctIds());
        }
        PageBean<FireAlarmListResp> res = facilitiesAlarmService.getFireAlarmList(dataScope,searchReq);
        return ResponseResult.buildOkResult(res);
    }

    @Log(title = "单位监控",action = "火灾告警详情")
    @ApiOperation(value = "火灾告警详情", httpMethod = "GET")
    @RequestMapping(value="/getFireAlarmInfoByAlarmId/{alarmId}",method = RequestMethod.GET)
    public ResponseResult<FireAlarmResp> getFireAlarmInfoByAlarmId(@ApiParam(value = "告警id") @PathVariable("alarmId") Integer alarmId){
        if(StringUtils.isEmpty(alarmId)){
            return error("告警id不能为空");
        }
        FacilitiesAlarm facilitiesAlarm = facilitiesAlarmService.selectByPrimaryKey(alarmId);
        if(StringUtils.isNull(facilitiesAlarm)){
            return error("该告警不存在");
        }

        FireAlarmResp fireAlarmResp = new FireAlarmResp();
        BeanUtils.copyProperties(facilitiesAlarm,fireAlarmResp);
        return ResponseResult.buildOkResult(fireAlarmResp);
    }

}
