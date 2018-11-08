package com.mailian.firecontrol.api.app.controller;

import com.mailian.core.annotation.AppAPI;
import com.mailian.core.annotation.CurUser;
import com.mailian.core.annotation.Log;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.BasePage;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.AlarmHandleStatus;
import com.mailian.firecontrol.common.enums.NoticeType;
import com.mailian.firecontrol.common.enums.RealNoticeType;
import com.mailian.firecontrol.common.manager.SystemManager;
import com.mailian.firecontrol.dao.auto.model.FacilitiesAlarm;
import com.mailian.firecontrol.dao.auto.model.Precinct;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dto.CountDataInfo;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.dto.app.AppAlarmInfo;
import com.mailian.firecontrol.dto.app.NoticeInfo;
import com.mailian.firecontrol.dto.app.response.AppRealtimeDataResp;
import com.mailian.firecontrol.dto.app.response.AppUnitDetailResp;
import com.mailian.firecontrol.dto.app.response.AppUnitResp;
import com.mailian.firecontrol.dto.web.response.AlarmNumResp;
import com.mailian.firecontrol.dto.web.response.CameraListResp;
import com.mailian.firecontrol.service.FacilitiesAlarmService;
import com.mailian.firecontrol.service.PrecinctService;
import com.mailian.firecontrol.service.UnitCameraService;
import com.mailian.firecontrol.service.UnitService;
import com.mailian.firecontrol.service.cache.NoticeCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/6
 * @Description:
 */
@RestController
@RequestMapping("/app/unit")
@Api(description = "单位相关接口")
@AppAPI
public class AppUnitController extends BaseController {
    @Autowired
    private UnitService unitService;
    @Autowired
    private FacilitiesAlarmService facilitiesAlarmService;
    @Autowired
    private NoticeCache noticeCache;
    @Autowired
    private PrecinctService precinctService;
    @Autowired
    private UnitCameraService unitCameraService;

    @Log(title = "app单位",action = "搜素单位")
    @ApiOperation(value = "搜索单位列表", httpMethod = "GET")
    @GetMapping(value = "/list")
    public ResponseResult<List<AppUnitResp>> list(@CurUser ShiroUser shiroUser,
                                                  @ApiParam(value = "单位名称") @RequestParam(value = "name",required = false) String name,
                                                  @ApiParam(value = "分页信息") BasePage basePage){
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


    @Log(title = "app单位",action = "单位详情(管理端)")
    @ApiOperation(value = "单位详情(管理端)", httpMethod = "GET")
    @GetMapping(value = "/detail")
    public ResponseResult<AppUnitDetailResp> detail(@ApiParam(value = "单位id") @RequestParam(value = "unitId") Integer unitId){
        AppUnitDetailResp appUnitDetailResp = unitService.getAppUnitDetailById(unitId);
        /*设置摄像头*/
        List<CameraListResp> cameraListResps = unitCameraService.getListByUnitId(unitId);
        appUnitDetailResp.setCameraListResps(cameraListResps);
        return ResponseResult.buildOkResult(appUnitDetailResp);
    }


    @Log(title = "app单位",action = "单位详情用户端")
    @ApiOperation(value = "单位详情(用户端)", httpMethod = "GET")
    @GetMapping(value = "/detailByUser")
    public ResponseResult<AppUnitDetailResp> detailByUser(@CurUser ShiroUser shiroUser){
        Integer unitId = shiroUser.getUnitId();
        if(StringUtils.isEmpty(unitId)){
            return error("当前用户没有所属单位!");
        }

        AppUnitDetailResp appUnitDetailResp = unitService.getAppUnitDetailById(unitId);
        /*设置摄像头*/
        List<CameraListResp> cameraListResps = unitCameraService.getListByUnitId(unitId);
        appUnitDetailResp.setCameraListResps(cameraListResps);
        return ResponseResult.buildOkResult(appUnitDetailResp);
    }


    @Log(title = "app首页",action = "实时监测数据")
    @ApiOperation(value = "实时监测", httpMethod = "GET")
    @GetMapping(value = "/realtimeData")
    public ResponseResult<AppRealtimeDataResp> realtimeData(@CurUser ShiroUser shiroUser){
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())) {
            List<Integer> precinctIds = shiroUser.getPrecinctIds();
            if(StringUtils.isEmpty(precinctIds)){
                return ResponseResult.buildOkResult(new AppRealtimeDataResp());
            }
            dataScope = new DataScope("precinct_id", precinctIds);
        }
        AlarmNumResp alarmNumResp = facilitiesAlarmService.getAlarmNumByAreaAndScope(null,dataScope);
        CountDataInfo countDataInfo = unitService.getUnitTotalByScope(dataScope);

