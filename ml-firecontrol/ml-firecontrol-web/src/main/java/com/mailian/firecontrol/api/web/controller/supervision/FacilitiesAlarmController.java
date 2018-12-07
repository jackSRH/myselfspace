package com.mailian.firecontrol.api.web.controller.supervision;

import com.mailian.core.annotation.CurUser;
import com.mailian.core.annotation.Log;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.db.DataScope;
import com.mailian.core.enums.BooleanEnum;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.AlarmHandleStatus;
import com.mailian.firecontrol.common.enums.AlarmMisreport;
import com.mailian.firecontrol.dao.auto.model.*;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.dto.web.request.AlarmHandleReq;
import com.mailian.firecontrol.dto.web.request.FireAlarmReq;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.*;
import com.mailian.firecontrol.framework.annotation.PrecinctUnitScope;
import com.mailian.firecontrol.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/supervision/facilitiesalarm")
@Api(description = "设施告警相关接口")
@WebAPI
public class FacilitiesAlarmController extends BaseController {
    @Resource
    private FacilitiesAlarmService facilitiesAlarmService;
    @Resource
    private UnitService unitService;
    @Resource
    private FacilitiesService facilitiesService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AlarmLogService alarmLogService;
    @Autowired
    private PrecinctService precinctService;
    @Autowired
    private UserService userService;

    @Log(title = "单位监控",action = "查找设施告警列表")
    @ApiOperation(value = "查找设施告警列表", httpMethod = "GET")
    @RequestMapping(value="/getFacilitiesAlarmList",method = RequestMethod.GET)
    public ResponseResult<PageBean<FacilitiesAlarmListResp>> getFacilitiesAlarmList(@PrecinctUnitScope(hasPrecinctOrUnit = true) DataScope dataScope,
                                                                                    SearchReq searchReq){
        PageBean<FacilitiesAlarmListResp> res =  facilitiesAlarmService.getFacilitiesAlarmList(dataScope,searchReq);
        return ResponseResult.buildOkResult(res);
    }

    @Log(title = "单位监控",action = "设施告警详情")
    @ApiOperation(value = "设施告警详情", httpMethod = "GET")
    @RequestMapping(value="/getFacilitiesAlarmInfoByAlarmId/{alarmId}",method = RequestMethod.GET)
    public ResponseResult<FacilitiesAlarmResp> getFacilitiesAlarmInfoByAlarmId(@ApiParam(value = "告警id",required = true) @PathVariable("alarmId") Integer alarmId){
        if(StringUtils.isEmpty(alarmId)){
            return error("告警id不能为空");
        }
        FacilitiesAlarm facilitiesAlarm = facilitiesAlarmService.selectByPrimaryKey(alarmId);
        if(StringUtils.isNull(facilitiesAlarm)){
            return error("该告警不存在");
        }

        FacilitiesAlarmResp facilitiesAlarmResp = new FacilitiesAlarmResp();
        BeanUtils.copyProperties(facilitiesAlarm,facilitiesAlarmResp);

        if(StringUtils.isNotEmpty(facilitiesAlarm.getUnitId())) {
            Unit unit = unitService.selectByPrimaryKey(facilitiesAlarm.getUnitId());
            if (StringUtils.isNotNull(unit)) {
                facilitiesAlarmResp.setUnitName(unit.getUnitName());
            }
        }
        if(StringUtils.isNotEmpty(facilitiesAlarm.getHandleUid())){
            User user = userService.selectByPrimaryKey(facilitiesAlarm.getHandleUid());
            if(StringUtils.isNotNull(user)){
                facilitiesAlarmResp.setHandleUserName(user.getFullName());
            }
        }
        return ResponseResult.buildOkResult(facilitiesAlarmResp);
    }

