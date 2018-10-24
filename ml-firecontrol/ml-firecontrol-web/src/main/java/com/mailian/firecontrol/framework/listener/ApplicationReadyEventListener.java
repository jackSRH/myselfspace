package com.mailian.firecontrol.framework.listener;

import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.framework.util.MqttTopicUtil;
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

    /*@Autowired
    private DeviceRelationService deviceRelationService;*/

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        /*List<DeviceRelation> deviceRelations =  deviceRelationService.selectByMap(null);
        Set<String> deviceIds = new HashSet<>();
        for (DeviceRelation deviceRelation : deviceRelations) {
            deviceIds.add(deviceRelation.getDid());
        }

        if(StringUtils.isNotEmpty(deviceIds)){
            for (String deviceId : deviceIds) {
                //订阅网关状态
                MqttTopicUtil.addDefaultTopic(deviceId);
            }
        }*/
    }
}
