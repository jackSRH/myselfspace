package com.mailian.firecontrol.service.cache;

import com.mailian.core.util.RedisUtils;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.dto.push.DeviceSub;
import com.mailian.firecontrol.service.repository.DeviceSubRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/23
 * @Description:
 */
@Component
public class DeviceSubCache {
    private static final Logger log = LoggerFactory.getLogger(DeviceSubCache.class);

    @Resource
    private DeviceSubRepository deviceSubRepository;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 更新RTU信息
     * @param deviceSubs
     */
    public Map<String,List<DeviceSub>> updateDeviceSubs(List<DeviceSub> deviceSubs) {
        Map<String,List<DeviceSub>> gwDeviceMap = new HashMap<>();
        if(StringUtils.isNotEmpty(deviceSubs)){
            for (DeviceSub deviceSub : deviceSubs) {
                String gwid = deviceSub.getGwid();
                List<DeviceSub> deviceSubList = gwDeviceMap.containsKey(gwid)?gwDeviceMap.get(gwid):new ArrayList<>();

                deviceSubList.add(deviceSub);
                gwDeviceMap.put(gwid,deviceSubList);
            }

            redisUtils.addAllHashValue(CommonConstant.DEVICE_CODE_TO_DEVICE_SUB,gwDeviceMap,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
        }
        return gwDeviceMap;
    }

    /**
     * 通过code获取RTU信息
     * @param deviceCodes
     * @return
     */
    public Map<String,List<DeviceSub>> getSubsByCodes(List<String> deviceCodes){
        Map<String,List<DeviceSub>> codeSubMap = redisUtils.entries(CommonConstant.DEVICE_CODE_TO_DEVICE_SUB);
        if(StringUtils.isEmpty(codeSubMap)){
            codeSubMap = new HashMap<>();
        }

        List<String> needFindCodes = new ArrayList<>();
        Map<String,List<DeviceSub>> code2Subs = new HashMap<>();
        for(String code :deviceCodes){
            if (codeSubMap.containsKey(code)){
                code2Subs.put(code,codeSubMap.get(code));
            }else{
                needFindCodes.add(code);
            }
        }

        //如果通过code在缓存中获取为null,则通过codes调接口获取
        if(StringUtils.isNotEmpty(needFindCodes)) {
            List<DeviceSub> apiDeviceSubList = deviceSubRepository.getDeviceSubsByCodes(needFindCodes);
            Map<String,List<DeviceSub>> apiDeviceSubMap = updateDeviceSubs(apiDeviceSubList);
            if(StringUtils.isNotEmpty(apiDeviceSubMap)){
                code2Subs.putAll(apiDeviceSubMap);
            }
        }
        return code2Subs;
    }

    /**
     * 根据RTU组合id 获取rtu信息
     * @param subIds
     * @return
     */
    public Map<String,DeviceSub> getDeviceSubsBySubIds(List<String> subIds){
        Map<String,DeviceSub> subId2Sub = new HashMap<String,DeviceSub>();
        List<String> needFindSubIds = new ArrayList<String>();
        List<String> newSubIds = new ArrayList<>(subIds);
        List<DeviceSub> deviceSubList = redisUtils.getHashMultiValue(CommonConstant.SUB_ID_TO_DEVICE_SUB,subIds);
        for (DeviceSub deviceSub : deviceSubList) {
            if(StringUtils.isNotNull(deviceSub)) {
                subId2Sub.put(deviceSub.getRtuidcb(), deviceSub);

                newSubIds.remove(deviceSub.getRtuidcb());
            }
        }
        needFindSubIds.addAll(newSubIds);

        if(StringUtils.isNotEmpty(needFindSubIds)) {
            List<DeviceSub> apiDeviceSubList = deviceSubRepository.getDeviceSubsBySubIds(needFindSubIds);
            if(StringUtils.isNotEmpty(apiDeviceSubList)){
                Map<String,DeviceSub> apiDeviceSubMap = new HashMap<String,DeviceSub>();
                for (DeviceSub deviceSub : apiDeviceSubList) {
                    subId2Sub.put(deviceSub.getRtuidcb(),deviceSub);
                    apiDeviceSubMap.put(deviceSub.getRtuidcb(),deviceSub);
                }
                redisUtils.addAllHashValue(CommonConstant.SUB_ID_TO_DEVICE_SUB,apiDeviceSubMap,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
            }
        }
        return subId2Sub;
    }

    /**
     * 根据 id 获取rtu信息
     * @param subId
     * @return
     */
    public DeviceSub getDeviceSubBySubId(String subId) {
        if(StringUtils.isEmpty(subId)){
            return null;
        }

        if(redisUtils.hasHashKey(CommonConstant.SUB_ID_TO_DEVICE_SUB,subId)){
            return redisUtils.getHashValue(CommonConstant.SUB_ID_TO_DEVICE_SUB,subId);
        }else{
            List<DeviceSub> deviceSubList = deviceSubRepository.getDeviceSubsBySubIds(Arrays.asList(subId));
            if(StringUtils.isNotEmpty(deviceSubList)){
                DeviceSub deviceSub = deviceSubList.get(0);
                redisUtils.addHashValue(CommonConstant.SUB_ID_TO_DEVICE_SUB,deviceSub.getRtuidcb(),deviceSub,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
                return deviceSub;
            }
        }

        return null;
    }
}