    @Log(title = "单位监控",action = "查找火灾告警列表")
    @ApiOperation(value = "查找火灾告警列表", httpMethod = "GET")
    @RequestMapping(value="/getFireAlarmList",method = RequestMethod.GET)
    public ResponseResult<PageBean<FireAlarmListResp>> getFireAlarmList(@PrecinctUnitScope(hasPrecinctOrUnit = true) DataScope dataScope, SearchReq searchReq){
        PageBean<FireAlarmListResp> res = facilitiesAlarmService.getFireAlarmList(dataScope,searchReq);
        return ResponseResult.buildOkResult(res);
    }

    @Log(title = "单位监控",action = "火灾告警详情")
    @ApiOperation(value = "火灾告警详情", httpMethod = "GET")
    @RequestMapping(value="/getFireAlarmInfoByAlarmId/{alarmId}",method = RequestMethod.GET)
    public ResponseResult<FireAlarmResp> getFireAlarmInfoByAlarmId(@ApiParam(value = "告警id",required = true) @PathVariable("alarmId") Integer alarmId){
        if(StringUtils.isEmpty(alarmId)){
            return error("告警id不能为空");
        }
        FacilitiesAlarm facilitiesAlarm = facilitiesAlarmService.selectByPrimaryKey(alarmId);
        if(StringUtils.isNull(facilitiesAlarm)){
            return error("该告警不存在");
        }

        FireAlarmResp fireAlarmResp = new FireAlarmResp();
        BeanUtils.copyProperties(facilitiesAlarm,fireAlarmResp);

        if(StringUtils.isNotEmpty(facilitiesAlarm.getUnitId())) {
            Unit unit = unitService.selectByPrimaryKey(facilitiesAlarm.getUnitId());
            if (StringUtils.isNotNull(unit)) {
                fireAlarmResp.setUnitName(unit.getUnitName());
            }
        }
        return ResponseResult.buildOkResult(fireAlarmResp);
    }

    @Log(title = "单位监控",action = "查找火灾自动报警列表")
    @ApiOperation(value = "查找火灾自动报警列表", httpMethod = "GET")
    @RequestMapping(value="/getFireAutoAlarmList",method = RequestMethod.GET)
    public ResponseResult<PageBean<FireAutoAlarmListResp>> getFireAutoAlarmList(@PrecinctUnitScope(hasPrecinctOrUnit = true) DataScope dataScope,
                                                                                SearchReq searchReq){
        PageBean<FireAutoAlarmListResp> res = facilitiesAlarmService.getFireAutoAlarmList(dataScope,searchReq);
        return ResponseResult.buildOkResult(res);
    }

    @Log(title = "单位监控",action = "火灾自动报警详情")
    @ApiOperation(value = "火灾自动报警详情", httpMethod = "GET")
    @RequestMapping(value="/getFireAutoAlarmInfoByAlarmId/{alarmId}",method = RequestMethod.GET)
    public ResponseResult<FireAutoAlarmResp> getFireAutoAlarmInfoByAlarmId(@ApiParam(value = "告警id",required = true) @PathVariable("alarmId") Integer alarmId){
        if(StringUtils.isEmpty(alarmId)){
            return error("告警id不能为空");
        }
        FacilitiesAlarm facilitiesAlarm = facilitiesAlarmService.selectByPrimaryKey(alarmId);
        if(StringUtils.isNull(facilitiesAlarm)){
            return error("该告警不存在");
        }
        FireAutoAlarmResp fireAutoAlarmResp = new FireAutoAlarmResp();
        BeanUtils.copyProperties(facilitiesAlarm,fireAutoAlarmResp);
        Integer unitId = facilitiesAlarm.getUnitId();
        if(StringUtils.isNotEmpty(facilitiesAlarm.getUnitId())){
            Unit unit = unitService.selectByPrimaryKey(unitId);
            if(StringUtils.isNotNull(unit)){
                fireAutoAlarmResp.setUnitName(unit.getUnitName());
            }
        }

        Integer facilitiesId = facilitiesAlarm.getFacilitiesId();
        if(StringUtils.isNotEmpty(facilitiesId)){
            Facilities facilities = facilitiesService.selectByPrimaryKey(facilitiesId);
            if(StringUtils.isNotNull(facilities)){
                fireAutoAlarmResp.setFacilitiesName(facilities.getFaName());
            }
        }
        return ResponseResult.buildOkResult(fireAutoAlarmResp);
    }

