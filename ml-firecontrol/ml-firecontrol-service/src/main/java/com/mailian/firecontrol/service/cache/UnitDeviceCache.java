package com.mailian.firecontrol.service.cache;

import com.mailian.core.enums.Status;
import com.mailian.core.util.RedisUtils;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.dao.auto.mapper.PrecinctMapper;
import com.mailian.firecontrol.dao.auto.mapper.UnitDeviceMapper;
import com.mailian.firecontrol.dao.auto.mapper.UnitMapper;
import com.mailian.firecontrol.dao.auto.model.Precinct;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dao.auto.model.UnitDevice;
import com.mailian.firecontrol.dao.manual.mapper.UnitManualMapper;
import com.mailian.firecontrol.dto.UnitRedisInfo;
import com.mailian.firecontrol.dto.push.DeviceCommunicationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/1
 * @Description:
 */
@Component
public class UnitDeviceCache {
    private static final Logger log = LoggerFactory.getLogger(UnitDeviceCache.class);

    @Autowired
    private RedisUtils redisUtils;
    @Resource
    private UnitManualMapper unitManualMapper;
    @Resource
    private UnitMapper unitMapper;
    @Resource
    private PrecinctMapper precinctMapper;
    @Resource
    private UnitDeviceMapper unitDeviceMapper;
    @Autowired
    private DeviceCache deviceCache;

