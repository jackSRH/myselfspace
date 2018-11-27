package com.mailian.firecontrol.framework.listener;

import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.dao.auto.model.UnitDevice;
import com.mailian.firecontrol.framework.util.MqttTopicUtil;
import com.mailian.firecontrol.service.UnitDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/29
 * @Description:
 */
@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private UnitDeviceService unitDeviceService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        List<UnitDevice> unitDevices = unitDeviceService.selectByMap(null);
        Set<String> deviceIds = new HashSet<>();
        for (UnitDevice unitDevice : unitDevices) {
            deviceIds.add(unitDevice.getDeviceId());
        }

        if(StringUtils.isNotEmpty(deviceIds)){
            for(String deviceId : deviceIds){
                MqttTopicUtil.addDefaultTopic(deviceId);
            }
        }
    }
}
