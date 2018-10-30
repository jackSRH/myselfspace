package com.mailian.firecontrol.service.repository;

import com.alibaba.fastjson.JSON;
import com.mailian.core.util.HttpClientUtil;
import com.mailian.firecontrol.common.util.PushResponseUtil;
import com.mailian.firecontrol.dto.push.Alarm;
import com.mailian.firecontrol.dto.push.YcAlarmConfig;
import com.mailian.firecontrol.dto.push.YxAlarmConfig;
import com.mailian.firecontrol.service.config.PushConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/25
 * @Description:
 */
@Repository
public class AlarmRepository {
    @Autowired
    private DeviceItemRepository deviceItemRepository;
    @Autowired
    private PushConfig pushConfig;

    /**
     * 遥测告警参数设置
     * @param ycAlarmConfigs
     * @param deviceCodes
     * @return
     */
    public Boolean setYcAlarmConfig(List<YcAlarmConfig> ycAlarmConfigs, String deviceCodes) {
        final Map<String,Object> params = new HashMap<String,Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("paras", ycAlarmConfigs);
        String data = JSON.toJSONString(params);

        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.SET_ALARM_URI), data, null,false);
        boolean updateRes = PushResponseUtil.processSuccess(res);
        if(updateRes) {
            //修改成功则更新缓存
            deviceItemRepository.upateCacheAfterUpdateAlarmStoreItems(Arrays.asList(deviceCodes.split(",")));
        }
        return updateRes;
    }

    /**
     * 遥信告警参数设置
     * @param yxAlarmConfigs
     * @param deviceCodes
     * @return
     */
    public 	Boolean setYxAlarmConfig(List<YxAlarmConfig> yxAlarmConfigs, String deviceCodes) {
        final Map<String,Object> params = new HashMap<String,Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("paras", yxAlarmConfigs);
        String data = JSON.toJSONString(params);

        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.SET_YXALARM_URI), data, null,false);
        boolean updateRes = PushResponseUtil.processSuccess(res);
        if(updateRes) {
            //修改成功则更新缓存
            deviceItemRepository.upateCacheAfterUpdateAlarmStoreItems(Arrays.asList(deviceCodes.split(",")));
        }
        return updateRes;
    }


    /**
     * 通过通信机code(多个code用，隔开)，开始时间，结束时间，pinding
     * @param deviceCodes 通信机code列表
     * @param stime
     * @param etime
     * @param pinding 0：获取所有告警；1：获取所有未结束的告警
     * @param tflag 是按哪种时间查询，1是告警结束时间，2是告警记录更新时间，其余的都是告警开始时间
     * @return
     */
    public List<Alarm> getAlarmInfoByDiviceCodesAndType(String deviceCodes, String stime,
                                                        String etime, Integer pinding, Integer tflag){
        final Map<String,Object> params = new HashMap<String,Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("gwids", deviceCodes);
        params.put("stime", stime);
        params.put("etime", etime);
        params.put("pending", pinding);
        params.put("tflag",tflag);
        String param = JSON.toJSONString(params);
        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.GET_ALARM_URI), param, null,false);
        return PushResponseUtil.processResponseListData(res);
    }

    public List<Alarm> getAlarmInfoByItemsAndType(String itemids,String stime,String etime,Integer pinding){
        final Map<String,Object> params = new HashMap<String,Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("itemids", itemids);
        params.put("stime", stime);
        params.put("etime", etime);
        params.put("pending", pinding);
        String param = JSON.toJSONString(params);
        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.GET_ALARM_URI), param, null,false);
        return PushResponseUtil.processResponseListData(res);
    }
}
