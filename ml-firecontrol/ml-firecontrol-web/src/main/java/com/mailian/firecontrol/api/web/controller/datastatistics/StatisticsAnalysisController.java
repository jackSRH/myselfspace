package com.mailian.firecontrol.api.web.controller.datastatistics;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mailian.core.annotation.CurUser;
import com.mailian.core.annotation.Log;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.DateUtil;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.AlarmType;
import com.mailian.firecontrol.common.enums.FaSystemType;
import com.mailian.firecontrol.common.enums.UnitType;
import com.mailian.firecontrol.common.manager.SystemManager;
import com.mailian.firecontrol.dao.auto.model.Facilities;
import com.mailian.firecontrol.dao.auto.model.FacilitiesAlarm;
import com.mailian.firecontrol.dao.auto.model.Precinct;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dao.manual.model.FaNumGySystem;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.dto.web.request.AlarmStatisticsReq;
import com.mailian.firecontrol.dto.web.response.AlarmListResp;
import com.mailian.firecontrol.dto.web.response.AlarmStatisticsResp;
import com.mailian.firecontrol.dto.web.response.FaNumStatisticsResp;
import com.mailian.firecontrol.dto.web.response.MisreportAlarmReap;
import com.mailian.firecontrol.dto.web.response.UnitFaNumStatistics;
import com.mailian.firecontrol.service.FacilitiesAlarmService;
import com.mailian.firecontrol.service.FacilitiesService;
import com.mailian.firecontrol.service.PrecinctService;
import com.mailian.firecontrol.service.UnitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/data/statisticsanalysis")
@Api(description = "统计分析接口")
@WebAPI
public class StatisticsAnalysisController extends BaseController {

    @Resource
    private FacilitiesService facilitiesService;
    @Resource
    private UnitService unitService;
    @Resource
    private PrecinctService precinctService;
    @Resource
    private FacilitiesAlarmService facilitiesAlarmService;