    /**
     * 根据网关id 获取单位信息
     * @param deviceIds
     * @return
     */
    public Map<String,UnitRedisInfo> getUnitMapByDeviceIds(List<String> deviceIds){
        if(StringUtils.isEmpty(deviceIds)){
            return null;
        }

        Map<String,UnitRedisInfo> unitDeviceMap = redisUtils.entries(CommonConstant.SYS_DEVICE_UNIT_KEY);
        if(StringUtils.isEmpty(unitDeviceMap)){
            List<UnitRedisInfo> unitDeviceList = unitManualMapper.selectUnitDeviceByDeviceIds(deviceIds);

            List<Precinct> precincts = precinctMapper.selectByMap(null);
            Map<Integer,Precinct> precinctMap = new HashMap<>();
            for (Precinct precinct : precincts) {
                if(StringUtils.isNotEmpty(precinct.getDutyPhone())) {
                    precinctMap.put(precinct.getId(),precinct);
                }
            }

            unitDeviceMap = new HashMap<>();
            for (UnitRedisInfo unitRedisInfo : unitDeviceList) {
                Precinct precinct = precinctMap.get(unitRedisInfo.getPrecinctId());
                unitRedisInfo.setContactPhone(precinct.getDutyPhone());
                unitRedisInfo.setDutyName(precinct.getDutyName());
                unitDeviceMap.put(unitRedisInfo.getDeviceId(),unitRedisInfo);
            }
            redisUtils.addAllHashValue(CommonConstant.SYS_DEVICE_UNIT_KEY,unitDeviceMap,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
        }
        return unitDeviceMap;
    }

    /**
     * 添加或更新单位网关关联关系
     * @param unitDevice
     */
    @Async
    public void addOrUpdateUnitDevice(UnitDevice unitDevice){
        if(StringUtils.isNull(unitDevice)){
            return;
        }
        Unit unit = unitMapper.selectByPrimaryKey(unitDevice.getUnitId());
        Precinct precinct = precinctMapper.selectByPrimaryKey(unit.getPrecinctId());
        String deviceId = unitDevice.getDeviceId();
        UnitRedisInfo unitRedisInfo = copyToUnitRedisInfo(deviceId,unit,precinct);
        redisUtils.addHashValue(CommonConstant.SYS_DEVICE_UNIT_KEY,deviceId,unitRedisInfo,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
    }

    /**
     * 添加或更新单位网关关联关系
     * @param unitDeviceList
     */
    @Async
    public void addOrUpdateUnitDevice(List<UnitDevice> unitDeviceList){
        if(StringUtils.isEmpty(unitDeviceList)){
            return;
        }
        Integer unitId = unitDeviceList.get(0).getUnitId();
        Unit unit = unitMapper.selectByPrimaryKey(unitId);
        Precinct precinct = precinctMapper.selectByPrimaryKey(unit.getPrecinctId());
        UnitRedisInfo unitRedisInfo;
        Map<String,UnitRedisInfo> unitInfoMap = new HashMap<>();
        for (UnitDevice unitDevice : unitDeviceList) {
            unitRedisInfo = copyToUnitRedisInfo(unitDevice.getDeviceId(),unit,precinct);
            unitInfoMap.put(unitDevice.getDeviceId(),unitRedisInfo);
        }
        redisUtils.addAllHashValue(CommonConstant.SYS_DEVICE_UNIT_KEY,unitInfoMap,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
    }

    /**
     * 添加单位网关关联关系
     */
    @Async
    public void addUnitDevices(){
        List<UnitDevice> unitDeviceList =  unitDeviceMapper.selectByMap(null);
        if(StringUtils.isNotEmpty(unitDeviceList)){
            List<Unit> unitList = unitMapper.selectByMap(null);
            Map<Integer,Unit> unitMap = new HashMap<>();
            for (Unit unit : unitList) {
                unitMap.put(unit.getId(),unit);
            }
            List<Precinct> precincts = precinctMapper.selectByMap(null);
            Map<Integer,Precinct> precinctMap = new HashMap<>();
            for (Precinct precinct : precincts) {
                precinctMap.put(precinct.getId(),precinct);
            }

            Map<String,UnitRedisInfo> unitInfoMap = new HashMap<>();
            for (UnitDevice unitDevice : unitDeviceList) {
                Unit unit = unitMap.get(unitDevice.getUnitId());
                if(StringUtils.isNull(unit)){
                    continue;
                }

                Precinct precinct = precinctMap.get(unit.getPrecinctId());
                UnitRedisInfo unitRedisInfo = new UnitRedisInfo();
                unitRedisInfo.setDeviceId(unitDevice.getDeviceId());
                unitRedisInfo.setId(unit.getId());
                unitRedisInfo.setUnitName(unit.getUnitName());
                unitRedisInfo.setPrecinctId(unit.getPrecinctId());
                unitRedisInfo.setContactPhone(precinct.getDutyPhone());
                unitRedisInfo.setDutyName(precinct.getDutyName());
                unitRedisInfo.setAreaId(unit.getAreaId());
                unitRedisInfo.setCityId(unit.getCityId());
                unitRedisInfo.setProvinceId(unit.getProvinceId());
                unitRedisInfo.setUnitType(unit.getUnitType());
                unitRedisInfo.setLat(unit.getLat());
                unitRedisInfo.setLng(unit.getLng());

                unitInfoMap.put(unitDevice.getDeviceId(),unitRedisInfo);
            }
            redisUtils.addAllHashValue(CommonConstant.SYS_DEVICE_UNIT_KEY,unitInfoMap,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
            log.info("添加单位网关关联关系成功，总数:{}",unitInfoMap.size());
        }
    }


    private UnitRedisInfo copyToUnitRedisInfo(String deviceId,Unit unit,Precinct precinct){
        UnitRedisInfo unitRedisInfo = new UnitRedisInfo();
        unitRedisInfo.setDeviceId(deviceId);
        unitRedisInfo.setId(unit.getId());
        unitRedisInfo.setUnitName(unit.getUnitName());
        unitRedisInfo.setPrecinctId(unit.getPrecinctId());
        unitRedisInfo.setContactPhone(precinct.getDutyPhone());
        unitRedisInfo.setAreaId(unit.getAreaId());
        unitRedisInfo.setCityId(unit.getCityId());
        unitRedisInfo.setProvinceId(unit.getProvinceId());
        unitRedisInfo.setUnitType(unit.getUnitType());
        return unitRedisInfo;
    }

    /**
     * 移除单位网关关联关系
     * @param deviceIds
     */
    @Async
    public void removeUnitDevice(String... deviceIds){
        if(StringUtils.isEmpty(deviceIds)){
            return;
        }
        redisUtils.deleteHashValue(CommonConstant.SYS_DEVICE_UNIT_KEY,deviceIds);
    }

    /**
     * 获取缓存中的网关
     * @return
     */
    public List<String> getDevices(){
        List<String> deviceList = new ArrayList<>();
        Map<String,UnitRedisInfo> unitDeviceMap = redisUtils.entries(CommonConstant.SYS_DEVICE_UNIT_KEY);
        if(StringUtils.isNotEmpty(unitDeviceMap)) {
            for (String device : unitDeviceMap.keySet()) {
                deviceList.add(device);
            }
        }
        return deviceList;
    }

    /**
     * 更新单位状态
     * @param deviceCommunicationStatus
     */
    public void upUnitOnlineStatus(DeviceCommunicationStatus deviceCommunicationStatus){
        Integer status = deviceCommunicationStatus.getStatus();
        String deviceCode = deviceCommunicationStatus.getGwid();
        UnitRedisInfo unitRedisInfo = redisUtils.getHashValue(CommonConstant.SYS_DEVICE_UNIT_KEY,deviceCode);
        if(StringUtils.isNull(unitRedisInfo)){
            return;
        }

        String unitIdStr = unitRedisInfo.getId().toString();
        if(Status.NORMAL.id.equals(status)){//上线
            Integer unitStatus = redisUtils.getHashValue(CommonConstant.SYS_UNIT_STATUS_KEY,unitIdStr);
            if(StringUtils.isNull(unitStatus) || Status.DISABLE.id.equals(unitStatus)){ /*单位离线状态*/
                Map<String,Object> queryMap = new HashMap<>();
                queryMap.put("unitId",unitRedisInfo.getId());
                List<UnitDevice> unitDeviceList = unitDeviceMapper.selectByMap(queryMap);

                int count = 0;
                for (UnitDevice unitDevice : unitDeviceList) {
                    DeviceCommunicationStatus deviceStatus = deviceCache.getStatusByCode(unitDevice.getDeviceId());
                    if(StringUtils.isNotNull(deviceStatus.getStatus())
                            && Status.NORMAL.id.equals(deviceStatus.getStatus())
                            && !deviceCode.equals(unitDevice.getDeviceId())) {//保证其余网关都上线
                        count++;
                    }
                }
                if(count >= unitDeviceList.size()-1){
                    redisUtils.addHashValue(CommonConstant.SYS_UNIT_STATUS_KEY,unitIdStr,Status.NORMAL.id,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
                }
            }
        }else{
            /*直接设置单位状态为离线*/
            redisUtils.addHashValue(CommonConstant.SYS_UNIT_STATUS_KEY,unitIdStr,Status.DISABLE.id,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
        }

    }

    /**
     * 获取单位在线状态
     * @param unitIdStr
     * @return
     */
    public Integer getUnitOnlineStatus(String unitIdStr){
        return redisUtils.getHashValue(CommonConstant.SYS_UNIT_STATUS_KEY,unitIdStr);
    }

}
