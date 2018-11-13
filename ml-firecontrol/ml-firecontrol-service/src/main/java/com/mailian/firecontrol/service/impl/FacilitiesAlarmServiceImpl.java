package com.mailian.firecontrol.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.bean.PageBean;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.BigDecimalUtil;
import com.mailian.core.util.DateUtil;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.AlarmHandleStatus;
import com.mailian.firecontrol.common.enums.AlarmType;
import com.mailian.firecontrol.common.enums.FaMisreportType;
import com.mailian.firecontrol.common.enums.ReqDateType;
import com.mailian.firecontrol.dao.auto.mapper.FacilitiesAlarmMapper;
import com.mailian.firecontrol.dao.auto.model.Facilities;
import com.mailian.firecontrol.dao.auto.model.FacilitiesAlarm;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dao.manual.mapper.ManageManualMapper;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.AlarmAnalysisResp;
import com.mailian.firecontrol.dto.web.response.AlarmIndustryShareResp;
import com.mailian.firecontrol.dto.web.response.AlarmNumResp;
import com.mailian.firecontrol.dto.web.response.AlarmStatisticsResp;
import com.mailian.firecontrol.dto.web.response.CurAlarmResp;
import com.mailian.firecontrol.dto.web.response.FacilitiesAlarmListResp;
import com.mailian.firecontrol.dto.web.response.FireAlarmCountResp;
import com.mailian.firecontrol.dto.web.response.FireAlarmListResp;
import com.mailian.firecontrol.dto.web.response.FireAutoAlarmListResp;
import com.mailian.firecontrol.service.FacilitiesAlarmService;
import com.mailian.firecontrol.service.FacilitiesService;
import com.mailian.firecontrol.service.UnitService;
import com.mailian.firecontrol.service.util.BuildDefaultResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FacilitiesAlarmServiceImpl extends BaseServiceImpl<FacilitiesAlarm, FacilitiesAlarmMapper> implements FacilitiesAlarmService {

    @Resource
    private UnitService unitService;
    @Resource
    private ManageManualMapper manageManualMapper;
    @Resource
    private FacilitiesService facilitiesService;

    @Override
    public PageBean<FacilitiesAlarmListResp> getFacilitiesAlarmList(DataScope dataScope, SearchReq searchReq){
        String unitName = searchReq.getUnitName();
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("precinctScope", dataScope);
        if(StringUtils.isNotEmpty(unitName)){
            queryMap.put("unitNameLike",unitName);
            List<Unit> units = unitService.selectByMap(queryMap);
            if(StringUtils.isEmpty(units)){
                return new PageBean<>();
            }
            List<Integer> unitIds = new ArrayList<>();
            for(Unit unit : units){
                unitIds.add(unit.getId());
            }
            queryMap.clear();
            queryMap.put("unitIds",unitIds);
        }

        Integer currentPage = searchReq.getCurrentPage();
        Integer pageSize = searchReq.getPageSize();
        Page page = PageHelper.startPage(currentPage,pageSize);
        page.setOrderBy("alarm_time desc");
        List<FacilitiesAlarm> facilitiesAlarms = manageManualMapper.selectFacilitiesAlarmByMap(queryMap);
        if(StringUtils.isEmpty(facilitiesAlarms)){
            return new PageBean<>();
        }

        //获取单位名称
        Set<Integer> unitIds = new HashSet<>();
        Integer unitId;
        for(FacilitiesAlarm facilitiesAlarm : facilitiesAlarms){
            unitId = facilitiesAlarm.getUnitId();
            if(StringUtils.isNotEmpty(unitId)){
                unitIds.add(unitId);
            }
        }

        List<Unit> units = unitService.selectBatchIds(unitIds);
        Map<Integer,String> unitId2Name = new HashMap<>();
        if(StringUtils.isNotEmpty(units)){
            for(Unit unit :units){
                unitId2Name.put(unit.getId(),unit.getUnitName());
            }
        }

        List<FacilitiesAlarmListResp> facilitiesAlarmListResps = new ArrayList<>();
        FacilitiesAlarmListResp facilitiesAlarmListResp;
        for(FacilitiesAlarm facilitiesAlarm : facilitiesAlarms){
            facilitiesAlarmListResp = new FacilitiesAlarmListResp();
            BeanUtils.copyProperties(facilitiesAlarm,facilitiesAlarmListResp);
            facilitiesAlarmListResp.setUnitName(unitId2Name.get(facilitiesAlarm.getUnitId()));
            facilitiesAlarmListResps.add(facilitiesAlarmListResp);
        }
        PageBean<FacilitiesAlarmListResp> pageBean = new PageBean<>(currentPage,pageSize,(int)page.getTotal(),facilitiesAlarmListResps);
        return pageBean;
    }


    @Override
    public PageBean<FireAlarmListResp> getFireAlarmList(DataScope dataScope, SearchReq searchReq){
        String unitName = searchReq.getUnitName();
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("precinctScope", dataScope);
        if(StringUtils.isNotEmpty(unitName)){
            queryMap.put("unitNameLike",unitName);
            List<Unit> units = unitService.selectByMap(queryMap);
            if(StringUtils.isEmpty(units)){
                return new PageBean<>();
            }
            List<Integer> unitIds = new ArrayList<>();
            for(Unit unit : units){
                unitIds.add(unit.getId());
            }
            queryMap.clear();
            queryMap.put("unitIds",unitIds);
        }

        Date startDate = searchReq.getStartDate();
        Date endDate = searchReq.getEndDate();
        queryMap.put("startDate",startDate);
        queryMap.put("endDate",endDate);
        queryMap.put("misreport", FaMisreportType.EFFECTIVE.id);
        queryMap.put("alarmType",AlarmType.ALARM.id);
        Integer currentPage = searchReq.getCurrentPage();
        Integer pageSize = searchReq.getPageSize();
        Page page = PageHelper.startPage(currentPage,pageSize);
        page.setOrderBy("alarm_time desc");
        List<FacilitiesAlarm> facilitiesAlarms = manageManualMapper.selectFacilitiesAlarmByMap(queryMap);
        if(StringUtils.isEmpty(facilitiesAlarms)){
            return new PageBean<>();
        }

        //获取单位名称
        Set<Integer> unitIds = new HashSet<>();
        Integer unitId;
        for(FacilitiesAlarm facilitiesAlarm : facilitiesAlarms){
            unitId = facilitiesAlarm.getUnitId();
            if(StringUtils.isNotEmpty(unitId)){
                unitIds.add(unitId);
            }
        }

        List<Unit> units = unitService.selectBatchIds(unitIds);
        Map<Integer,String> unitId2Name = new HashMap<>();
        if(StringUtils.isNotEmpty(units)){
            for(Unit unit :units){
                unitId2Name.put(unit.getId(),unit.getUnitName());
            }
        }

        List<FireAlarmListResp> fireAlarmListResps = new ArrayList<>();
        FireAlarmListResp fireAlarmListResp;
        for(FacilitiesAlarm facilitiesAlarm : facilitiesAlarms){
            fireAlarmListResp = new FireAlarmListResp();
            BeanUtils.copyProperties(facilitiesAlarm,fireAlarmListResp);
            fireAlarmListResp.setUnitName(unitId2Name.get(facilitiesAlarm.getUnitId()));
            fireAlarmListResps.add(fireAlarmListResp);
        }
        PageBean<FireAlarmListResp> pageBean = new PageBean<>(currentPage,pageSize,(int)page.getTotal(),fireAlarmListResps);
        return pageBean;
    }

    @Override
    public PageBean<FireAutoAlarmListResp> getFireAutoAlarmList(DataScope dataScope, SearchReq searchReq){
        String unitName = searchReq.getUnitName();
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("precinctScope", dataScope);
        if(StringUtils.isNotEmpty(unitName)){
            queryMap.put("unitNameLike",unitName);
            List<Unit> units = unitService.selectByMap(queryMap);
            if(StringUtils.isEmpty(units)){
                return new PageBean<>();
            }
            List<Integer> unitIds = new ArrayList<>();
            for(Unit unit : units){
                unitIds.add(unit.getId());
            }
            queryMap.clear();
            queryMap.put("unitIds",unitIds);
        }

        queryMap.put("alarmType",AlarmType.ALARM.id);
        Integer currentPage = searchReq.getCurrentPage();
        Integer pageSize = searchReq.getPageSize();
        Page page = PageHelper.startPage(currentPage,pageSize);
        page.setOrderBy("alarm_time desc");
        List<FacilitiesAlarm> facilitiesAlarms = manageManualMapper.selectFacilitiesAlarmByMap(queryMap);
        if(StringUtils.isEmpty(facilitiesAlarms)){
            return new PageBean<>();
        }

        Set<Integer> unitIds = new HashSet<>();
        Set<Integer> facilitiesIds = new HashSet<>();
        Integer faId,unitId;
        for(FacilitiesAlarm facilitiesAlarm : facilitiesAlarms){
            faId = facilitiesAlarm.getFacilitiesId();
            if(StringUtils.isNotEmpty(faId)){
                facilitiesIds.add(faId);
            }
            unitId = facilitiesAlarm.getUnitId();
            if(StringUtils.isNotEmpty(unitId)){
                unitIds.add(unitId);
            }
        }

        //查找设施名称
        Map<Integer,String> faId2Name = new HashMap<>();
        if(StringUtils.isNotEmpty(facilitiesIds)){
           List<Facilities> facilitiess = facilitiesService.selectBatchIds(facilitiesIds);
            if(StringUtils.isNotEmpty(facilitiess)){
                for(Facilities facilities : facilitiess){
                    faId2Name.put(facilities.getId(),facilities.getFaName());
                }
            }
        }

        //查找设施名称
        List<Unit> units = unitService.selectBatchIds(unitIds);
        Map<Integer,String> unitId2Name = new HashMap<>();
        if(StringUtils.isNotEmpty(units)){
            for(Unit unit :units){
                unitId2Name.put(unit.getId(),unit.getUnitName());
            }
        }

        List<FireAutoAlarmListResp> fireAutoAlarmListResps = new ArrayList<>();
        FireAutoAlarmListResp fireAutoAlarmListResp;
        for(FacilitiesAlarm facilitiesAlarm : facilitiesAlarms){
            fireAutoAlarmListResp = new FireAutoAlarmListResp();
            BeanUtils.copyProperties(facilitiesAlarm,fireAutoAlarmListResp);
            fireAutoAlarmListResp.setUnitName(unitId2Name.get(facilitiesAlarm.getUnitId()));
            fireAutoAlarmListResp.setCurStatus(AlarmHandleStatus.getValue(facilitiesAlarm.getHandleStatus()));
            fireAutoAlarmListResp.setMisreport(FaMisreportType.getValue(facilitiesAlarm.getMisreport()));
            fireAutoAlarmListResp.setFacilitiesName(faId2Name.get(facilitiesAlarm.getFacilitiesId()));
            fireAutoAlarmListResp.setAlarmType(AlarmType.ALARM.desc);
            fireAutoAlarmListResps.add(fireAutoAlarmListResp);
        }
        PageBean<FireAutoAlarmListResp> pageBean = new PageBean<>(currentPage,pageSize,(int)page.getTotal(),fireAutoAlarmListResps);
        return pageBean;
    }

    @Override
    public List<FacilitiesAlarm> selectFacilitiesAlarmByMap(Map<String,Object> queryMap){
        return  manageManualMapper.selectFacilitiesAlarmByMap(queryMap);
    }

    @Override
    public AlarmAnalysisResp getAlarmAnalysisByAreaAndScope(Integer areaId,Integer dateType,Integer misreport, DataScope dataScope) {
        AlarmAnalysisResp alarmAnalysisResp = new AlarmAnalysisResp();

        Date now = new Date();
        Date startTime = DateUtil.getStartDate(now);
        Date endTime = now;
        Date yDate = DateUtil.getDateAfterDay(now,-1);
        Date upStartTime = DateUtil.getStartDate(yDate);
        Date upEndTime = DateUtil.getEndDate(yDate);

        if(ReqDateType.WEEK.id.equals(dateType)){
            startTime = DateUtil.getDateAfterDay(now,-7);
            upStartTime = DateUtil.getDateAfterDay(now,-14);
            upEndTime = startTime;
        }else if(ReqDateType.MONTH.id.equals(dateType)){
            startTime = DateUtil.addMonth(now,-1);
            upStartTime = DateUtil.addMonth(now,-2);
            upEndTime = startTime;
        }

        Map<String,Object> queryMap = new HashMap<>();
        BuildDefaultResultUtil.putAreaSearchMap(areaId, queryMap);


        if(StringUtils.isNotNull(dataScope)){
            queryMap.put("precinctIds",dataScope.getDataIds());
        }
        queryMap.put("misreport",misreport);
        queryMap.put("startDate",startTime);
        queryMap.put("endDate",endTime);
        List<Map<String,Object>> alarmResultList = manageManualMapper.countFaTypeNumByMap(queryMap);

        for (Map<String, Object> alarmCountMap : alarmResultList) {
            Object alarmTypeObj = alarmCountMap.get("alarmType");
            if(StringUtils.isNotNull(alarmTypeObj)){
                Integer alramType = Integer.parseInt(alarmTypeObj.toString());
                if(AlarmType.ALARM.id.equals(alramType)){
                    alarmAnalysisResp.getAlarmNum().settAn(Integer.parseInt(alarmCountMap.get("alarmNum").toString()));
                }
                if(AlarmType.EARLY_WARNING.id.equals(alramType)){
                    alarmAnalysisResp.getEarlyWarningNum().settAn(Integer.parseInt(alarmCountMap.get("alarmNum").toString()));
                }
            }
        }

        queryMap.put("startDate",upStartTime);
        queryMap.put("endDate",upEndTime);
        alarmResultList = manageManualMapper.countFaTypeNumByMap(queryMap);

        for (Map<String, Object> alarmCountMap : alarmResultList) {
            Object alarmTypeObj = alarmCountMap.get("alarmType");
            if(StringUtils.isNotNull(alarmTypeObj)){
                Integer alramType = Integer.parseInt(alarmTypeObj.toString());
                if(AlarmType.ALARM.id.equals(alramType)){
                    alarmAnalysisResp.getAlarmNum().setyAn(Integer.parseInt(alarmCountMap.get("alarmNum").toString()));
                }
                if(AlarmType.EARLY_WARNING.id.equals(alramType)){
                    alarmAnalysisResp.getEarlyWarningNum().setyAn(Integer.parseInt(alarmCountMap.get("alarmNum").toString()));
                }
            }
        }

        //设置环比
        AlarmAnalysisResp.NumInfo alarmNumInfo = alarmAnalysisResp.getAlarmNum();
        setRingRatio(alarmNumInfo);
        //设置环比
        AlarmAnalysisResp.NumInfo earlyWarningNumInfo = alarmAnalysisResp.getEarlyWarningNum();
        setRingRatio(earlyWarningNumInfo);

        return alarmAnalysisResp;
    }

    @Override
    public FireAlarmCountResp getFireAlarmCountByArea(Integer areaId) {
        Map<String,Object> queryMap = new HashMap<>();
        BuildDefaultResultUtil.putAreaSearchMap(areaId, queryMap);
        queryMap.put("alarmType",AlarmType.ALARM.id);
        List<Map<String,Object>> alarmResultList = manageManualMapper.countFaRealNumByMap(queryMap);

        FireAlarmCountResp fireAlarmCountResp = new FireAlarmCountResp();
        for (Map<String, Object> alarmCountMap : alarmResultList) {
            Object misreportObj = alarmCountMap.get("misreport");
            if(StringUtils.isNotNull(misreportObj)){
                Integer alramType = Integer.parseInt(misreportObj.toString());
                if(FaMisreportType.MISREPORT.id.equals(alramType)){
                    fireAlarmCountResp.setMisreportNum(Integer.parseInt(alarmCountMap.get("alarmNum").toString()));
                }
                if(FaMisreportType.EFFECTIVE.id.equals(alramType)){
                    fireAlarmCountResp.setEffectiveNum(Integer.parseInt(alarmCountMap.get("alarmNum").toString()));
                }
            }
        }

        return fireAlarmCountResp;
    }

    @Override
    public Map<Integer, Integer> countAlarmNumByUnitId(Integer unitId) {
        Date now = new Date();
        Date startTime = DateUtil.getStartDate(now);
        Date endTime = now;
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("misreport",FaMisreportType.EFFECTIVE.id);
        queryMap.put("startDate",startTime);
        queryMap.put("endDate",endTime);
        queryMap.put("unitId",unitId);
        List<Map<String,Object>> alarmResultList = manageManualMapper.countFaTypeNumByMap(queryMap);

        Map<Integer, Integer> resultMap = new HashMap<>();
        for (Map<String, Object> alarmCountMap : alarmResultList) {
            Object alarmTypeObj = alarmCountMap.get("alarmType");
            if(StringUtils.isNotNull(alarmTypeObj)){
                resultMap.put(Integer.parseInt(alarmTypeObj.toString()),Integer.parseInt(alarmCountMap.get("alarmNum").toString()));
            }
        }
        return resultMap;
    }

    /**
     * 设置环比
     * @param alarmNumInfo
     */
    private void setRingRatio(AlarmAnalysisResp.NumInfo alarmNumInfo) {
        if(alarmNumInfo.getyAn()!=0){
            double ringRatio = BigDecimalUtil.div(BigDecimalUtil.sub(alarmNumInfo.gettAn(),alarmNumInfo.getyAn()),alarmNumInfo.getyAn());
            alarmNumInfo.setRingRatio((int)ringRatio*100);
        }else{
            double ringRatio = BigDecimalUtil.div(BigDecimalUtil.sub(alarmNumInfo.gettAn(),alarmNumInfo.getyAn()),1);
            alarmNumInfo.setRingRatio((int)ringRatio*100);
        }
    }


    @Override
    public AlarmNumResp getAlarmNumByAreaAndScope(Integer areaId, DataScope dataScope) {
        AlarmNumResp alarmNumResp = new AlarmNumResp();

        Map<String,Object> queryMap = new HashMap<>();
        BuildDefaultResultUtil.putAreaSearchMap(areaId, queryMap);

        Date now = new Date();
        Date startTime = DateUtil.getStartDate(now);
        Date endTime = DateUtil.getEndDate(now);
        queryMap.put("precinctScope",dataScope);
        queryMap.put("startDate",startTime);
        queryMap.put("endDate",endTime);
        List<FacilitiesAlarm> facilitiesAlarmList = manageManualMapper.selectFacilitiesAlarmByMap(queryMap);

        AlarmNumResp.AlarmNum alarmNum = alarmNumResp.getAlarmNum();
        AlarmNumResp.AlarmNum earlyWarningNum = alarmNumResp.getEarlyWarningNum();
        for (FacilitiesAlarm facilitiesAlarm : facilitiesAlarmList) {
            Integer alarmType = facilitiesAlarm.getAlarmType();
            Integer alarmHandleStatus = facilitiesAlarm.getHandleStatus();
            if(AlarmHandleStatus.UNTREATED.id.equals(alarmHandleStatus)){
                if(AlarmType.ALARM.id.equals(alarmType)){
                    alarmNum.setUntreated(alarmNum.getUntreated()+1);
                }
                if(AlarmType.EARLY_WARNING.id.equals(alarmType)){
                    earlyWarningNum.setUntreated(earlyWarningNum.getUntreated()+1);
                }
            }else if(AlarmHandleStatus.COMPLETED.id.equals(alarmHandleStatus)){
                if(AlarmType.ALARM.id.equals(alarmType)){
                    alarmNum.setCompleted(alarmNum.getCompleted()+1);
                }
                if(AlarmType.EARLY_WARNING.id.equals(alarmType)){
                    earlyWarningNum.setCompleted(earlyWarningNum.getCompleted()+1);
                }
            }else if(AlarmHandleStatus.UNDER_WAY.id.equals(alarmHandleStatus)){
                if(AlarmType.ALARM.id.equals(alarmType)){
                    alarmNum.setUnderWay(alarmNum.getUnderWay()+1);
                }
                if(AlarmType.EARLY_WARNING.id.equals(alarmType)){
                    earlyWarningNum.setUnderWay(earlyWarningNum.getUnderWay()+1);
                }
            }else if(AlarmHandleStatus.RESPONSE.id.equals(alarmHandleStatus)){
                if(AlarmType.ALARM.id.equals(alarmType)){
                    alarmNum.setResponse(alarmNum.getResponse()+1);
                }
                if(AlarmType.EARLY_WARNING.id.equals(alarmType)){
                    earlyWarningNum.setResponse(earlyWarningNum.getResponse()+1);
                }
            }
        }
        return alarmNumResp;
    }

    @Override
    public List<AlarmIndustryShareResp> getAlarmIndustryShare(Integer areaId,DataScope dataScope, Integer alarmType){
        Date endDate = new Date();
        Date startDate = DateUtil.getDateBeforeMonth(endDate,1);
        Map<String,Object> queryMap = new HashMap<>();
        BuildDefaultResultUtil.putAreaSearchMap(areaId, queryMap);
        queryMap.put("endDate",endDate);
        queryMap.put("startDate",startDate);
        queryMap.put("alarmType",alarmType);
        if(StringUtils.isNotNull(dataScope)){
            queryMap.put("precinctIds",dataScope.getDataIds());
        }

        return manageManualMapper.countAlarmIndustryShare(queryMap);
    }

    @Override
    public List<CurAlarmResp> getCurAlarm(Integer areaId, DataScope dataScope){
        Map<String,Object> queryMap = new HashMap<>();
        BuildDefaultResultUtil.putAreaSearchMap(areaId, queryMap);
        if(StringUtils.isNotNull(dataScope)){
            queryMap.put("precinctIds",dataScope.getDataIds());
        }
        queryMap.put("misreport",FaMisreportType.EFFECTIVE.id);
        queryMap.put("alarmStatus",new DataScope("handle_status", Arrays.asList(AlarmHandleStatus.UNTREATED.id,AlarmHandleStatus.RESPONSE.id,AlarmHandleStatus.UNDER_WAY.id)));
        List<FacilitiesAlarm> facilitiesAlarms = selectFacilitiesAlarmByMap(queryMap);
        List<CurAlarmResp> curAlarmResps = new ArrayList<>();
        if(StringUtils.isEmpty(facilitiesAlarms)){
            return curAlarmResps;
        }

        Set<Integer> unitIds = new HashSet<>();
        for(FacilitiesAlarm facilitiesAlarm :facilitiesAlarms){
            unitIds.add(facilitiesAlarm.getUnitId());
        }
        List<Unit> units = unitService.selectBatchIds(unitIds);
        Map<Integer,String> unitId2Name = new HashMap<>();
        if(StringUtils.isNotEmpty(units)){
            for(Unit unit : units){
                unitId2Name.put(unit.getId(),unit.getUnitName());
            }
        }

        CurAlarmResp curAlarmResp;
        for(FacilitiesAlarm facilitiesAlarm :facilitiesAlarms){
            curAlarmResp = new CurAlarmResp();
            curAlarmResp.setId(facilitiesAlarm.getId());
            curAlarmResp.setAlarmContent(facilitiesAlarm.getAlarmContent());
            curAlarmResp.setAlarmStatus(facilitiesAlarm.getAlarmStatus());
            curAlarmResp.setAlarmTime(facilitiesAlarm.getAlarmTime());
            curAlarmResp.setUnitName(unitId2Name.get(facilitiesAlarm.getUnitId()));
            curAlarmResps.add(curAlarmResp);
        }
        return curAlarmResps;
    }

    @Override
    public List<AlarmStatisticsResp> getAlarmTrend(Integer areaId, DataScope dataScope, Integer alarmType){
        Date endDate = new Date();
        Date startDate = DateUtil.getDateBeforeMonth(endDate,1);
        Map<String,Object> queryMap = new HashMap<>();
        BuildDefaultResultUtil.putAreaSearchMap(areaId, queryMap);
        queryMap.put("endDate",endDate);
        queryMap.put("startDate",startDate);
        queryMap.put("alarmType",alarmType);
        if(StringUtils.isNotNull(dataScope)){
            queryMap.put("precinctIds",dataScope.getDataIds());
        }

        List<FacilitiesAlarm> facilitiesAlarms = selectFacilitiesAlarmByMap(queryMap);
        List<AlarmStatisticsResp> alarmStatisticsResps = new ArrayList<>();
        if(StringUtils.isEmpty(facilitiesAlarms)){
            return alarmStatisticsResps;
        }

        Map<String,Integer> date2AlarmNum = new HashMap<>();
        String day;
        for(FacilitiesAlarm facilitiesAlarm : facilitiesAlarms){
            day = DateUtil.toString(facilitiesAlarm.getAlarmTime(),DateUtil.DATE_PATTERN);
            int alarmNum = date2AlarmNum.containsKey(day)?date2AlarmNum.get(day):0;
            alarmNum += 1;
            date2AlarmNum.put(day,alarmNum);
        }

        List<String> dates = DateUtil.getDaysBetween(startDate,endDate);
        AlarmStatisticsResp alarmStatisticsResp;
        for(String date: dates){
            alarmStatisticsResp = new AlarmStatisticsResp();
            alarmStatisticsResp.setDate(date);
            int alarmNum = StringUtils.isNotEmpty(date2AlarmNum.get(date))?date2AlarmNum.get(date):0;
            alarmStatisticsResp.setAlarmNum(alarmNum);
            alarmStatisticsResps.add(alarmStatisticsResp);
        }
        return alarmStatisticsResps;
    }

}
