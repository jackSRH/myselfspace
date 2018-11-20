package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.util.DateUtil;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.FindHistoryDataType;
import com.mailian.firecontrol.common.enums.HistoryDataType;
import com.mailian.firecontrol.dao.auto.mapper.PowerMonitoringMapper;
import com.mailian.firecontrol.dao.auto.model.PowerMonitoring;
import com.mailian.firecontrol.dto.DateValue;
import com.mailian.firecontrol.dto.push.Device;
import com.mailian.firecontrol.dto.push.DeviceItem;
import com.mailian.firecontrol.dto.push.DeviceItemHistoryData;
import com.mailian.firecontrol.dto.push.DeviceItemRealTimeData;
import com.mailian.firecontrol.dto.web.request.PowerMonitoringReq;
import com.mailian.firecontrol.dto.web.response.VoltageComparedResp;
import com.mailian.firecontrol.dto.web.response.VoltageItemComparedResp;
import com.mailian.firecontrol.dto.web.response.YtHisDataResp;
import com.mailian.firecontrol.dto.web.response.LoadComparedResp;
import com.mailian.firecontrol.service.PowerMonitoringService;
import com.mailian.firecontrol.service.repository.DeviceItemRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PowerMonitoringServiceImpl extends BaseServiceImpl<PowerMonitoring, PowerMonitoringMapper> implements PowerMonitoringService {

    @Resource
    private DeviceItemRepository deviceItemRepository;

    @Override
    public ResponseResult insertOrUpdate(PowerMonitoringReq powerMonitoringReq){
        PowerMonitoring powerMonitoring = new PowerMonitoring();
        BeanUtils.copyProperties(powerMonitoringReq,powerMonitoring);
        int res = 0;
        if(StringUtils.isEmpty(powerMonitoring.getId())){
           res = super.insert(powerMonitoring);
        }else{
            res = super.updateByPrimaryKeySelective(powerMonitoring);
        }
        return res >0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }


    @Override
    public LoadComparedResp getLoadCompared(String itemId){
        LoadComparedResp loadComparedResp = new LoadComparedResp();
        DeviceItemRealTimeData rtData = deviceItemRepository.getRtDataByItemId(itemId);
        if(StringUtils.isNotNull(rtData)){
            loadComparedResp.setRtData(rtData.getVal());
            loadComparedResp.setLastUpdateTime(rtData.getLastUpdateTime());
        }

        Date endDate = new Date();
        Date startDate = DateUtil.getStartDate(DateUtil.getDateBeforeDay(endDate,1)) ;
        List<DeviceItemHistoryData> historyDatas = deviceItemRepository.getItemDataByItemIdAndTime(itemId,DateUtil.toString(startDate),DateUtil.toString(endDate), FindHistoryDataType.HOUR.id);
        if(StringUtils.isEmpty(historyDatas)){
            return loadComparedResp;
        }

        List<DateValue> yesterdayVals = new ArrayList<>();
        List<DateValue> todayVals = new ArrayList<>();
        dealHisDatas(historyDatas,startDate,endDate,yesterdayVals,todayVals);

        List<YtHisDataResp> ytHisDataResps = new ArrayList<>();
        YtHisDataResp yesHisData = new YtHisDataResp();
        yesHisData.setDateType(0);
        yesHisData.setDateValues(yesterdayVals);
        ytHisDataResps.add(yesHisData);

        YtHisDataResp todayHisData = new YtHisDataResp();
        todayHisData.setDateType(1);
        todayHisData.setDateValues(todayVals);
        ytHisDataResps.add(todayHisData);
        loadComparedResp.setHisDatas(ytHisDataResps);
        return loadComparedResp;
    }

    @Override
    public VoltageComparedResp getVoltageCompared(String itemIds){
        VoltageComparedResp voltageComparedResp = new VoltageComparedResp();
        List<String> itemIdList = Arrays.asList(itemIds.split(","));

        //数据项信息
        Map<String,DeviceItem> itemId2ItemInfo = new HashMap<>();
        for(String itemId:itemIdList){
            DeviceItem item = deviceItemRepository.getDeviceItemInfosByItemId(itemId);
            itemId2ItemInfo.put(itemId,item);
        }

        //数据项实时值
        Map<String,Float> itemId2RtVal = new HashMap<>();
        List<DeviceItemRealTimeData> deviceItemRealTimeDatas = deviceItemRepository.getRtDataByItemIds(itemIdList);
        if(StringUtils.isNotEmpty(deviceItemRealTimeDatas)){
            Date lastUpdateTime = deviceItemRealTimeDatas.get(0).getLastUpdateTime();
            for(DeviceItemRealTimeData deviceItemRealTimeData : deviceItemRealTimeDatas){
                itemId2RtVal.put(deviceItemRealTimeData.getId(),deviceItemRealTimeData.getVal());
                if(lastUpdateTime.before(deviceItemRealTimeData.getLastUpdateTime())){
                    lastUpdateTime = deviceItemRealTimeData.getLastUpdateTime();
                }
            }
            voltageComparedResp.setLastUpdateTime(lastUpdateTime);
        }

        Date endDate = new Date();
        Date startDate = DateUtil.getStartDate(DateUtil.getDateBeforeDay(endDate,1)) ;
        List<DeviceItemHistoryData> historyDatas = deviceItemRepository.getItemDataByItemIdAndTime(itemIds,DateUtil.toString(startDate),DateUtil.toString(endDate), FindHistoryDataType.HOUR.id);

        List<VoltageItemComparedResp> voltageItemComparedResps = new ArrayList<>();
        VoltageItemComparedResp voltageItemComparedResp;
        DeviceItem deviceItem;
        if(StringUtils.isEmpty(historyDatas)){
            for(Map.Entry<String,DeviceItem> entry : itemId2ItemInfo.entrySet()){
                deviceItem = entry.getValue();
                voltageItemComparedResp = new VoltageItemComparedResp();
                if(StringUtils.isNotNull(deviceItem)){
                    voltageItemComparedResp.setItemName(deviceItem.getShortname());
                    voltageItemComparedResp.setShortName(deviceItem.getDisplayname());
                }
                voltageItemComparedResp.setRtData(StringUtils.nvl(itemId2RtVal.get(entry.getKey()),0f));
                voltageItemComparedResps.add(voltageItemComparedResp);
            }
            voltageComparedResp.setVoltageItemComparedResps(voltageItemComparedResps);
            return voltageComparedResp;
        }

        //区分数据项的历史数据
        Map<String,List<DeviceItemHistoryData>> itemId2HisDatas = new HashMap<>();
        String itemId;
        List<DeviceItemHistoryData> hisDatas ;
        for(DeviceItemHistoryData deviceItemHistoryData:historyDatas){
            if(deviceItemHistoryData.getCtg() != HistoryDataType.TOTAL.id.intValue()){
                continue;
            }
            itemId = deviceItemHistoryData.getId();
            hisDatas = itemId2HisDatas.containsKey(itemId)?itemId2HisDatas.get(itemId):new ArrayList<>();
            hisDatas.add(deviceItemHistoryData);
            itemId2HisDatas.put(itemId,hisDatas);
        }

        List<DateValue> yesterdayVals;
        List<DateValue> todayVals;
        List<YtHisDataResp> ytHisDataResps ;
        YtHisDataResp ytHisDataResp;
        for(Map.Entry<String,List<DeviceItemHistoryData>> entry : itemId2HisDatas.entrySet()){
            voltageItemComparedResp = new VoltageItemComparedResp();
            deviceItem = itemId2ItemInfo.get(entry.getKey());
            if(StringUtils.isNotNull(deviceItem)){
                voltageItemComparedResp.setItemName(deviceItem.getShortname());
                voltageItemComparedResp.setShortName(deviceItem.getDisplayname());
                voltageItemComparedResp.setRtData(StringUtils.nvl(itemId2RtVal.get(entry.getKey()),0f));
            }

            yesterdayVals = new ArrayList<>();
            todayVals = new ArrayList<>();
            dealHisDatas(entry.getValue(),startDate,endDate,yesterdayVals,todayVals);
            ytHisDataResps = new ArrayList<>();

            ytHisDataResp = new YtHisDataResp();
            ytHisDataResp.setDateType(0);
            ytHisDataResp.setDateValues(yesterdayVals);
            ytHisDataResps.add(ytHisDataResp);

            ytHisDataResp = new YtHisDataResp();
            ytHisDataResp.setDateType(1);
            ytHisDataResp.setDateValues(todayVals);
            ytHisDataResps.add(ytHisDataResp);
            voltageItemComparedResp.setYtHisDatas(ytHisDataResps);
            voltageItemComparedResps.add(voltageItemComparedResp);
        }
        voltageComparedResp.setVoltageItemComparedResps(voltageItemComparedResps);
        return voltageComparedResp;
    }


    private void dealHisDatas(List<DeviceItemHistoryData> historyDatas,Date startDate,Date endDate,List<DateValue> yesterdayVals,List<DateValue> todayVals){
        String date;
        Float value;
        Map<String,Float> date2Val = new HashMap<>();
        for (DeviceItemHistoryData historyData : historyDatas){
            if(historyData.getCtg() != HistoryDataType.TOTAL.id.intValue()){
                continue;
            }
            date = DateUtil.toString(historyData.getTm(),DateUtil.DATE_FORMAT_FOR_YMDH);
            value = date2Val.containsKey(date)?date2Val.get(date):0f;
            value += historyData.getVal();
            date2Val.put(date,value);
        }

        List<String> times = DateUtil.getHoursBetween(startDate,endDate);
        String yesterday = DateUtil.toString(startDate,DateUtil.DATE_FORMAT_FOR_YMD);
        DateValue dateValue;
        for(String time:times){
            dateValue = new DateValue();
            dateValue.setDate(time);
            value = StringUtils.nvl(date2Val.get(time),0f);
            dateValue.setValue(value);
            if(time.contains(yesterday)){
                yesterdayVals.add(dateValue);
            }else{
                todayVals.add(dateValue);
            }
        }

        //排序
        Collections.sort(yesterdayVals, new Comparator<DateValue>() {
            public int compare(DateValue o1, DateValue o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        Collections.sort(todayVals, new Comparator<DateValue>() {
            public int compare(DateValue o1, DateValue o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
    }


}
