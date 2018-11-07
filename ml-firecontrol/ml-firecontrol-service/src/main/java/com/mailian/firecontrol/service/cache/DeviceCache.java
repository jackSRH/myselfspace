package com.mailian.firecontrol.service.cache;

import com.mailian.core.util.RedisUtils;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.dto.push.Device;
import com.mailian.firecontrol.dto.push.DeviceCommunicationStatus;
import com.mailian.firecontrol.service.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/25
 * @Description:
 */
@Component
public class DeviceCache {
    @Resource
    private DeviceRepository deviceRepository;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 新增网关
     * @param devices
     */
    public Map<String,Device> addDevices(List<Device> devices) {
        Map<String,Device> codeDeviceMap = new HashMap<>();
        for (Device device : devices) {
            codeDeviceMap.put(device.getCode(),device);
        }

        redisUtils.addAllHashValue(CommonConstant.DEVICE_CODE_TO_DEVICE,codeDeviceMap,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
        return codeDeviceMap;
    }

    /**
     * 通过code 列表获取网关信息
     * @param deviceCodes
     * @return
     */
    public List<Device> getDevicesByCodes(List<String> deviceCodes){
        if(StringUtils.isEmpty(deviceCodes)){
            return null;
        }
        List<String> needFindCodes = new ArrayList<String>();
        List<Device> devices = redisUtils.getHashMultiValue(CommonConstant.DEVICE_CODE_TO_DEVICE,deviceCodes);
        List<String> newDeviceCodes = new ArrayList<>(deviceCodes);
        for (Device device : devices) {
            if(StringUtils.isNotNull(device)) {
                newDeviceCodes.remove(device.getCode());
            }
        }
        needFindCodes.addAll(newDeviceCodes);

        if(StringUtils.isNotEmpty(needFindCodes)) {
            //如果通过code在缓存中获取为null,则通过codes调接口获取
            List<Device> apiDevices = getDevicesByApi(needFindCodes);
            if (StringUtils.isNotEmpty(apiDevices)) {
                devices.addAll(apiDevices);
            }
        }
        return devices;
    }


    /**
     * 通过code 获取网关信息
     * @param deviceCode
     * @return
     */
    public Device getDeviceByCode(String deviceCode) {
        Device device = redisUtils.getHashValue(CommonConstant.DEVICE_CODE_TO_DEVICE,deviceCode);

        List<String> needFindCodes = new ArrayList<String>();
        if(null == device) {
            List<Device> devices = getDevicesByApi(needFindCodes);
            if(StringUtils.isNotEmpty(devices)){
                device = devices.get(0);
            }
        }
        return device;
    }

    /**
     * api接口获取网关信息
     * @param needFindCodes
     */
    private List<Device> getDevicesByApi(List<String> needFindCodes){
        if(StringUtils.isNotEmpty(needFindCodes)) {
            List<Device> deviceList = deviceRepository.getDevicesByCodes(needFindCodes);
            if(StringUtils.isNotEmpty(deviceList)) {
                addDevices(deviceList);
                return deviceList;
            }
        }
        return null;
    }


    /**
     * 根据 code列表 获取网关状态
     * @param codes
     * @return
     */
    public Map<String,DeviceCommunicationStatus> getStatussByCodes(List<String> codes){
        List<DeviceCommunicationStatus> statusList = redisUtils.getHashMultiValue(CommonConstant.DEVICE_CODE_COMMUMICATION_STATUS,codes);
        Map<String,DeviceCommunicationStatus> resultMap = new HashMap<>();
        for (DeviceCommunicationStatus deviceCommunicationStatus : statusList) {
            if(StringUtils.isNotNull(deviceCommunicationStatus)) {
                resultMap.put(deviceCommunicationStatus.getGwid(), deviceCommunicationStatus);
            }
        }
        return resultMap;
    }

    /**
     * 更新网关状态
     * @param status
     */
    public void updateStatus(DeviceCommunicationStatus status) {
        redisUtils.addHashValue(CommonConstant.DEVICE_CODE_COMMUMICATION_STATUS,status.getGwid(),status,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
    }


    /**
     * 根据 code获取网关状态
     * @param code
     * @return
     */
    public DeviceCommunicationStatus getStatusByCode(String code) {
        DeviceCommunicationStatus status = redisUtils.getHashValue(CommonConstant.DEVICE_CODE_COMMUMICATION_STATUS,code);
        return status;
    }

}