        AppRealtimeDataResp appRealtimeDataResp = new AppRealtimeDataResp();
        appRealtimeDataResp.setAlarmNumResp(alarmNumResp);
        appRealtimeDataResp.setCountDataInfo(countDataInfo);
        return ResponseResult.buildOkResult(appRealtimeDataResp);
    }

    @ApiOperation(value = "获取告警通知(管理端)", httpMethod = "GET")
    @GetMapping(value="/getAlarmNotice")
    public ResponseResult<List<NoticeInfo>> getAlarmNotice(@CurUser ShiroUser shiroUser){
        List<Integer> precinctIds = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())) {
            precinctIds = shiroUser.getPrecinctIds();
            if (StringUtils.isEmpty(precinctIds)) {
                return ResponseResult.buildOkResult(new ArrayList<NoticeInfo>());
            }
        }

        List<NoticeInfo> noticeInfos = noticeCache.getNoticesByType(NoticeType.ALARM);

        List<NoticeInfo> noticeInfoList = new ArrayList<>();
        for (NoticeInfo noticeInfo : noticeInfos) {
            if(StringUtils.isNull(precinctIds) || precinctIds.contains(noticeInfo.getPrecinctId())){
                noticeInfoList.add(noticeInfo);
            }
        }
        return ResponseResult.buildOkResult(noticeInfoList);
    }


    @ApiOperation(value = "获取告警通知(用户端)", httpMethod = "GET")
    @GetMapping(value="/getAlarmNoticeByUser")
    public ResponseResult<List<NoticeInfo>> getAlarmNoticeByUser(@CurUser ShiroUser shiroUser){
        Integer unitId = shiroUser.getUnitId();
        if(StringUtils.isEmpty(unitId)){
            return error("当前用户没有所属单位!");
        }

        List<NoticeInfo> noticeInfos = noticeCache.getNoticesByType(NoticeType.ALARM);
        List<NoticeInfo> noticeInfoList = new ArrayList<>();
        for (NoticeInfo noticeInfo : noticeInfos) {
            if(noticeInfo.getUnitId().equals(unitId)) {
                noticeInfoList.add(noticeInfo);
            }
        }
        return ResponseResult.buildOkResult(noticeInfoList);
    }


    @ApiOperation(value = "获取告警详情", httpMethod = "GET")
    @GetMapping(value="/getAlarmDetail/{alarmId}")
    public ResponseResult<AppAlarmInfo> getAlarmDetail(@CurUser ShiroUser shiroUser,@ApiParam(value = "告警id") @PathVariable(name = "alarmId") Integer alarmId){
        AppAlarmInfo alarmInfo = new AppAlarmInfo();
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("alarmId",alarmId);
        List<FacilitiesAlarm> facilitiesAlarmList = facilitiesAlarmService.selectByMap(queryMap);
        if(StringUtils.isEmpty(facilitiesAlarmList)){
            return ResponseResult.buildOkResult(new AppAlarmInfo());
        }

        FacilitiesAlarm facilitiesAlarm = facilitiesAlarmList.get(0);
        if(!SystemManager.isAdminRole(shiroUser.getRoles())) {
            List<Integer> precinctIds = shiroUser.getPrecinctIds();
            if (StringUtils.isEmpty(precinctIds)) {
                return ResponseResult.buildOkResult(new AppAlarmInfo());
            }

            if(!precinctIds.contains(facilitiesAlarm.getPrecinctId())){
                return ResponseResult.buildOkResult(new AppAlarmInfo());
            }
        }

        Integer handleStatus = facilitiesAlarm.getHandleStatus();
        if(AlarmHandleStatus.UNTREATED.id.equals(handleStatus)) {
            FacilitiesAlarm upAlarm = new FacilitiesAlarm();
            upAlarm.setId(facilitiesAlarm.getId());
            upAlarm.setHandleStatus(AlarmHandleStatus.RESPONSE.id);
            handleStatus = AlarmHandleStatus.RESPONSE.id;
            facilitiesAlarmService.updateByPrimaryKeySelective(upAlarm);
        }

        Unit unit = unitService.selectByPrimaryKey(facilitiesAlarm.getUnitId());
        if(StringUtils.isNull(unit)){
            return error("单位不存在");
        }

        Precinct precinct = precinctService.selectByPrimaryKey(unit.getPrecinctId());
        if(StringUtils.isNull(precinct)){
            return error("管辖区不存在");
        }

        alarmInfo.setId(facilitiesAlarm.getId());
        alarmInfo.setLng(StringUtils.nvl(unit.getLng(),new BigDecimal(0)).doubleValue());
        alarmInfo.setLat(StringUtils.nvl(unit.getLat(),new BigDecimal(0)).doubleValue());
        alarmInfo.setAlarmTime(facilitiesAlarm.getAlarmTime());
        alarmInfo.setAlarmEndTime(facilitiesAlarm.getAlarmEndTime());
        alarmInfo.setAlarmContent(facilitiesAlarm.getAlarmContent());
        alarmInfo.setUnitName(unit.getUnitName());
        alarmInfo.setUnitAddress(unit.getAddress());
        alarmInfo.setUnitType(unit.getUnitType());
        alarmInfo.setTransactor(unit.getTransactor());
        alarmInfo.setContactPhone(unit.getContactPhone());
        alarmInfo.setPrecinctName(precinct.getPrecinctName());
        alarmInfo.setDutyName(precinct.getDutyName());
        alarmInfo.setDutyPhone(precinct.getDutyPhone());
        alarmInfo.setHandleStatus(handleStatus);

        /*设置摄像头*/
        List<CameraListResp> cameraListResps = unitCameraService.getListByUnitId(unit.getId());
        alarmInfo.setCameraListResps(cameraListResps);

        /* 设置消息已读 */
        NoticeInfo noticeInfo = new NoticeInfo();
        noticeInfo.setId(alarmId);
        noticeInfo.setUid(null);
        noticeInfo.setType(NoticeType.ALARM.id);
        noticeInfo.setRealType(RealNoticeType.ALARM_NOTICE.id);
        noticeCache.readNotice(noticeInfo);
        return ResponseResult.buildOkResult(alarmInfo);
    }

}