    @ApiOperation(value = "保存火灾信息",httpMethod = "POST")
    @PostMapping(value = "/saveFireAlarmInfo")
    public ResponseResult saveFireAlarmInfo(@RequestBody FireAlarmReq fireAlarmReq){
        if(StringUtils.isEmpty(fireAlarmReq.getId())){
            return error("火灾id不能为空");
        }

        FacilitiesAlarm facilitiesAlarm = new FacilitiesAlarm();
        facilitiesAlarm.setId(fireAlarmReq.getId());
        facilitiesAlarm.setAlarmReason(fireAlarmReq.getAlarmReason());
        facilitiesAlarm.setAlarmArea(fireAlarmReq.getAlarmArea());
        facilitiesAlarm.setDieNum(fireAlarmReq.getDieNum());
        facilitiesAlarm.setInjured(fireAlarmReq.getInjured());
        facilitiesAlarm.setPropertyLoss(fireAlarmReq.getPropertyLoss());
        facilitiesAlarm.setEmedialMeasures(fireAlarmReq.getEmedialMeasures());
        int result = facilitiesAlarmService.updateByPrimaryKeySelective(facilitiesAlarm);
        return result>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }


    @Log(title = "单位监控",action = "设置火灾自动报警处理信息")
    @ApiOperation(value = "设置火灾自动报警处理信息", httpMethod = "POST")
    @RequestMapping(value="/setAlarmHandleInfo",method = RequestMethod.POST)
    public ResponseResult setAlarmHandleInfo(@CurUser ShiroUser shiroUser,@RequestBody AlarmHandleReq alarmHandleReq){
        if(StringUtils.isNull(alarmHandleReq)){
            return error("参数不能为空");
        }
        if(StringUtils.isEmpty(alarmHandleReq.getId())){
            return error("告警id不能为空");
        }
        Integer alarmId = alarmHandleReq.getId();
        /*判断警情当前状态*/
        FacilitiesAlarm alarmDb = facilitiesAlarmService.selectByPrimaryKey(alarmHandleReq.getId());
        if(StringUtils.isNull(alarmDb)){
            return error("告警不存在");
        }

        List<Role> roleList = roleService.selectRolesByUserId(shiroUser.getId());
        String roleName = "";
        if(StringUtils.isNotEmpty(roleList)) {
            roleName = roleList.get(0).getRoleName();
        }

        Integer handleStatus = StringUtils.nvl(alarmDb.getHandleStatus(),AlarmHandleStatus.UNTREATED.id);
        if(AlarmHandleStatus.UNTREATED.id.equals(handleStatus) || AlarmHandleStatus.RESPONSE.id.equals(handleStatus)){
            if(AlarmMisreport.MISREPORT.id.equals(alarmHandleReq.getMisreport())){
                facilitiesAlarmService.misreportAlarm(alarmDb,shiroUser.getId(),shiroUser.getFullName(),roleName,alarmId);
            }else if(AlarmMisreport.EFFECTIVE.id.equals(alarmHandleReq.getMisreport())){
                Precinct precinct = precinctService.selectByPrimaryKey(alarmDb.getPrecinctId());
                facilitiesAlarmService.effectiveAlarm(alarmDb,shiroUser.getId(),shiroUser.getFullName(),precinct.getDutyName(),roleName,alarmId,BooleanEnum.YES.id.equals(alarmHandleReq.getHandleStatus()),alarmHandleReq.getHandleEndTime(),alarmHandleReq.getHandleResult());
            }
        }

        if(AlarmHandleStatus.UNDER_WAY.id.equals(handleStatus) && BooleanEnum.YES.id.equals(alarmHandleReq.getHandleStatus())){
            facilitiesAlarmService.completeAlarm(shiroUser.getId(),shiroUser.getFullName(),roleName,alarmId,alarmHandleReq.getHandleEndTime(),alarmHandleReq.getHandleResult());
        }
        return ResponseResult.buildOkResult();
    }

}
