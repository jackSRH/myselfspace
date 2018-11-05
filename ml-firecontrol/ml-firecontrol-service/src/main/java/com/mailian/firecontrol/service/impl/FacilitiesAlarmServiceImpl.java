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
import com.mailian.firecontrol.common.enums.ItemBtype;
import com.mailian.firecontrol.dao.auto.mapper.FacilitiesAlarmMapper;
import com.mailian.firecontrol.dao.auto.model.Facilities;
import com.mailian.firecontrol.dao.auto.model.FacilitiesAlarm;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dao.manual.ManageManualMapper;
import com.mailian.firecontrol.dto.push.DeviceItem;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.FacilitiesAlarmListResp;
import com.mailian.firecontrol.dto.web.response.FireAlarmListResp;
import com.mailian.firecontrol.dto.web.response.FireAutoAlarmListResp;
import com.mailian.firecontrol.service.FacilitiesAlarmService;
import com.mailian.firecontrol.service.FacilitiesService;
import com.mailian.firecontrol.service.UnitService;
import com.mailian.firecontrol.service.repository.DeviceItemRepository;
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
        Integer currentPage = searchReq.getCurrentPage();
        Integer pageSize = searchReq.getPageSize();
        Page page = PageHelper.startPage(currentPage,pageSize);
        page.setOrderBy("alarm_time desc");
        List<FacilitiesAlarm> facilitiesAlarms = manageManualMapper.selectFacilitiesAlarmByMap(queryMap);
        if(StringUtils.isEmpty(facilitiesAlarms)){
            return new PageBean<>();
        }


        Set<String> itemIds =new HashSet<>();
        for(FacilitiesAlarm facilitiesAlarm : facilitiesAlarms){
            itemIds.add(facilitiesAlarm.getAlarmItemId());
        }
        Map<String, DeviceItem> itemId2Item = deviceItemRepository.getDeviceItemInfosByItemIds(new ArrayList<>(itemIds));
        Set<String> fireItemIds = new HashSet<>();
        if(StringUtils.isNotNull(itemId2Item)){
            for(Map.Entry<String,DeviceItem> entry :itemId2Item.entrySet()){
                if(ItemBtype.SMOKE.id.intValue() == entry.getValue().getBtype().intValue()){
                    fireItemIds.add(entry.getKey());
                }
            }
        }

        List<FireAlarmListResp> fireAlarmListResps = new ArrayList<>();
        FireAlarmListResp fireAlarmListResp;
        for(FacilitiesAlarm facilitiesAlarm : facilitiesAlarms){
            //不是烟感告警的排除掉
            if(!fireItemIds.contains(facilitiesAlarm.getAlarmItemId())){
                continue;
            }
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
        Integer currentPage = searchReq.getCurrentPage();
        Integer pageSize = searchReq.getPageSize();
        Page page = PageHelper.startPage(currentPage,pageSize);
        page.setOrderBy("alarm_time desc");
        List<FacilitiesAlarm> facilitiesAlarms = manageManualMapper.selectFacilitiesAlarmByMap(queryMap);
        if(StringUtils.isEmpty(facilitiesAlarms)){
            return new PageBean<>();
        }


        Set<String> itemIds =new HashSet<>();
        Set<Integer> facilitiesIds = new HashSet<>();
        for(FacilitiesAlarm facilitiesAlarm : facilitiesAlarms){
            itemIds.add(facilitiesAlarm.getAlarmItemId());
            Integer faId = facilitiesAlarm.getFacilitiesId();
            if(StringUtils.isNotEmpty(faId)){
                facilitiesIds.add(faId);
            }
        }
        Map<String, DeviceItem> itemId2Item = deviceItemRepository.getDeviceItemInfosByItemIds(new ArrayList<>(itemIds));
        Set<String> fireItemIds = new HashSet<>();
        if(StringUtils.isNotEmpty(itemId2Item)){
            for(Map.Entry<String,DeviceItem> entry :itemId2Item.entrySet()){
                if(ItemBtype.SMOKE.id.intValue() == entry.getValue().getBtype().intValue()){
                    fireItemIds.add(entry.getKey());
                }
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
            //不是烟感告警的排除掉
            if(!fireItemIds.contains(facilitiesAlarm.getAlarmItemId())){
                continue;
            }
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

}
