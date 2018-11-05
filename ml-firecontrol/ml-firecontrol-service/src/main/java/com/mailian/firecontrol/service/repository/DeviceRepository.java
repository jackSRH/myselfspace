package com.mailian.firecontrol.service.repository;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.mailian.core.util.HttpClientUtil;
import com.mailian.firecontrol.common.util.PushResponseUtil;
import com.mailian.firecontrol.dto.push.Device;
import com.mailian.firecontrol.dto.push.DevieceJFPGDate;
import com.mailian.firecontrol.service.config.PushConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/25
 * @Description:
 */
@Repository
public class DeviceRepository {
    @Autowired
    private PushConfig pushConfig;

    /**
     * 根据通讯机编号获取通讯机
     * @param codes
     * @return
     */
    public List<Device> getDevicesByCodes(List<String> codes){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        if(null != codes && !codes.isEmpty()) {
            params.put("gwids",CollectionUtil.join(codes,","));
        }

        String res = HttpClientUtil.get(pushConfig.getUrl(pushConfig.GET_DEVICE_URI), params);
        return PushResponseUtil.processResponseListData(res,Device.class);
    }

    /**
     * 设置通信机峰平谷时间段
     * @param jfpgDates
     * @return
     */
    @Async
    public Boolean setjfpgDate(List<DevieceJFPGDate> jfpgDates) {
        final Map<String,Object> params = new HashMap<String,Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("paras", jfpgDates);

        String data = JSON.toJSONString(params);
        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.SET_JFPGDATE_URI), data, null,false);
        return PushResponseUtil.processSuccess(res);
    }
}