    @Log(title = "统计分析",action = "区域设施数量统计")
    @ApiOperation(value = "区域设施数量统计", httpMethod = "GET")
    @RequestMapping(value="/faNumStatistics",method = RequestMethod.GET)
    public ResponseResult<List<FaNumStatisticsResp>> faNumStatistics(@CurUser ShiroUser shiroUser,
         @ApiParam(value = "监管等级") @RequestParam(value = "superviseLevel", required = false,defaultValue = "1")Integer superviseLevel){
        //初始化数据
        List<FaNumStatisticsResp> faNumStatisticsResps = new ArrayList<>();
        FaNumStatisticsResp fireSystemFaStatistics = new FaNumStatisticsResp();
        fireSystemFaStatistics.setFaSystemId(FaSystemType.FIRESYSTEM.id);
        faNumStatisticsResps.add(fireSystemFaStatistics);
        FaNumStatisticsResp powerSystemFaStatistics = new FaNumStatisticsResp();
        powerSystemFaStatistics.setFaSystemId(FaSystemType.POWERSYSTEM.id);
        faNumStatisticsResps.add(powerSystemFaStatistics);

        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())){
            List<Integer> precinctIds = shiroUser.getPrecinctIds();
            if(StringUtils.isEmpty(precinctIds)){
                return ResponseResult.buildOkResult(faNumStatisticsResps);
            }
            dataScope = new DataScope("precinct_id", precinctIds);
        }
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("precinctScope", dataScope);
        queryMap.put("superviseLevel",superviseLevel);
        List<Unit> units = unitService.selectByMap(queryMap);
        if(StringUtils.isEmpty(units)){
            return ResponseResult.buildOkResult(faNumStatisticsResps);
        }

        List<Integer> unitIds = new ArrayList<>();
        for(Unit unit : units){
            unitIds.add(unit.getId());
        }
        List<FaNumGySystem> faNumGySystems = facilitiesService.countFaNumGySystem(unitIds);
        if(StringUtils.isNotEmpty(faNumGySystems)){
            for(FaNumGySystem faNumGySystem : faNumGySystems){
                if(FaSystemType.FIRESYSTEM.id.intValue() ==  faNumGySystem.getSystemId().intValue()){
                    faNumStatisticsResps.get(0).setFaNum(faNumGySystem.getFaNum());
                }

                if(FaSystemType.POWERSYSTEM.id.intValue() ==  faNumGySystem.getSystemId().intValue()){
                    faNumStatisticsResps.get(1).setFaNum(faNumGySystem.getFaNum());
                }
            }
        }
        return ResponseResult.buildOkResult(faNumStatisticsResps);
    }

    @Log(title = "统计分析",action = "区域单位设施数量列表")
    @ApiOperation(value = "区域单位设施数量列表", httpMethod = "GET")
    @RequestMapping(value="/unitFaNumStatistics",method = RequestMethod.GET)
    public ResponseResult<PageBean<UnitFaNumStatistics>> faNumStatistics(@CurUser ShiroUser shiroUser,
            @ApiParam(value = "监管等级") @RequestParam(value = "superviseLevel", required = false,defaultValue = "1") Integer superviseLevel,
            @ApiParam(name = "页数") @RequestParam(required = false,defaultValue = "1") Integer currentPage,
            @ApiParam(name = "每页条数") @RequestParam(required = false,defaultValue = "10") Integer pageSize) {
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())){
            List<Integer> precinctIds = shiroUser.getPrecinctIds();
            if(StringUtils.isEmpty(precinctIds)){
                return ResponseResult.buildOkResult(new PageBean<>());
            }
            dataScope = new DataScope("precinct_id", precinctIds);
        }

        Page page = PageHelper.startPage(currentPage,pageSize);
        page.setOrderBy("create_time asc");
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("precinctScope", dataScope);
        queryMap.put("superviseLevel",superviseLevel);
        List<Unit> units = unitService.selectByMap(queryMap);
        if(StringUtils.isEmpty(units)){
            return ResponseResult.buildOkResult(new PageBean<>());
        }

        List<Integer> unitIds = new ArrayList<>();
        Map<Integer,Integer> unitId2FaNum = new HashMap<>();
        Set<Integer> precinctIds = new HashSet<>();
        for(Unit unit : units){
            unitIds.add(unit.getId());
            unitId2FaNum.put(unit.getId(),0);
            precinctIds.add(unit.getPrecinctId());
        }

        //统计单位设施数量
        queryMap.clear();
        queryMap.put("unitScope",new DataScope("unit_id", unitIds));
        List<Facilities> facilitiesList = facilitiesService.selectByMap(queryMap);
        if(StringUtils.isNotEmpty(facilitiesList)){
            for(Facilities facilities : facilitiesList){
                Integer unitId = facilities.getUnitId();
                unitId2FaNum.put(unitId,unitId2FaNum.get(unitId) + 1);
            }
        }

        //查找管辖区名称
        List<Precinct> precincts = precinctService.selectBatchIds(precinctIds);
        Map<Integer,String> precinetId2Name = new HashMap<>();
        for(Precinct precinct : precincts){
            precinetId2Name.put(precinct.getId(),precinct.getPrecinctName());
        }

        List<UnitFaNumStatistics> unitFaNumStatisticsList = new ArrayList<>();
        UnitFaNumStatistics unitFaNumStatistics;
        for(Unit unit : units){
            unitFaNumStatistics = new UnitFaNumStatistics();
            unitFaNumStatistics.setUnitName(unit.getUnitName());
            unitFaNumStatistics.setPrecinctName(precinetId2Name.get(unit.getId()));
            unitFaNumStatistics.setFaNum(unitId2FaNum.get(unit.getId()));
            unitFaNumStatisticsList.add(unitFaNumStatistics);
        }

        PageBean<UnitFaNumStatistics> pageBean = new PageBean<>(currentPage,pageSize,(int)page.getTotal(),unitFaNumStatisticsList);
        return ResponseResult.buildOkResult(pageBean);
    }

    @Log(title = "统计分析",action = "告警统计")
    @ApiOperation(value = "告警统计", httpMethod = "GET")
    @RequestMapping(value="/alarmStatistics",method = RequestMethod.GET)
    public ResponseResult<List<AlarmStatisticsResp>> alarmStatistics(@CurUser ShiroUser shiroUser,AlarmStatisticsReq alarmStatisticsReq){
        Integer dateType = alarmStatisticsReq.getDateType();
        Integer alarmType = alarmStatisticsReq.getAlarmType();
        Integer misreport = alarmStatisticsReq.getMisreport();
        Date endDate = new Date();
        Date startDate;
        if(1 == dateType){
            startDate= DateUtil.getDateBeforeDay(endDate,7);
        }{
            startDate= DateUtil.getDateBeforeMonth(endDate,1);
        }
        List<String> dates = DateUtil.getDaysBetween(startDate,endDate);
        List<AlarmStatisticsResp> alarmStatisticsResps = new ArrayList<>();
        AlarmStatisticsResp alarmStatisticsResp;
        for(String date: dates){
            alarmStatisticsResp = new AlarmStatisticsResp();
            alarmStatisticsResp.setDate(date);
            alarmStatisticsResp.setAlarmNum(0);
            alarmStatisticsResps.add(alarmStatisticsResp);
        }

        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())){
            List<Integer> precinctIds = shiroUser.getPrecinctIds();
            if(StringUtils.isEmpty(precinctIds)){
                return ResponseResult.buildOkResult(alarmStatisticsResps);
            }
            dataScope = new DataScope("precinct_id", precinctIds);
        }

        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("precinctScope", dataScope);
        queryMap.put("startDate", startDate);
        queryMap.put("endDate", endDate);
        if(StringUtils.isNotEmpty(alarmType)){
            queryMap.put("alarmType",alarmType);
        }
        if(StringUtils.isNotEmpty(misreport)){
            queryMap.put("misreport",misreport);
        }
        List<FacilitiesAlarm> facilitiesAlarms = facilitiesAlarmService.selectFacilitiesAlarmByMap(queryMap);
        if(StringUtils.isEmpty(facilitiesAlarms)){
            return ResponseResult.buildOkResult(alarmStatisticsResps);
        }

        Map<String,Integer> date2AlarmNum = new HashMap<>();
        String day;
        for(FacilitiesAlarm facilitiesAlarm : facilitiesAlarms){
            day = DateUtil.toString(facilitiesAlarm.getAlarmTime(),DateUtil.DATE_PATTERN);
            int alarmNum = date2AlarmNum.containsKey(day)?date2AlarmNum.get(day):0;
            alarmNum += 1;
            date2AlarmNum.put(day,alarmNum);
        }

        alarmStatisticsResps = new ArrayList<>();
        for(String date: dates){
            alarmStatisticsResp = new AlarmStatisticsResp();
            alarmStatisticsResp.setDate(date);
            int alarmNum = StringUtils.isNotEmpty(date2AlarmNum.get(date))?date2AlarmNum.get(date):0;
            alarmStatisticsResp.setAlarmNum(alarmNum);
            alarmStatisticsResps.add(alarmStatisticsResp);
        }
        return ResponseResult.buildOkResult(alarmStatisticsResps);
    }

    @Log(title = "统计分析",action = "获取告警列表")
    @ApiOperation(value = "获取告警列表", httpMethod = "GET")
    @RequestMapping(value="/getAlarmList",method = RequestMethod.GET)
    public ResponseResult<PageBean<AlarmListResp>> getAlarmList(@CurUser ShiroUser shiroUser, AlarmStatisticsReq alarmStatisticsReq) {
        Integer dateType = alarmStatisticsReq.getDateType();
        Integer alarmType = alarmStatisticsReq.getAlarmType();
        Integer misreport = alarmStatisticsReq.getMisreport();
        Integer currentPage = alarmStatisticsReq.getCurrentPage();
        Integer pageSize = alarmStatisticsReq.getPageSize();

        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())){
            List<Integer> precinctIds = shiroUser.getPrecinctIds();
            if(StringUtils.isEmpty(precinctIds)){
                return ResponseResult.buildOkResult(new PageBean<>());
            }
            dataScope = new DataScope("precinct_id", precinctIds);
        }

        Date endDate = new Date();
        Date startDate;
        if(1 == dateType){
            startDate= DateUtil.getDateBeforeDay(endDate,7);
        }{
            startDate= DateUtil.getDateBeforeMonth(endDate,1);
        }

        Page page = PageHelper.startPage(currentPage,pageSize);
        page.setOrderBy("alarm_time asc");
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("precinctScope", dataScope);
        queryMap.put("startDate", startDate);
        queryMap.put("endDate", endDate);
        if(StringUtils.isNotEmpty(alarmType)){
            queryMap.put("alarmType",alarmType);
        }
        if(StringUtils.isNotEmpty(misreport)){
            queryMap.put("misreport",misreport);
        }
        List<FacilitiesAlarm> facilitiesAlarms = facilitiesAlarmService.selectFacilitiesAlarmByMap(queryMap);
        if(StringUtils.isEmpty(facilitiesAlarms)){
            return ResponseResult.buildOkResult(new PageBean<>());
        }

        //查找单位名称
        Set<Integer> unitIds= new HashSet<>();
        for(FacilitiesAlarm facilitiesAlarm : facilitiesAlarms){
            unitIds.add(facilitiesAlarm.getUnitId());
        }
        List<Unit> units = unitService.selectBatchIds(unitIds);
        Map<Integer,String> unitId2Name = new HashMap<>();
        for(Unit unit : units){
            unitId2Name.put(unit.getId(),unit.getUnitName());
        }

        List<AlarmListResp>  alarmListResps = new ArrayList<>();
        AlarmListResp alarmListResp ;
        for(FacilitiesAlarm facilitiesAlarm :facilitiesAlarms){
            alarmListResp = new AlarmListResp();
            alarmListResp.setAlarmContent(facilitiesAlarm.getAlarmContent());
            alarmListResp.setAlarmTime(facilitiesAlarm.getAlarmTime());
            alarmListResp.setAlarmTypeDesc(AlarmType.getDesc(facilitiesAlarm.getAlarmType()));
            alarmListResp.setHandleTime(facilitiesAlarm.getHandleTime());
            alarmListResp.setHandleEndTime(facilitiesAlarm.getHandleEndTime());
            alarmListResp.setUnitName(unitId2Name.get(facilitiesAlarm.getUnitId()));
            alarmListResps.add(alarmListResp);
        }
        PageBean<AlarmListResp> pageBean = new PageBean<>(currentPage,pageSize,(int)page.getTotal(),alarmListResps);
        return ResponseResult.buildOkResult(pageBean);
    }

    @Log(title = "统计分析",action = "获取误告列表")
    @ApiOperation(value = "获取误告列表", httpMethod = "GET")
    @RequestMapping(value="/getMisreportAlarmList",method = RequestMethod.GET)
    public ResponseResult<PageBean<MisreportAlarmReap>> getMisreportAlarmList(@CurUser ShiroUser shiroUser, AlarmStatisticsReq alarmStatisticsReq){
        Integer dateType = alarmStatisticsReq.getDateType();
        Integer currentPage = alarmStatisticsReq.getCurrentPage();
        Integer pageSize = alarmStatisticsReq.getPageSize();

        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())){
            List<Integer> precinctIds = shiroUser.getPrecinctIds();
            if(StringUtils.isEmpty(precinctIds)){
                return ResponseResult.buildOkResult(new PageBean<>());
            }
            dataScope = new DataScope("precinct_id", precinctIds);
        }

        Date endDate = new Date();
        Date startDate;
        if(1 == dateType){
            startDate= DateUtil.getDateBeforeDay(endDate,7);
        }{
            startDate= DateUtil.getDateBeforeMonth(endDate,1);
        }
        Page page = PageHelper.startPage(currentPage,pageSize);
        page.setOrderBy("alarm_time asc");
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("precinctScope", dataScope);
        queryMap.put("startDate", startDate);
        queryMap.put("endDate", endDate);
        queryMap.put("misreport",1);
        List<FacilitiesAlarm> facilitiesAlarms = facilitiesAlarmService.selectFacilitiesAlarmByMap(queryMap);
        if(StringUtils.isEmpty(facilitiesAlarms)){
            return ResponseResult.buildOkResult(new PageBean<>());
        }

        //查找单位名称
        Set<Integer> unitIds= new HashSet<>();
        for(FacilitiesAlarm facilitiesAlarm : facilitiesAlarms){
            unitIds.add(facilitiesAlarm.getUnitId());
        }
        List<Unit> units = unitService.selectBatchIds(unitIds);
        Map<Integer,Unit> unitId2Unit = new HashMap<>();
        for(Unit unit : units){
            unitId2Unit.put(unit.getId(),unit);
        }

        List<MisreportAlarmReap> misreportAlarmReaps = new ArrayList<>();
        MisreportAlarmReap misreportAlarmReap;
        for(FacilitiesAlarm facilitiesAlarm : facilitiesAlarms){
            misreportAlarmReap = new MisreportAlarmReap();
            misreportAlarmReap.setAlarmContent(facilitiesAlarm.getAlarmContent());
            misreportAlarmReap.setAlarmTime(facilitiesAlarm.getAlarmTime());
            misreportAlarmReap.setAlarmTypeDesc(AlarmType.getDesc(facilitiesAlarm.getAlarmType()));
            Unit unit = unitId2Unit.get(facilitiesAlarm.getUnitId());
            if(StringUtils.isNotNull(unit)){
                misreportAlarmReap.setUnitTypeDesc(UnitType.getValue(unit.getUnitType()));
                misreportAlarmReap.setUserName(unit.getTransactor());
            }
            misreportAlarmReaps.add(misreportAlarmReap);
        }

        PageBean<MisreportAlarmReap> pageBean = new PageBean<>(currentPage,pageSize,(int)page.getTotal(),misreportAlarmReaps);
        return ResponseResult.buildOkResult(pageBean);
    }


}
