package com.mailian.firecontrol.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.bean.PageBean;
import com.mailian.core.db.DataScope;
import com.mailian.core.enums.Status;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.UnitSuperviseLevel;
import com.mailian.firecontrol.common.enums.UnitType;
import com.mailian.firecontrol.dao.auto.mapper.PrecinctMapper;
import com.mailian.firecontrol.dao.auto.mapper.UnitMapper;
import com.mailian.firecontrol.dao.auto.model.Area;
import com.mailian.firecontrol.dao.auto.model.Precinct;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dao.auto.model.UnitDevice;
import com.mailian.firecontrol.dao.manual.UnitManualMapper;
import com.mailian.firecontrol.dto.push.Device;
import com.mailian.firecontrol.dto.web.UnitInfo;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.DeviceResp;
import com.mailian.firecontrol.dto.web.response.UnitListResp;
import com.mailian.firecontrol.service.AreaService;
import com.mailian.firecontrol.service.UnitDeviceService;
import com.mailian.firecontrol.service.UnitService;
import com.mailian.firecontrol.service.cache.DeviceCache;
import com.mailian.firecontrol.service.cache.UnitDeviceCache;
import com.mailian.firecontrol.service.repository.DeviceRepository;
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
        for(Unit unit : units){
            precinctIds.add(unit.getPrecinctId());
        }
        List<Precinct> precincts = precinctMapper.selectBatchIds(precinctIds);
        Map<Integer,String> precinetId2Name = new HashMap<>();
        for(Precinct precinct : precincts){
            precinetId2Name.put(precinct.getId(),precinct.getPrecinctName());
        }

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
     * 添加用户管辖区关联
     * @param unitId
     * @param deviceIds
     */
    private void addUnitDevices(Integer unitId, List<String> deviceIds) {
        if(StringUtils.isEmpty(deviceIds)){
            return;
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
        unitDeviceCache.addOrUpdateUnitDevice(unitDeviceList);
    }


    @Override
    public List<DeviceResp> getUnallotDevice() {
        List<DeviceResp> deviceRespList = new ArrayList<>();
        List<Device> devices = deviceRepository.getDevicesByCodes(null);
        if(StringUtils.isEmpty(devices)){
            return deviceRespList;
        }

        Map<String,Device> deviceMap = deviceCache.addDevices(devices);
        List<String> deviceIds = unitManualMapper.selectDevices();
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

}
