package com.mailian.firecontrol.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.bean.PageBean;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.AlarmHandleStatus;
import com.mailian.firecontrol.common.enums.AlarmType;
import com.mailian.firecontrol.common.enums.FaMisreportType;
import com.mailian.firecontrol.dao.auto.mapper.FacilitiesAlarmMapper;
import com.mailian.firecontrol.dao.auto.model.Facilities;
import com.mailian.firecontrol.dao.auto.model.FacilitiesAlarm;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dao.manual.mapper.ManageManualMapper;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.AlarmNumResp;
import com.mailian.firecontrol.dto.web.response.FacilitiesAlarmListResp;
import com.mailian.firecontrol.dto.web.response.FireAlarmListResp;
import com.mailian.firecontrol.dto.web.response.FireAutoAlarmListResp;
import com.mailian.firecontrol.service.FacilitiesAlarmService;
import com.mailian.firecontrol.service.FacilitiesService;
import com.mailian.firecontrol.service.UnitService;
import com.mailian.firecontrol.service.repository.DeviceItemRepository;
import com.mailian.firecontrol.service.util.BuildDefaultResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    private DeviceItemRepository deviceItemRepository;
    @Resource
    private FacilitiesService facilitiesService;

    @Override
    public PageBean<FacilitiesAlarmListResp> getFacilitiesAlarmList(DataScope dataScope, SearchReq searchReq){
        String unitName = searchReq.getUnitName();
        Map<String,Object> queryMap = new HashMap<>();
        //查找单位id列表
        queryMap.put("precinctScope", dataScope);
        if(StringUtils.isNotEmpty(unitName)){
            queryMap.put("unitNameLike",unitName);
        }
        List<Unit> units = unitService.selectByMap(queryMap);
        if(StringUtils.isEmpty(units)){
            return new PageBean<>();
        }

        //查找单位信息
        List<Integer> unitIds = new ArrayList<>();
        Map<Integer,String> unitId2Name = new HashMap<>();
        for(Unit unit : units){
            unitIds.add(unit.getId());
            unitId2Name.put(unit.getId(),unit.getUnitName());
        }

        queryMap.clear();
        queryMap.put("unitIds",unitIds);
        Integer currentPage = searchReq.getCurrentPage();
        Integer pageSize = searchReq.getPageSize();
        Page page = PageHelper.startPage(currentPage,pageSize);
        page.setOrderBy("alarm_time desc");
        List<FacilitiesAlarm> facilitiesAlarms = manageManualMapper.selectFacilitiesAlarmByMap(queryMap);
        if(StringUtils.isEmpty(facilitiesAlarms)){
            return new PageBean<>();
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
        Map<String,Object> queryMap = new HashMap<>();
        String unitName = searchReq.getUnitName();
        queryMap.put("precinctScope", dataScope);
        if(StringUtils.isNotEmpty(unitName)){
            queryMap.put("unitNameLike",unitName);
        }
        List<Unit> units = unitService.selectByMap(queryMap);
        if(StringUtils.isEmpty(units)){
            return new PageBean<>();
        }

        //查找单位信息
        List<Integer> unitIds = new ArrayList<>();
        Map<Integer,String> unitId2Name = new HashMap<>();
        for(Unit unit : units){
            unitIds.add(unit.getId());
            unitId2Name.put(unit.getId(),unit.getUnitName());
        }

        Date startDate = searchReq.getStartDate();
        Date endDate = searchReq.getEndDate();
        queryMap.clear();
        queryMap.put("unitIds",unitIds);
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
        Map<String,Object> queryMap = new HashMap<>();
        String unitName = searchReq.getUnitName();
        queryMap.put("precinctScope", dataScope);
        if(StringUtils.isNotEmpty(unitName)){
            queryMap.put("unitNameLike",unitName);
        }
        List<Unit> units = unitService.selectByMap(queryMap);
        if(StringUtils.isEmpty(units)){
            return new PageBean<>();
        }

        //查找单位信息
        List<Integer> unitIds = new ArrayList<>();
        Map<Integer,String> unitId2Name = new HashMap<>();
        for(Unit unit : units){
            unitIds.add(unit.getId());
            unitId2Name.put(unit.getId(),unit.getUnitName());
        }

        queryMap.clear();
        queryMap.put("unitIds",unitIds);
        queryMap.put("alarmType",AlarmType.ALARM.id);
        Integer currentPage = searchReq.getCurrentPage();
        Integer pageSize = searchReq.getPageSize();
        Page page = PageHelper.startPage(currentPage,pageSize);
        page.setOrderBy("alarm_time desc");
        List<FacilitiesAlarm> facilitiesAlarms = manageManualMapper.selectFacilitiesAlarmByMap(queryMap);
        if(StringUtils.isEmpty(facilitiesAlarms)){
            return new PageBean<>();
        }

        Set<Integer> facilitiesIds = new HashSet<>();
        for(FacilitiesAlarm facilitiesAlarm : facilitiesAlarms){
            Integer faId = facilitiesAlarm.getFacilitiesId();
            if(StringUtils.isNotEmpty(faId)){
                facilitiesIds.add(faId);
            }
        }

        //查找设施
        Map<Integer,String> faId2Name = new HashMap<>();
        if(StringUtils.isNotEmpty(facilitiesIds)){
           List<Facilities> facilitiess = facilitiesService.selectBatchIds(facilitiesIds);
            if(StringUtils.isNotEmpty(facilitiess)){
                for(Facilities facilities : facilitiess){
                    faId2Name.put(facilities.getId(),facilities.getFaName());
                }
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
    public AlarmNumResp getAlarmNumByAreaAndScope(Integer areaId, DataScope dataScope) {
        AlarmNumResp alarmNumResp = new AlarmNumResp();

        Map<String,Object> queryMap = new HashMap<>();
        BuildDefaultResultUtil.putAreaSearchMap(areaId, queryMap);
        queryMap.put("precinctScope",dataScope);
        List<FacilitiesAlarm> facilitiesAlarmList = selectByMap(queryMap);

        AlarmNumResp.AlarmNum alarmNum = alarmNumResp.getAlarmNum();
        AlarmNumResp.AlarmNum earlyWarningNum = alarmNumResp.getEarlyWarningNum();
        for (FacilitiesAlarm facilitiesAlarm : facilitiesAlarmList) {
            Integer alarmType = facilitiesAlarm.getAlarmType();
            Integer alarmHandleStatus = facilitiesAlarm.getHandleStatus();
            if(AlarmHandleStatus.UNTREATED.id.equals(alarmHandleStatus)){
                if(AlarmType.ALARM.id.equals(alarmType)){
                    alarmNum.setUntreated(alarmNum.getUntreated()+1);
                }else{
                    earlyWarningNum.setUntreated(earlyWarningNum.getUntreated()+1);
                }
            }else if(AlarmHandleStatus.COMPLETED.id.equals(alarmHandleStatus)){
                if(AlarmType.ALARM.id.equals(alarmType)){
                    alarmNum.setCompleted(alarmNum.getCompleted()+1);
                }else{
                    earlyWarningNum.setCompleted(earlyWarningNum.getCompleted()+1);
                }
            }else{
                if(AlarmType.ALARM.id.equals(alarmType)){
                    alarmNum.setUnderWay(alarmNum.getUnderWay()+1);
                }else{
                    earlyWarningNum.setUnderWay(earlyWarningNum.getUnderWay()+1);
                }
            }
        }
        return alarmNumResp;
    }

}
