package com.mailian.firecontrol.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.bean.BasePage;
import com.mailian.core.bean.PageBean;
import com.mailian.core.db.DataScope;
import com.mailian.core.enums.BooleanEnum;
import com.mailian.core.enums.ResponseCode;
import com.mailian.core.enums.Status;
import com.mailian.core.exception.RequestException;
import com.mailian.core.util.BigDecimalUtil;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.*;
import com.mailian.firecontrol.dao.auto.mapper.FacilitiesAlarmMapper;
import com.mailian.firecontrol.dao.auto.mapper.PrecinctMapper;
import com.mailian.firecontrol.dao.auto.mapper.UnitMapper;
import com.mailian.firecontrol.dao.auto.model.*;
import com.mailian.firecontrol.dao.manual.mapper.ManageManualMapper;
import com.mailian.firecontrol.dao.manual.mapper.UnitManualMapper;
import com.mailian.firecontrol.dto.DiagramItemDto;
import com.mailian.firecontrol.dto.SelectDto;
import com.mailian.firecontrol.dto.app.response.AppUnitDetailResp;
import com.mailian.firecontrol.dto.app.response.AppUnitResp;
import com.mailian.firecontrol.dto.app.response.SwitchResp;
import com.mailian.firecontrol.dto.push.Device;
import com.mailian.firecontrol.dto.web.UnitInfo;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.*;
import com.mailian.firecontrol.service.AreaService;
import com.mailian.firecontrol.service.DeviceItemOpertionService;
import com.mailian.firecontrol.service.UnitDeviceService;
import com.mailian.firecontrol.service.UnitService;
import com.mailian.firecontrol.service.cache.DeviceCache;
import com.mailian.firecontrol.service.cache.UnitDeviceCache;
import com.mailian.firecontrol.service.repository.DeviceRepository;
import com.mailian.firecontrol.service.util.BuildDefaultResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UnitServiceImpl extends BaseServiceImpl<Unit, UnitMapper> implements UnitService {
    @Resource
    private PrecinctMapper precinctMapper;
    @Autowired
    private AreaService areaService;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceCache deviceCache;
    @Resource
    private UnitManualMapper unitManualMapper;
    @Autowired
    private UnitDeviceService unitDeviceService;
    @Autowired
    private UnitDeviceCache unitDeviceCache;
    @Resource
    private FacilitiesAlarmMapper facilitiesAlarmMapper;
    @Resource
    private ManageManualMapper manageManualMapper;
    @Autowired
    private DeviceItemOpertionService deviceItemOpertionService;

    @Override
    public PageBean<UnitListResp> getUnitList(DataScope dataScope,SearchReq searchReq) {
        Integer currentPage = searchReq.getCurrentPage();
        Integer pageSize = searchReq.getPageSize();
        String unitName = searchReq.getUnitName();
        Page page = PageHelper.startPage(currentPage,pageSize);
        page.setOrderBy("update_time desc");
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("precinctScope", dataScope);
        if(StringUtils.isNotEmpty(unitName)){
            queryMap.put("unitNameLike",unitName);
        }
        List<Unit> units = super.selectByMap(queryMap);
        if(StringUtils.isEmpty(units)){
            return new PageBean<>();
        }

        //查找管辖区名称
        Set<Integer> precinctIds = new HashSet<>();
        //查找地址信息
        Set<Integer> areaIds = new HashSet<>();
        for(Unit unit:units){
            Integer areaId = unit.getAreaId();
            Integer provinceId = unit.getProvinceId();
            Integer cityId = unit.getCityId();
            if(StringUtils.isNotEmpty(areaId)){
                areaIds.add(areaId);
            }
            if(StringUtils.isNotEmpty(provinceId)){
                areaIds.add(provinceId);
            }
            if(StringUtils.isNotEmpty(cityId)){
                areaIds.add(cityId);
            }
            precinctIds.add(unit.getPrecinctId());
        }
        List<Precinct> precincts = precinctMapper.selectBatchIds(precinctIds);
        Map<Integer,String> precinetId2Name = new HashMap<>();
        for(Precinct precinct : precincts){
            precinetId2Name.put(precinct.getId(),precinct.getPrecinctName());
        }
        List<Area> areas = areaService.selectBatchIds(areaIds);
        Map<Integer,String> areaId2Name = new HashMap<>();
        for(Area area : areas){
            areaId2Name.put(area.getId(),area.getAreaName());
        }

        List<UnitListResp> unitListResps = new ArrayList<>();
        UnitListResp unitListResp;
        StringBuffer areaInfo = new StringBuffer();
        Integer areaId = 0 ,provinceId = 0,cityId = 0 ;
        for(Unit unit:units){
            unitListResp = new UnitListResp();
            BeanUtils.copyProperties(unit,unitListResp);
            unitListResp.setPrecinct(precinetId2Name.get(unit.getPrecinctId()));

            areaId = unit.getAreaId();
            provinceId = unit.getProvinceId();
            cityId = unit.getCityId();
            areaInfo.setLength(0);
            if(StringUtils.isNotEmpty(provinceId) && areaId2Name.containsKey(provinceId)){
                areaInfo.append(areaId2Name.get(provinceId));
            }
            if(StringUtils.isNotEmpty(cityId) && areaId2Name.containsKey(cityId)){
                areaInfo.append(areaId2Name.get(cityId));
            }
            if(StringUtils.isNotEmpty(areaId) && areaId2Name.containsKey(areaId)){
                areaInfo.append(areaId2Name.get(areaId));
            }
            unitListResp.setAreaInfo(areaInfo.toString());
            unitListResp.setUnitTypeDesc(UnitType.getValue(unit.getUnitType()));
            unitListResp.setSuperviseLevelDesc(UnitSuperviseLevel.getValue(unit.getSuperviseLevel()));

            Integer onlineStatus =  unitDeviceCache.getUnitOnlineStatus(unit.getId().toString());
            if(StringUtils.isNotNull(onlineStatus) && Status.NORMAL.id.equals(onlineStatus)){
                unitListResp.setOnlineStatus(BooleanEnum.YES.id);
            }
            unitListResps.add(unitListResp);
        }

        PageBean<UnitListResp> pageBean = new PageBean<>(currentPage,pageSize,(int)page.getTotal(),unitListResps);
        return pageBean;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean insertOrUpdate(UnitInfo unitInfo){
        Unit unit = new Unit();
        BeanUtils.copyProperties(unitInfo,unit);

        List<String> deviceIds = unitInfo.getDeviceIds();
        if(StringUtils.isEmpty(unit.getId())){
            unit.setStatus(Status.NORMAL.id);

            int result = super.insert(unit);
            if(result>0) {
                Integer unitId = unit.getId();
                addUnitDevices(unitId,deviceIds);
            }
            return result > 0;
        }else {
            updateUnitDevice(unitInfo,unitInfo.getId());

            return super.updateByPrimaryKeySelective(unit) > 0;
        }
    }


    /**
     * 更新单位网关关系
     * @param unitInfo
     * @param unitId
     */
    private void updateUnitDevice(UnitInfo unitInfo, Integer unitId) {
        List<String> deviceIds = unitInfo.getDeviceIds();
        if(StringUtils.isEmpty(deviceIds)){
            unitManualMapper.deleteDeviceByUnitId(unitId);
        }else{
            Map<String,Object> map = new HashMap<>();
            map.put("unitId",unitId);
            List<UnitDevice> unitDeviceList = unitDeviceService.selectByMap(map);

            String oldDeviceId;
            List<Integer> delUnitDeviceIds = new ArrayList<>();
            List<String> removeDeviceIds = new ArrayList<>();
            for (UnitDevice unitDevice : unitDeviceList) {
                oldDeviceId = unitDevice.getDeviceId();
                boolean result = deviceIds.remove(oldDeviceId);
                if(!result){
                    delUnitDeviceIds.add(unitDevice.getId());
                    removeDeviceIds.add(unitDevice.getDeviceId());
                }
            }
            if(StringUtils.isNotEmpty(delUnitDeviceIds)){
                unitDeviceService.deleteBatchIds(delUnitDeviceIds);
                unitDeviceCache.removeUnitDevice(removeDeviceIds.toArray(new String[]{}));
            }
            addUnitDevices(unitId,deviceIds);
        }

    }

    /**
     * 添加单位网关关系
     * @param unitId
     * @param deviceIds
     */
    private void addUnitDevices(Integer unitId, List<String> deviceIds) {
        if(StringUtils.isEmpty(deviceIds)){
            return;
        }
        List<UnitDevice> unitDevicesDb = unitDeviceService.selectByDeviceIds(deviceIds);
        List<UnitDevice> upList = new ArrayList<>();
        for (UnitDevice unitDevice : unitDevicesDb) {
            if(deviceIds.contains(unitDevice.getDeviceId())){
                if(unitDevice.getUnitId().equals(unitId)) {
                    UnitDevice upUnitDevice = new UnitDevice();
                    upUnitDevice.setId(unitDevice.getId());
                    upUnitDevice.setUnitId(unitId);
                    setUserNameAndTime(upUnitDevice, false);
                    upList.add(upUnitDevice);
                }
                deviceIds.remove(unitDevice.getDeviceId());
            }
        }

        UnitDevice unitDevice;
        List<UnitDevice> unitDeviceList = new ArrayList<>();
        for (String deviceId : deviceIds) {
            unitDevice = new UnitDevice();
            unitDevice.setUnitId(unitId);
            unitDevice.setDeviceId(deviceId);
            setUserNameAndTime(unitDevice,true);
            unitDeviceList.add(unitDevice);
        }
        unitDeviceService.insertBatch(unitDeviceList);
        if(StringUtils.isNotEmpty(upList)){
            unitDeviceService.updateByList(upList);
        }

        unitDeviceList.addAll(upList);
        unitDeviceCache.addOrUpdateUnitDevice(unitDeviceList);
    }


    @Override
    public List<DeviceResp> getUnallotDevice(Integer unitId) {
        List<DeviceResp> deviceRespList = new ArrayList<>();
        List<Device> devices = deviceRepository.getDevicesByCodes(null);
        if(StringUtils.isEmpty(devices)){
            return deviceRespList;
        }

        Map<String,Device> deviceMap = deviceCache.addDevices(devices);
        List<String> deviceIds = unitManualMapper.selectDevicesExcludeUnitId(unitId);

        if(StringUtils.isNotNull(deviceIds)) {
            for (String deviceId : deviceIds) {
                deviceMap.remove(deviceId);
            }
        }

        if(StringUtils.isNotEmpty(deviceMap)){
            DeviceResp deviceResp;
            for (Map.Entry<String, Device> deviceEntry : deviceMap.entrySet()) {
                deviceResp = new DeviceResp();
                BeanUtils.copyProperties(deviceEntry.getValue(),deviceResp);
                deviceRespList.add(deviceResp);
            }
        }
        return deviceRespList;
    }

    @Override
    public PieResp getUnitSpreadByAreaAndScope(Integer areaId,DataScope dataScope) {
        Map<String,Object> queryMap = new HashMap<>();
        BuildDefaultResultUtil.putAreaSearchMap(areaId, queryMap);

        queryMap.put("precinctScope",dataScope);
        List<Unit> unitList = selectByMap(queryMap);

        Map<Integer,PieData> pieDataMap = new HashMap<>();
        PieResp pieResp = BuildDefaultResultUtil.buildDefaultPieResp(pieDataMap);

        for (Unit unit : unitList) {
            Integer unitType = unit.getUnitType();
            if(pieDataMap.containsKey(unitType)){
                PieData pieData = pieDataMap.get(unitType);
                pieData.setValue(pieData.getValue()+1);
            }
        }

        pieResp.setTotal(unitList.size());
        return pieResp;
    }

    @Override
    public AreaUnitMapResp getUnitMapDataByAreaAndScope(Integer areaId, Integer unitType, DataScope dataScope) {
        Map<String,Object> queryMap = new HashMap<>();
        BuildDefaultResultUtil.putAreaSearchMap(areaId, queryMap);

        queryMap.put("unitType",unitType);
        queryMap.put("precinctScope",dataScope);
        List<Unit> unitList = selectByMap(queryMap);

        AreaUnitMapResp areaUnitMapResp = new AreaUnitMapResp();
        AreaUnitMapResp.CountDataInfo countDataInfo = areaUnitMapResp.getCountDataInfo();
        List<UnitInfo> unitInfos = new ArrayList<>();
        List<Integer> unitIdList = new ArrayList<>();
        Integer onlineCount = 0;
        for (Unit unit : unitList) {
            UnitInfo unitInfo = new UnitInfo();
            BeanUtils.copyProperties(unit,unitInfo);
            unitInfos.add(unitInfo);
            unitIdList.add(unit.getId());

            Integer onlineStatus =  unitDeviceCache.getUnitOnlineStatus(unit.getId().toString());
            if(StringUtils.isNotNull(onlineStatus) && Status.NORMAL.id.equals(onlineStatus)){
                onlineCount++;
            }
        }
        areaUnitMapResp.setUnitInfos(unitInfos);

        queryMap.clear();
        queryMap.put("unitScope",new DataScope("unit_id",unitIdList));
        List<FacilitiesAlarm> facilitiesAlarmList = facilitiesAlarmMapper.selectByMap(queryMap);
        Integer alarmCount = 0;
        Integer earlyWarningCount = 0;
        for (FacilitiesAlarm facilitiesAlarm : facilitiesAlarmList) {
            Integer alarmType = facilitiesAlarm.getAlarmType();
            if(StringUtils.isNotNull(alarmType) && AlarmType.ALARM.id.equals(alarmType)){
                alarmCount++;
            }
            if(StringUtils.isNotNull(alarmType) && AlarmType.EARLY_WARNING.id.equals(alarmType)){
                earlyWarningCount++;
            }
        }

        int unitCount = unitList.size();
        countDataInfo.setAlarmCount(alarmCount);
        countDataInfo.setEarlyWarningCount(earlyWarningCount);
        countDataInfo.setOnlineRate(0);
        if(unitCount != 0) {
            countDataInfo.setOnlineRate((int) BigDecimalUtil.mul(BigDecimalUtil.div(onlineCount, unitCount,2),100));
        }
        countDataInfo.setUnitCount(unitCount);
        return areaUnitMapResp;
    }

    @Override
    public List<AppUnitResp> selectByNameAndPageScope(String name, BasePage basePage,DataScope dataScope) {
        PageHelper.startPage(basePage.getCurrentPage(),basePage.getPageSize());

        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("unitNameLike",name);
        queryMap.put("precinctScope",dataScope);

        List<Unit> unitList = selectByMap(queryMap);

        List<AppUnitResp> unitRespList = new ArrayList<>();
        for (Unit unit : unitList) {
            AppUnitResp appUnitResp = new AppUnitResp();
            appUnitResp.setUnitId(unit.getId());
            appUnitResp.setUnitName(unit.getUnitName());
            unitRespList.add(appUnitResp);
        }
        return unitRespList;
    }

    @Override
    public AppUnitDetailResp getAppUnitDetailById(Integer unitId) {
        Unit unit = selectByPrimaryKey(unitId);
        if(StringUtils.isNull(unit)){
            throw new RequestException(ResponseCode.FAIL.code,"单位不存在");
        }

        AppUnitDetailResp appUnitDetailResp = new AppUnitDetailResp();

        appUnitDetailResp.setUnitId(unitId);
        appUnitDetailResp.setUnitName(unit.getUnitName());
        appUnitDetailResp.setTransactor(unit.getTransactor());
        appUnitDetailResp.setContactPhone(unit.getContactPhone());
        appUnitDetailResp.setAddress(unit.getAddress());
        appUnitDetailResp.setBusinessScope(unit.getBusinessScope());
        appUnitDetailResp.setItems(unit.getItems());
        appUnitDetailResp.setJoinTime(unit.getJoinTime());

        /*获取管辖区信息*/
        Precinct precinct = precinctMapper.selectByPrimaryKey(unit.getPrecinctId());
        if(StringUtils.isNotNull(precinct)){
            appUnitDetailResp.setPrecinctName(precinct.getPrecinctName());
            appUnitDetailResp.setDutyName(precinct.getDutyName());
            appUnitDetailResp.setDutyPhone(precinct.getDutyPhone());
        }

        /*获取当前烟感、漏电状态*/
        int somkeAlarmCount = manageManualMapper.countUnfinishAlarmNumByType(AlarmType.ALARM.id);
        int leakageAlarmCount = manageManualMapper.countUnfinishAlarmNumByType(AlarmType.EARLY_WARNING.id);
        appUnitDetailResp.setSomkeStatus(Status.NORMAL.id);
        if(somkeAlarmCount>0){
            appUnitDetailResp.setSomkeStatus(Status.DISABLE.id);
        }

        appUnitDetailResp.setLeakageStatus(Status.NORMAL.id);
        if(leakageAlarmCount>0){
            appUnitDetailResp.setLeakageStatus(Status.DISABLE.id);
        }

        /*设置开关状态*/
        //找到对应遥控数据项
        List<DiagramItemDto> diagramItems = manageManualMapper.selectDiagramItemByUnitIdAndType(unitId,StructType.REMOTE.id);
        Map<Integer,Map<Integer,DiagramItemDto>> dsIdItemMap = new HashMap<>();
        for (DiagramItemDto diagramItem : diagramItems) {
            if(dsIdItemMap.containsKey(diagramItem.getDsId())){
                dsIdItemMap.get(diagramItem.getDsId()).put(diagramItem.getItemType(),diagramItem);
            }else{
                Map<Integer,DiagramItemDto> typeItemMap = new HashMap<>();
                typeItemMap.put(diagramItem.getItemType(),diagramItem);
                dsIdItemMap.put(diagramItem.getDsId(),typeItemMap);
            }
        }

        if(StringUtils.isNotEmpty(dsIdItemMap)){
            List<SwitchResp> switchResps = new ArrayList<>();
            for (Map<Integer, DiagramItemDto> typeItemMap : dsIdItemMap.values()) {
                DiagramItemDto ykItem = typeItemMap.get(DiaItemType.TELECONTROL.id);
                DiagramItemDto switchItem = typeItemMap.get(DiaItemType.ALARM.id);

                if(StringUtils.isNotNull(ykItem) && StringUtils.isNotNull(switchItem)) {
                    String status = deviceItemOpertionService.getYaoceStatus(ykItem.getItemId(),switchItem.getItemId());
                    List<SelectDto> selectDtos = deviceItemOpertionService.getYaokongEnumList(ykItem.getItemId());

                    SwitchResp switchResp = new SwitchResp();
                    switchResp.setSelects(selectDtos);
                    switchResp.setStatus(status);
                    switchResp.setSwitchName(ykItem.getStructName());
                    switchResps.add(switchResp);
                }
            }

            appUnitDetailResp.setSwitchResps(switchResps);
        }


        /*设置电压电流等数据项*/
        return appUnitDetailResp;
    }

}
