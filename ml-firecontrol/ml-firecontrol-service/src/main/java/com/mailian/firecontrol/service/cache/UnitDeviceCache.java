package com.mailian.firecontrol.service.cache;

import com.mailian.core.util.RedisUtils;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.dao.auto.mapper.UnitMapper;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dao.auto.model.UnitDevice;
import com.mailian.firecontrol.dao.manual.UnitManualMapper;
import com.mailian.firecontrol.dto.UnitRedisInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
    @Autowired
    private RedisUtils redisUtils;
    @Resource
    private UnitManualMapper unitManualMapper;
    @Resource
    private UnitMapper unitMapper;

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

            unitDeviceMap = new HashMap<>();
            for (UnitRedisInfo unitRedisInfo : unitDeviceList) {
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
    public void addOrUpdateUnitDevice(UnitDevice unitDevice){
        if(StringUtils.isNotNull(unitDevice)){
            return;
        }
        UnitRedisInfo unitRedisInfo = new UnitRedisInfo();
        Unit unit = unitMapper.selectByPrimaryKey(unitDevice.getUnitId());
        String deviceId = unitDevice.getDeviceId();
        unitRedisInfo.setDeviceId(deviceId);
        unitRedisInfo.setId(unit.getId());
        unitRedisInfo.setUnitName(unit.getUnitName());
        redisUtils.addHashValue(CommonConstant.SYS_DEVICE_UNIT_KEY,deviceId,unitRedisInfo,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
    }

    /**
     * 添加或更新单位网关关联关系
     * @param unitDeviceList
     */
    public void addOrUpdateUnitDevice(List<UnitDevice> unitDeviceList){
        if(StringUtils.isNotEmpty(unitDeviceList)){
            return;
        }
        Integer unitId = unitDeviceList.get(0).getUnitId();
        Unit unit = unitMapper.selectByPrimaryKey(unitId);
        UnitRedisInfo unitRedisInfo;
        Map<String,UnitRedisInfo> unitInfoMap = new HashMap<>();
        for (UnitDevice unitDevice : unitDeviceList) {
            unitRedisInfo = new UnitRedisInfo();
            unitRedisInfo.setDeviceId(unitDevice.getDeviceId());
            unitRedisInfo.setId(unit.getId());
            unitRedisInfo.setUnitName(unit.getUnitName());
            unitInfoMap.put(unitDevice.getDeviceId(),unitRedisInfo);
        }

        redisUtils.addAllHashValue(CommonConstant.SYS_DEVICE_UNIT_KEY,unitInfoMap,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
    }

    /**
     * 移除单位网关关联关系
     * @param deviceIds
     */
    public void removeUnitDevice(String... deviceIds){
        if(StringUtils.isEmpty(deviceIds)){
            return;
        }
        redisUtils.deleteHashValue(CommonConstant.SYS_DEVICE_UNIT_KEY,deviceIds);
    }

}
