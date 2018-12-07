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
import com.mailian.core.util.DateUtil;
import com.mailian.core.util.PageUtil;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.common.enums.*;
import com.mailian.firecontrol.dao.auto.mapper.PrecinctMapper;
import com.mailian.firecontrol.dao.auto.mapper.UnitMapper;
import com.mailian.firecontrol.dao.auto.model.*;
import com.mailian.firecontrol.dao.manual.mapper.ManageManualMapper;
import com.mailian.firecontrol.dao.manual.mapper.UnitManualMapper;
import com.mailian.firecontrol.dto.CountDataInfo;
import com.mailian.firecontrol.dto.DiagramItemDto;
import com.mailian.firecontrol.dto.SelectDto;
import com.mailian.firecontrol.dto.app.response.AppUnitDetailResp;
import com.mailian.firecontrol.dto.app.response.AppUnitResp;
import com.mailian.firecontrol.dto.app.response.ItemDataResp;
import com.mailian.firecontrol.dto.app.response.SwitchResp;
import com.mailian.firecontrol.dto.push.Device;
import com.mailian.firecontrol.dto.push.DeviceItem;
import com.mailian.firecontrol.dto.push.DeviceItemRealTimeData;
import com.mailian.firecontrol.dto.web.UnitInfo;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.*;
import com.mailian.firecontrol.service.AreaService;
import com.mailian.firecontrol.service.DeviceItemOpertionService;
import com.mailian.firecontrol.service.UnitDeviceService;
import com.mailian.firecontrol.service.UnitService;
import com.mailian.firecontrol.service.cache.DeviceCache;
import com.mailian.firecontrol.service.cache.UnitDeviceCache;
import com.mailian.firecontrol.service.repository.DeviceItemRepository;
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
    private ManageManualMapper manageManualMapper;
    @Autowired
    private DeviceItemOpertionService deviceItemOpertionService;
    @Autowired
    private DeviceItemRepository deviceItemRepository;

    @Override
    public PageBean<UnitListResp> getUnitList(DataScope dataScope, SearchReq searchReq) {
        Integer currentPage = searchReq.getCurrentPage();
        Integer pageSize = searchReq.getPageSize();
        String unitName = searchReq.getUnitName();
        List<Unit> units;
        int total = 0;
        if (StringUtils.isEmpty(searchReq.getUnitId())) {
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("precinctScope", dataScope);
            if (StringUtils.isNotEmpty(unitName)) {
                queryMap.put("unitNameLike", unitName);
            }
            Page page = PageHelper.startPage(currentPage, pageSize);
            page.setOrderBy("update_time desc");
            units = super.selectByMap(queryMap);
            total = (int) page.getTotal();
        } else {
            units = new ArrayList<>();
            units.add(selectByPrimaryKey(searchReq.getUnitId()));
            total = 1;
        }
        if (StringUtils.isEmpty(units)) {
            return new PageBean<>();
        }

        //查找管辖区名称
        Set<Integer> precinctIds = new HashSet<>();
        //查找地址信息
        Set<Integer> areaIds = new HashSet<>();
        for (Unit unit : units) {
            Integer areaId = unit.getAreaId();
            Integer provinceId = unit.getProvinceId();
            Integer cityId = unit.getCityId();
            if (StringUtils.isNotEmpty(areaId)) {
                areaIds.add(areaId);
            }
            if (StringUtils.isNotEmpty(provinceId)) {
                areaIds.add(provinceId);
            }
            if (StringUtils.isNotEmpty(cityId)) {
                areaIds.add(cityId);
            }
            precinctIds.add(unit.getPrecinctId());
        }
        List<Precinct> precincts = precinctMapper.selectBatchIds(precinctIds);
        Map<Integer, String> precinetId2Name = new HashMap<>();
        for (Precinct precinct : precincts) {
            precinetId2Name.put(precinct.getId(), precinct.getPrecinctName());
        }
        List<Area> areas = areaService.selectBatchIds(areaIds);
        Map<Integer, String> areaId2Name = new HashMap<>();
        for (Area area : areas) {
            areaId2Name.put(area.getId(), area.getAreaName());
        }

        List<UnitListResp> unitListResps = new ArrayList<>();
        UnitListResp unitListResp;
        StringBuffer areaInfo = new StringBuffer();
        Integer areaId = 0, provinceId = 0, cityId = 0;
        for (Unit unit : units) {
            unitListResp = new UnitListResp();
            BeanUtils.copyProperties(unit, unitListResp);
            unitListResp.setPrecinct(precinetId2Name.get(unit.getPrecinctId()));

            areaId = unit.getAreaId();
            provinceId = unit.getProvinceId();
            cityId = unit.getCityId();
            areaInfo.setLength(0);
            if (StringUtils.isNotEmpty(provinceId) && areaId2Name.containsKey(provinceId)) {
                areaInfo.append(areaId2Name.get(provinceId));
            }
            if (StringUtils.isNotEmpty(cityId) && areaId2Name.containsKey(cityId)) {
                areaInfo.append(areaId2Name.get(cityId));
            }
            if (StringUtils.isNotEmpty(areaId) && areaId2Name.containsKey(areaId)) {
                areaInfo.append(areaId2Name.get(areaId));
            }
            unitListResp.setAreaInfo(areaInfo.toString());
            unitListResp.setUnitTypeDesc(UnitType.getValue(unit.getUnitType()));
            unitListResp.setSuperviseLevelDesc(UnitSuperviseLevel.getValue(unit.getSuperviseLevel()));

            Integer onlineStatus = unitDeviceCache.getUnitOnlineStatus(unit.getId().toString());
            if (StringUtils.isNotNull(onlineStatus) && Status.NORMAL.id.equals(onlineStatus)) {
                unitListResp.setOnlineStatus(BooleanEnum.YES.id);
            }
            unitListResps.add(unitListResp);
        }

        PageBean<UnitListResp> pageBean = new PageBean<>(currentPage, pageSize,total , unitListResps);
        return pageBean;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean insertOrUpdate(UnitInfo unitInfo) {
        Unit unit = new Unit();
        BeanUtils.copyProperties(unitInfo, unit);

        List<String> deviceIds = unitInfo.getDeviceIds();
        if (StringUtils.isEmpty(unit.getId())) {
            unit.setStatus(Status.NORMAL.id);

            int result = super.insert(unit);
            if (result > 0) {
                Integer unitId = unit.getId();
                addUnitDevices(unitId, deviceIds);
            }
            return result > 0;
        } else {
            updateUnitDevice(unitInfo, unitInfo.getId());

            return super.updateByPrimaryKeySelective(unit) > 0;
        }
    }


    /**
     * 更新单位网关关系
     *
     * @param unitInfo
     * @param unitId
     */
    private void updateUnitDevice(UnitInfo unitInfo, Integer unitId) {
        List<String> deviceIds = unitInfo.getDeviceIds();
        if (StringUtils.isEmpty(deviceIds)) {
            unitManualMapper.deleteDeviceByUnitId(unitId);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("unitId", unitId);
            List<UnitDevice> unitDeviceList = unitDeviceService.selectByMap(map);

            String oldDeviceId;
            List<Integer> delUnitDeviceIds = new ArrayList<>();
            List<String> removeDeviceIds = new ArrayList<>();
            for (UnitDevice unitDevice : unitDeviceList) {
                oldDeviceId = unitDevice.getDeviceId();
                boolean result = deviceIds.remove(oldDeviceId);
                if (!result) {
                    delUnitDeviceIds.add(unitDevice.getId());
                    removeDeviceIds.add(unitDevice.getDeviceId());
                }
            }
            if (StringUtils.isNotEmpty(delUnitDeviceIds)) {
                unitDeviceService.deleteBatchIds(delUnitDeviceIds);
                unitDeviceCache.removeUnitDevice(removeDeviceIds.toArray(new String[]{}));
            }
            addUnitDevices(unitId, deviceIds);
        }

    }

    /**
     * 添加单位网关关系
     *
     * @param unitId
     * @param deviceIds
     */
    private void addUnitDevices(Integer unitId, List<String> deviceIds) {
        if (StringUtils.isEmpty(deviceIds)) {
            return;
        }
        List<UnitDevice> unitDevicesDb = unitDeviceService.selectByDeviceIds(deviceIds);
        List<UnitDevice> upList = new ArrayList<>();
        for (UnitDevice unitDevice : unitDevicesDb) {
            if (deviceIds.contains(unitDevice.getDeviceId())) {
                if (unitDevice.getUnitId().equals(unitId)) {
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
            setUserNameAndTime(unitDevice, true);
            unitDeviceList.add(unitDevice);
        }
        unitDeviceService.insertBatch(unitDeviceList);
        if (StringUtils.isNotEmpty(upList)) {
            unitDeviceService.updateByList(upList);
        }

        unitDeviceList.addAll(upList);
        unitDeviceCache.addOrUpdateUnitDevice(unitDeviceList);
    }


    @Override
    public List<DeviceResp> getUnallotDevice(Integer unitId) {
        List<DeviceResp> deviceRespList = new ArrayList<>();
        List<Device> devices = deviceRepository.getDevicesByCodes(null);
        if (StringUtils.isEmpty(devices)) {
            return deviceRespList;
        }

        Map<String, Device> deviceMap = deviceCache.addDevices(devices);
        List<String> deviceIds = unitManualMapper.selectDevicesExcludeUnitId(unitId);

        if (StringUtils.isNotNull(deviceIds)) {
            for (String deviceId : deviceIds) {
                deviceMap.remove(deviceId);
            }
        }

        if (StringUtils.isNotEmpty(deviceMap)) {
            DeviceResp deviceResp;
            for (Map.Entry<String, Device> deviceEntry : deviceMap.entrySet()) {
                deviceResp = new DeviceResp();
                BeanUtils.copyProperties(deviceEntry.getValue(), deviceResp);
                deviceRespList.add(deviceResp);
            }
        }
        return deviceRespList;
    }

    @Override
    public PieResp getUnitSpreadByAreaAndScope(Integer areaId, DataScope dataScope) {
        Map<String, Object> queryMap = new HashMap<>();
        BuildDefaultResultUtil.putAreaSearchMap(areaId, queryMap);

        queryMap.put("precinctScope", dataScope);
        List<Unit> unitList = selectByMap(queryMap);

        Map<Integer, PieData> pieDataMap = new HashMap<>();
        PieResp pieResp = BuildDefaultResultUtil.buildDefaultPieResp(pieDataMap);

        for (Unit unit : unitList) {
            Integer unitType = unit.getUnitType();
            if (pieDataMap.containsKey(unitType)) {
                PieData pieData = pieDataMap.get(unitType);
                pieData.setValue(pieData.getValue() + 1);
            }
        }

        pieResp.setTotal(unitList.size());
        return pieResp;
    }

    @Override
    public AreaUnitMapResp getUnitMapDataByAreaAndScope(Integer areaId, Integer unitType, DataScope dataScope) {
        Map<String, Object> queryMap = new HashMap<>();
        BuildDefaultResultUtil.putAreaSearchMap(areaId, queryMap);

        queryMap.put("unitType", unitType);
        queryMap.put("precinctScope", dataScope);
        List<Unit> unitList = selectByMap(queryMap);

        AreaUnitMapResp areaUnitMapResp = new AreaUnitMapResp();
        List<UnitInfo> unitInfos = new ArrayList<>();
        List<Integer> unitIdList = new ArrayList<>();
        Integer onlineCount = 0;

        List<Integer> unitIds = new ArrayList<>();
        Set<Integer> precinctIdSet = new HashSet<>();
        for(Unit unit : unitList){
            unitIds.add(unit.getId());
            precinctIdSet.add(unit.getPrecinctId());
        }

        //获取单位状态
        Map<Integer,Integer> unitId2Status = getStatusByUnitIds(unitIds);

        for (Unit unit : unitList) {
            UnitInfo unitInfo = new UnitInfo();
            BeanUtils.copyProperties(unit, unitInfo);
            unitInfo.setUnitTypeDesc(UnitType.getValue(unitInfo.getUnitType()));
            unitInfo.setUnitStatus(unitId2Status.get(unit.getId()));
            unitInfos.add(unitInfo);
            unitIdList.add(unit.getId());

            Integer onlineStatus = unitDeviceCache.getUnitOnlineStatus(unit.getId().toString());
            if (StringUtils.isNotNull(onlineStatus) && Status.NORMAL.id.equals(onlineStatus)) {
                onlineCount++;
            }
        }

        if(StringUtils.isNotEmpty(precinctIdSet)){
           List<Precinct> precincts = precinctMapper.selectBatchIds(precinctIdSet);
           Map<Integer,Precinct> precinctNameMap = new HashMap<>();
            for (Precinct precinct : precincts) {
                precinctNameMap.put(precinct.getId(),precinct);
            }
            for (UnitInfo unitInfo : unitInfos) {
                if(precinctNameMap.containsKey(unitInfo.getPrecinctId())){
                    Precinct precinct = precinctNameMap.get(unitInfo.getPrecinctId());
                    unitInfo.setDutyName(precinct.getDutyName());
                    unitInfo.setDutyPhone(precinct.getDutyPhone());
                }
            }
        }

        areaUnitMapResp.setUnitInfos(unitInfos);

        CountDataInfo countDataInfo = getCountDataInfo(unitIdList, onlineCount);
        areaUnitMapResp.setCountDataInfo(countDataInfo);
        return areaUnitMapResp;
    }

    /**
     * 获取管辖区单位统计数据
     *
     * @param unitIdList
     * @param onlineCount
     * @return
     */
    private CountDataInfo getCountDataInfo(List<Integer> unitIdList, Integer onlineCount) {
        Integer alarmCount = 0;
        Integer earlyWarningCount = 0;
        if(StringUtils.isNotEmpty(unitIdList)) {
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("unitIds", unitIdList);
            Date now = new Date();
            Date startTime = DateUtil.getStartDate(now);
            Date endTime = DateUtil.getEndDate(now);
            queryMap.put("startDate", startTime);
            queryMap.put("endDate", endTime);
            queryMap.put("misreport", FaMisreportType.EFFECTIVE.id);
            List<FacilitiesAlarm> facilitiesAlarmList = manageManualMapper.selectFacilitiesAlarmByMap(queryMap);
            for (FacilitiesAlarm facilitiesAlarm : facilitiesAlarmList) {
                Integer alarmType = facilitiesAlarm.getAlarmType();
                if (StringUtils.isNotNull(alarmType) && AlarmType.ALARM.id.equals(alarmType)) {
                    alarmCount++;
                }
                if (StringUtils.isNotNull(alarmType) && AlarmType.EARLY_WARNING.id.equals(alarmType)) {
                    earlyWarningCount++;
                }
            }
        }

        int unitCount = unitIdList.size();
        CountDataInfo countDataInfo = new CountDataInfo();
        countDataInfo.setAlarmCount(alarmCount);
        countDataInfo.setEarlyWarningCount(earlyWarningCount);
        countDataInfo.setOnlineRate(0);
        if (unitCount != 0) {
            countDataInfo.setOnlineRate((int) BigDecimalUtil.mul(BigDecimalUtil.div(onlineCount, unitCount, 2), 100));
        }
        countDataInfo.setUnitCount(unitCount);
        return countDataInfo;
    }

    @Override
    public List<AppUnitResp> selectByNameAndPageScope(String name, BasePage basePage, DataScope dataScope) {
        PageHelper.startPage(basePage.getCurrentPage(), basePage.getPageSize());

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("unitNameLike", name);
        queryMap.put("precinctScope", dataScope);

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
        if (StringUtils.isNull(unit)) {
            throw new RequestException(ResponseCode.FAIL.code, "单位不存在");
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
        appUnitDetailResp.setUnitType(unit.getUnitType());

        /*获取管辖区信息*/
        Precinct precinct = precinctMapper.selectByPrimaryKey(unit.getPrecinctId());
        if (StringUtils.isNotNull(precinct)) {
            appUnitDetailResp.setPrecinctName(precinct.getPrecinctName());
            appUnitDetailResp.setDutyName(precinct.getDutyName());
            appUnitDetailResp.setDutyPhone(precinct.getDutyPhone());
        }

        /*获取当前烟感、漏电状态*/
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("alarmType",AlarmType.ALARM.id);
        queryMap.put("unitId",unitId);
        int somkeAlarmCount = manageManualMapper.countUnfinishAlarmNumByMap(queryMap);

        queryMap.clear();
        queryMap.put("alarmType",AlarmType.EARLY_WARNING.id);
        queryMap.put("unitId",unitId);
        int leakageAlarmCount = manageManualMapper.countUnfinishAlarmNumByMap(queryMap);
        appUnitDetailResp.setSomkeStatus(Status.NORMAL.id);
        if (somkeAlarmCount > 0) {
            appUnitDetailResp.setSomkeStatus(Status.DISABLE.id);
        }

        appUnitDetailResp.setLeakageStatus(Status.NORMAL.id);
        if (leakageAlarmCount > 0) {
            appUnitDetailResp.setLeakageStatus(Status.DISABLE.id);
        }

        /*设置开关状态*/
        List<SwitchResp> switchResps = getSwitchResps(unitId);
        appUnitDetailResp.setSwitchResps(switchResps);

        /*设置电压电流等数据项*/
        List<ItemDataResp> voltages = new ArrayList<>();
        List<ItemDataResp> electriccurrents = new ArrayList<>();
        List<ItemDataResp> cableTemperatures = new ArrayList<>();
        List<ItemDataResp> leakages = new ArrayList<>();

        setItemData(unitId, voltages, electriccurrents, cableTemperatures, leakages);

        appUnitDetailResp.setVoltages(voltages);
        appUnitDetailResp.setElectriccurrents(electriccurrents);
        appUnitDetailResp.setCableTemperatures(cableTemperatures);
        appUnitDetailResp.setLeakages(leakages);

        return appUnitDetailResp;
    }

    /**
     * 设置实时数据
     *
     * @param unitId
     * @param voltages
     * @param electriccurrents
     * @param cableTemperatures
     * @param leakages
     */
    private void setItemData(Integer unitId, List<ItemDataResp> voltages, List<ItemDataResp> electriccurrents, List<ItemDataResp> cableTemperatures, List<ItemDataResp> leakages) {
        List<String> deviceIds = getDevicesByUnitId(unitId);
        if (StringUtils.isEmpty(deviceIds)) {
            return;
        }

        //获取单位下所有数据项
        List<DeviceItem> items = new ArrayList<>();
        Map<String, List<DeviceItem>> deviceItemMap = deviceItemRepository.getDeviceItemInfosByCodes(deviceIds);
        if (StringUtils.isNotEmpty(deviceItemMap)) {
            for (List<DeviceItem> deviceItems : deviceItemMap.values()) {
                items.addAll(deviceItems);
            }
        }
        Map<String, List<DeviceItem>> deviceCalcItemMap = deviceItemRepository.getCalcItemsByDeviceCodes(deviceIds);
        if (StringUtils.isNotEmpty(deviceCalcItemMap)) {
            for (List<DeviceItem> deviceItems : deviceCalcItemMap.values()) {
                items.addAll(deviceItems);
            }
        }
        if (StringUtils.isEmpty(items)) {
            return;
        }

        //查找类型和数据项的关系
        List<String> needFindItemIds = new ArrayList<>();
        Integer btype;
        for (DeviceItem deviceItem : items) {
            btype = deviceItem.getBtype();
            if (!CommonConstant.UNIT_TREND_TYPES.contains(btype)) {
                continue;
            }
            needFindItemIds.add(deviceItem.getId());
        }

        Map<String, DeviceItemRealTimeData> realTimeDataMap = deviceItemRepository.getDeviceItemRealTimeDatasByItemIds(needFindItemIds);
        for (DeviceItem deviceItem : items) {
            ItemDataResp itemDataResp = new ItemDataResp();
            itemDataResp.setDisplayname(deviceItem.getDisplayname());
            itemDataResp.setUnit(deviceItem.getUnit());
            itemDataResp.setVal(CommonConstant.ITEM_INITIAL_VALUE);
            if (realTimeDataMap.containsKey(deviceItem.getId())) {
                itemDataResp.setVal(realTimeDataMap.get(deviceItem.getId()).getVal());
                if (ItemBtype.VOLTAGE.id.equals(deviceItem.getBtype())) {
                    voltages.add(itemDataResp);
                }
                if (ItemBtype.LEAKAGE.id.equals(deviceItem.getBtype())) {
                    leakages.add(itemDataResp);
                }
                if (ItemBtype.CABLE_TEMPERATURE.id.equals(deviceItem.getBtype())) {
                    cableTemperatures.add(itemDataResp);
                }
                if (ItemBtype.ELECTRICCURRENT.id.equals(deviceItem.getBtype())) {
                    electriccurrents.add(itemDataResp);
                }
            }
        }
    }

    private List<SwitchResp> getSwitchResps(Integer unitId) {
        //找到对应遥控数据项
        Map<String, Object> map = new HashMap<>();
        map.put("unitId", unitId);
        map.put("type", StructType.REMOTE.id);
        List<DiagramItemDto> diagramItems = manageManualMapper.selectDiagramItemByMap(map);
        Map<Integer, Map<Integer, DiagramItemDto>> dsIdItemMap = new HashMap<>();
        for (DiagramItemDto diagramItem : diagramItems) {
            if (dsIdItemMap.containsKey(diagramItem.getDsId())) {
                dsIdItemMap.get(diagramItem.getDsId()).put(diagramItem.getItemType(), diagramItem);
            } else {
                Map<Integer, DiagramItemDto> typeItemMap = new HashMap<>();
                typeItemMap.put(diagramItem.getItemType(), diagramItem);
                dsIdItemMap.put(diagramItem.getDsId(), typeItemMap);
            }
        }

        List<SwitchResp> switchResps = new ArrayList<>();
        if (StringUtils.isNotEmpty(dsIdItemMap)) {
            for (Map<Integer, DiagramItemDto> typeItemMap : dsIdItemMap.values()) {
                DiagramItemDto ykItem = typeItemMap.get(DiaItemType.TELECONTROL.id);
                DiagramItemDto switchItem = typeItemMap.get(DiaItemType.ALARM.id);

                if (StringUtils.isNotNull(ykItem) && StringUtils.isNotNull(switchItem)) {
                    String status = deviceItemOpertionService.getYaokongStatus(ykItem.getItemId(), switchItem.getItemId());
                    List<SelectDto> selectDtos = deviceItemOpertionService.getYaokongEnumList(ykItem.getItemId());

                    SwitchResp switchResp = new SwitchResp();
                    switchResp.setSelects(selectDtos);
                    switchResp.setStatus(status);
                    switchResp.setSwitchName(ykItem.getStructName());
                    switchResp.setYkItemId(ykItem.getItemId());
                    switchResps.add(switchResp);
                }
            }
        }
        return switchResps;
    }

    @Override
    public PageBean<List<UnitSwitchResp>> getUnitSwitchList(DataScope dataScope, SearchReq searchReq) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("precinctScope", dataScope);
        queryMap.put("type", StructType.REMOTE.id);
        List<DiagramItemDto> diagramItems = manageManualMapper.selectDiagramItemByMap(queryMap);
        if (StringUtils.isEmpty(diagramItems)) {
            return new PageBean<>();
        }

        Map<Integer, List<DiagramItemDto>> unitId2DiagramItems = new HashMap<>();
        List<DiagramItemDto> tempDiagramItems;
        Integer unitId;
        Set<Integer> unitIds = new HashSet<>();
        for (DiagramItemDto diagramItem : diagramItems) {
            unitId = diagramItem.getUnitId();
            unitIds.add(unitId);
            tempDiagramItems = unitId2DiagramItems.containsKey(unitId) ? unitId2DiagramItems.get(unitId) : new ArrayList<>();
            tempDiagramItems.add(diagramItem);
            unitId2DiagramItems.put(unitId, tempDiagramItems);
        }

        Map<Integer, String> unitId2Name = new HashMap<>();
        if (StringUtils.isNotEmpty(unitIds)) {
            List<Unit> unitList = selectBatchIds(unitIds);
            for (Unit unit : unitList) {
                unitId2Name.put(unit.getId(), unit.getUnitName());
            }
        }

        List<List<UnitSwitchResp>> unitSwitchList = new ArrayList<>();
        for (Map.Entry<Integer, List<DiagramItemDto>> entry : unitId2DiagramItems.entrySet()) {
            Map<Integer, Map<Integer, DiagramItemDto>> dsIdItemMap = new HashMap<>();
            for (DiagramItemDto diagramItem : diagramItems) {
                if (dsIdItemMap.containsKey(diagramItem.getDsId())) {
                    dsIdItemMap.get(diagramItem.getDsId()).put(diagramItem.getItemType(), diagramItem);
                } else {
                    Map<Integer, DiagramItemDto> typeItemMap = new HashMap<>();
                    typeItemMap.put(diagramItem.getItemType(), diagramItem);
                    dsIdItemMap.put(diagramItem.getDsId(), typeItemMap);
                }
            }

            if (StringUtils.isNotEmpty(dsIdItemMap)) {
                List<UnitSwitchResp> unitSwitchResps = new ArrayList<>();
                UnitSwitchResp unitSwitchResp;
                for (Map<Integer, DiagramItemDto> typeItemMap : dsIdItemMap.values()) {
                    DiagramItemDto ykItem = typeItemMap.get(DiaItemType.TELECONTROL.id);
                    DiagramItemDto switchItem = typeItemMap.get(DiaItemType.ALARM.id);

                    if (StringUtils.isNotNull(ykItem) && StringUtils.isNotNull(switchItem)) {
                        String status = deviceItemOpertionService.getYaokongStatus(ykItem.getItemId(), switchItem.getItemId());
                        List<SelectDto> selectDtos = deviceItemOpertionService.getYaokongEnumList(ykItem.getItemId());

                        unitSwitchResp = new UnitSwitchResp();
                        unitSwitchResp.setSelects(selectDtos);
                        unitSwitchResp.setSwitchStatus(status);
                        unitSwitchResp.setSwitchName(ykItem.getStructName());
                        unitSwitchResp.setUnitName(unitId2Name.get(entry.getKey()));
                        unitSwitchResp.setItemId(ykItem.getItemId());
                        unitSwitchResps.add(unitSwitchResp);
                    }
                }
                unitSwitchList.add(unitSwitchResps);
            }
        }

        List<List<UnitSwitchResp>> resultList = PageUtil.pagedList(searchReq.getCurrentPage(), searchReq.getPageSize(), unitSwitchList);
        PageBean<List<UnitSwitchResp>> pageBean = new PageBean<>(searchReq.getCurrentPage(), searchReq.getPageSize(), unitSwitchList.size(), resultList);
        return pageBean;
    }

    @Override
    public List<String> getDevicesByUnitId(Integer unitId) {
        return unitManualMapper.selectDevicesByUnitId(unitId);
    }

    @Override
    public UnitMapResp getUnitMapDataByUnitId(Integer unitId) {
        UnitMapResp unitMapResp = new UnitMapResp();

        Unit unit = selectByPrimaryKey(unitId);
        if (StringUtils.isNull(unit)) {
            throw new RequestException(ResponseCode.FAIL.code, "单位不存在");
        }
        UnitInfo unitInfo = new UnitInfo();
        unitInfo.setId(unitId);
        unitInfo.setUnitName(unit.getUnitName());
        unitInfo.setAreaId(unit.getAreaId());
        unitInfo.setPrecinctId(unit.getPrecinctId());
        unitInfo.setTransactor(unit.getTransactor());
        unitInfo.setContactPhone(unit.getContactPhone());
        unitInfo.setAddress(unit.getAddress());
        unitInfo.setBusinessScope(unit.getBusinessScope());
        unitInfo.setItems(unit.getItems());
        unitInfo.setJoinTime(unit.getJoinTime());
        unitInfo.setUnitType(unit.getUnitType());
        unitInfo.setSuperviseLevel(unit.getSuperviseLevel());
        unitInfo.setEconomySystem(unit.getEconomySystem());

        unitInfo.setLng(unit.getLng());
        unitInfo.setLat(unit.getLat());
        unitInfo.setUnitPic(unit.getUnitPic());
        unitInfo.setUnitTypeDesc(UnitType.getValue(unit.getUnitType()));
        /*获取管辖区信息*/
        Precinct precinct = precinctMapper.selectByPrimaryKey(unit.getPrecinctId());
        if (StringUtils.isNotNull(precinct)) {
            unitInfo.setPrecinctName(precinct.getPrecinctName());
            unitInfo.setDutyName(precinct.getDutyName());
            unitInfo.setDutyPhone(precinct.getDutyPhone());
        }

        /*单位状态*/
        Map<Integer,Integer> unitId2Status = getStatusByUnitIds(Arrays.asList(unitId));
        unitInfo.setUnitStatus(unitId2Status.get(unitId));
        unitMapResp.setUnitInfo(unitInfo);

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("unitId", unitId);
        Date now = new Date();
        Date startTime = DateUtil.getStartDate(now);
        Date endTime = DateUtil.getEndDate(now);
        queryMap.put("startDate", startTime);
        queryMap.put("endDate", endTime);
        queryMap.put("misreport", FaMisreportType.EFFECTIVE.id);
        List<Map<String, Object>> alarmResultList = manageManualMapper.countFaTypeNumByMap(queryMap);
        Integer allCount = 0;
        for (Map<String, Object> alarmCountMap : alarmResultList) {
            Object alarmTypeObj = alarmCountMap.get("alarmType");
            if (StringUtils.isNotNull(alarmTypeObj)) {
                Integer alramType = Integer.parseInt(alarmTypeObj.toString());
                Integer alarmNum = Integer.parseInt(alarmCountMap.get("alarmNum").toString());
                if (AlarmType.ALARM.id.equals(alramType)) {
                    unitMapResp.setAlarmCount(alarmNum);
                    allCount = allCount + alarmNum;
                }
                if (AlarmType.EARLY_WARNING.id.equals(alramType)) {
                    unitMapResp.setEarlyWarningCount(alarmNum);
                    allCount = allCount + alarmNum;
                }
            }
        }
        unitMapResp.setAllCount(allCount);

        /*统计巡查次数*/
        Integer patrolCount = unitManualMapper.countUnitPatrol(queryMap);
        unitMapResp.setPatrol(patrolCount);
        return unitMapResp;
    }

    @Override
    public UnitRealtimeDataResp getUnitRealtimeData(Integer unitId) {
        UnitRealtimeDataResp unitRealtimeDataResp = new UnitRealtimeDataResp();

        /*获取当前烟感、漏电状态*/
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("unitId",unitId);
        queryMap.put("handleStatuses",Arrays.asList(0,1,2));
        queryMap.put("alarmType",AlarmType.ALARM.id);
        List<FacilitiesAlarm> facilitiesAlarms = manageManualMapper.selectFacilitiesAlarmByMap(queryMap);

        List<String> alarmFaNames = new ArrayList<>();
        if(StringUtils.isNotEmpty(facilitiesAlarms)){
            List<String> itemIds = new ArrayList<>();
            for(FacilitiesAlarm facilitiesAlarm : facilitiesAlarms){
                itemIds.add(facilitiesAlarm.getAlarmItemId());
            }

            queryMap.clear();
            queryMap.put("itemIds",itemIds);
            List<DiagramItemDto> itemDtos = manageManualMapper.selectDiagramItemByMap(queryMap);

            for(DiagramItemDto itemDto: itemDtos){
                alarmFaNames.add(itemDto.getStructName());
            }
        }
        unitRealtimeDataResp.setAlarmFaNames(alarmFaNames);

        /*设置开关状态*/
        List<SwitchResp> switchResps = getSwitchResps(unitId);
        unitRealtimeDataResp.setSwitchResps(switchResps);

        /*设置电压电流等数据项*/
        List<ItemDataResp> voltages = new ArrayList<>();
        List<ItemDataResp> electriccurrents = new ArrayList<>();
        List<ItemDataResp> cableTemperatures = new ArrayList<>();
        List<ItemDataResp> leakages = new ArrayList<>();
        setItemData(unitId, voltages, electriccurrents, cableTemperatures, leakages);

        unitRealtimeDataResp.setVoltages(voltages);
        unitRealtimeDataResp.setElectriccurrents(electriccurrents);
        unitRealtimeDataResp.setCableTemperatures(cableTemperatures);
        unitRealtimeDataResp.setLeakages(leakages);
        return unitRealtimeDataResp;
    }

    @Override
    public CountDataInfo getUnitTotalByScope(DataScope dataScope) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("precinctScope", dataScope);
        List<Unit> unitList = selectByMap(queryMap);

        List<Integer> unitIdList = new ArrayList<>();
        Integer onlineCount = 0;
        for (Unit unit : unitList) {
            unitIdList.add(unit.getId());
            Integer onlineStatus = unitDeviceCache.getUnitOnlineStatus(unit.getId().toString());
            if (StringUtils.isNotNull(onlineStatus) && Status.NORMAL.id.equals(onlineStatus)) {
                onlineCount++;
            }
        }
        return getCountDataInfo(unitIdList, onlineCount);
    }

    @Override
    public List<DeviceResp> getDeviceByUnitId(Integer unitId) {
        List<DeviceResp> deviceRespList = new ArrayList<>();
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("unitId", unitId);
        List<UnitDevice> unitDevices = unitDeviceService.selectByMap(queryMap);
        if (StringUtils.isEmpty(unitDevices)) {
            return deviceRespList;
        }

        List<String> deviceIds = new ArrayList<>();
        for (UnitDevice unitDevice : unitDevices) {
            deviceIds.add(unitDevice.getDeviceId());
        }
        List<Device> devices = deviceRepository.getDevicesByCodes(deviceIds);
        if (StringUtils.isEmpty(devices)) {
            return deviceRespList;
        }

        DeviceResp deviceResp;
        for (Device device : devices) {
            deviceResp = new DeviceResp();
            BeanUtils.copyProperties(device, deviceResp);
            deviceRespList.add(deviceResp);
        }
        return deviceRespList;
    }

    @Override
    public List<UnitListResp> getUnitListByPrecinctIds(List<Integer> precinctIds) {
        DataScope dataScope = new DataScope("precinct_id", precinctIds);
        Map<String, Object> map = new HashMap<>();
        map.put("precinctScope", dataScope);
        List<Unit> unitList = selectByMap(map);

        List<UnitListResp> unitListResps = new ArrayList<>();
        for (Unit unit : unitList) {
            UnitListResp unitListResp = new UnitListResp();
            BeanUtils.copyProperties(unit, unitListResp);
            unitListResps.add(unitListResp);
        }
        return unitListResps;
    }

    @Override
    public List<UnitListResp> getUnitListByNameAndScope(String unitName, DataScope dataScope) {
        Map<String, Object> map = new HashMap<>();
        map.put("precinctScope", dataScope);
        if (StringUtils.isNotEmpty(unitName)) {
            map.put("unitNameLike", unitName);
        }
        List<Unit> unitList = selectByMap(map);

        List<UnitListResp> unitListResps = new ArrayList<>();
        for (Unit unit : unitList) {
            UnitListResp unitListResp = new UnitListResp();
            BeanUtils.copyProperties(unit, unitListResp);
            unitListResps.add(unitListResp);
        }
        return unitListResps;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int delUnitById(Integer unitId) {
        //删除网关单位关联关系
        unitManualMapper.deleteDeviceByUnitId(unitId);
        return deleteByPrimaryKey(unitId);
    }


    @Override
    public  Map<Integer,Integer> getStatusByUnitIds(List<Integer> unitIds){
        Map<Integer,Integer> unitId2Status = new HashMap<>();
        for(Integer unitId: unitIds){
            unitId2Status.put(unitId,UnitStatus.OFFLINE.id);
        }

        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("unitIds",unitIds);
        queryMap.put("alarmStatus",new DataScope("handle_status", Arrays.asList(AlarmHandleStatus.UNTREATED.id,AlarmHandleStatus.RESPONSE.id,AlarmHandleStatus.UNDER_WAY.id)));
        List<FacilitiesAlarm> facilitiesAlarms = manageManualMapper.selectFacilitiesAlarmByMap(queryMap);

        Integer unitId;
        Set<Integer> alarmUnitIds = new HashSet<>();
        Map<Integer,Integer> unitId2AlarmLevel = new HashMap<>();
        if(StringUtils.isNotEmpty(facilitiesAlarms)){
            for(FacilitiesAlarm facilitiesAlarm : facilitiesAlarms){
                unitId = facilitiesAlarm.getUnitId();
                if(alarmUnitIds.contains(unitId)){
                    continue;
                }
                if(AlarmType.ALARM.id.equals(facilitiesAlarm.getAlarmType())){
                    alarmUnitIds.add(unitId);
                    unitId2AlarmLevel.put(unitId,UnitStatus.ALARM.id);
                }
                if(unitId2AlarmLevel.containsKey(unitId)){
                    continue;
                }
                unitId2AlarmLevel.put(unitId,UnitStatus.EARLYWARNING.id);
            }
        }

        Integer unitStatus;
        for(Map.Entry<Integer,Integer> entry : unitId2Status.entrySet()){
            unitId = entry.getKey();
            unitStatus =  unitDeviceCache.getUnitOnlineStatus(unitId.toString());
            if(StringUtils.isNull(unitStatus) || unitStatus.equals(UnitStatus.OFFLINE.id)) {
                continue;
            }

            if(unitId2AlarmLevel.containsKey(unitId)){
                unitId2Status.put(unitId,unitId2AlarmLevel.get(unitId));
            }else{
                unitId2Status.put(unitId,UnitStatus.ONLINE.id);
            }
        }
        return unitId2Status;
    }

}
