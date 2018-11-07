package com.mailian.firecontrol.service.impl;

import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.dto.SelectDto;
import com.mailian.firecontrol.dto.push.DeviceItem;
import com.mailian.firecontrol.dto.push.DeviceItemRealTimeData;
import com.mailian.firecontrol.service.DeviceItemOpertionService;
import com.mailian.firecontrol.service.repository.DeviceItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/7
 * @Description:
 */
@Service
public class DeviceItemOpertionServiceImpl implements DeviceItemOpertionService {
    @Autowired
    private DeviceItemRepository deviceItemRepository;


    //和工程人员约定  遥控的描述结构有两种
    //1：制冷 ,1代表下发值，制冷代表状态；
    //1:2:3:制冷,1代表状态值，2代表下发值，3代表是否可控，制冷代表状态
    @Override
    public String getYaoceStatus(String yaokongItemId, String statusItemId){
        String	status = "无";
        DeviceItemRealTimeData realTimeData = deviceItemRepository.getRtDataByItemId(statusItemId);
        if(StringUtils.isNull(realTimeData)|| realTimeData.getVal() == CommonConstant.ITEM_INITIAL_VALUE) {
            return status;
        }

        DeviceItem yaokongItem = deviceItemRepository.getDeviceItemInfosByItemId(yaokongItemId);
        DeviceItem statusItem = deviceItemRepository.getDeviceItemInfosByItemId(statusItemId);
        String ykDesc = StringUtils.isNull(yaokongItem)?"":yaokongItem.getDesc();
        String[] allType = ykDesc.split(CommonConstant.SPLIT_STR);

        for (String typeStr : allType) {
            String[] type = typeStr.split(":");
            if(type.length == 2) {
                status = realTimeData.getVal() == 1?statusItem.getDesc1():statusItem.getDesc0();
            }else {
                if(Integer.valueOf(type[0]) == realTimeData.getVal().intValue()) {
                    status = type[3];
                }
            }
        }
        return status;
    }

    @Override
    public List<SelectDto> getYaokongEnumList(String yaokongItemId){
        DeviceItem yaokongItem = deviceItemRepository.getDeviceItemInfosByItemId(yaokongItemId);
        String ykDesc = StringUtils.isNull(yaokongItem)?"":yaokongItem.getDesc();
        String[] allType = ykDesc.split(";");
        List<SelectDto> data = new ArrayList<SelectDto>();

        for(int i = 0; i<allType.length; i++) {
            SelectDto selectDto = new SelectDto();
            String[] type = allType[i].split(":");
            if(type.length == 2) {

                selectDto.setValue(Integer.valueOf(type[0]));
                selectDto.setName(type[1]);
            }else {
                selectDto.setName(type[3]);
                selectDto.setValue(Integer.valueOf(type[1]));
            }
            data.add(selectDto);
        }
        return data;
    }

}
