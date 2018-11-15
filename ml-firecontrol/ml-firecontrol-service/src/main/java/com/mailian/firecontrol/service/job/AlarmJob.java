package com.mailian.firecontrol.service.job;

import com.mailian.core.util.DateUtil;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.AlarmHandleStatus;
import com.mailian.firecontrol.dao.auto.model.FacilitiesAlarm;
import com.mailian.firecontrol.dao.auto.model.Precinct;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dto.AlarmRemindInfo;
import com.mailian.firecontrol.service.AlarmOpertionService;
import com.mailian.firecontrol.service.FacilitiesAlarmService;
import com.mailian.firecontrol.service.PrecinctService;
import com.mailian.firecontrol.service.UnitService;
import com.mailian.firecontrol.service.cache.RemindCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/5
 * @Description:
 */
@Component
public class AlarmJob {
    private static final Logger log = LoggerFactory.getLogger(AlarmJob.class);
    @Autowired
    private AlarmOpertionService alarmOpertionService;
    @Autowired
    private FacilitiesAlarmService facilitiesAlarmService;
    @Autowired
    private RemindCache remindCache;
    @Autowired
    private UnitService unitService;
    @Autowired
    private PrecinctService precinctService;

    /**
     * 补全缺失的告警
     */
    public void completionEquipmentAlarm(){
        alarmOpertionService.completionEquipmentAlarm();
    }


    /**
     * 响应超时告警(3分钟)
     */
    public void dealTimeoutAlarm(){
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("endAlarmTime",DateUtil.addMinute(new Date(),-3));
        queryMap.put("handleStatus",AlarmHandleStatus.UNTREATED.id);
        List<FacilitiesAlarm> alarms = facilitiesAlarmService.selectFacilitiesAlarmByMap(queryMap);
        List<AlarmRemindInfo> remindInfos = new ArrayList<>();
        if(StringUtils.isNotEmpty(alarms)){
            FacilitiesAlarm facilitiesAlarm = alarms.get(0);
            AlarmRemindInfo alarmRemindInfo = new AlarmRemindInfo();
            alarmRemindInfo.setAlarmId(facilitiesAlarm.getAlarmId());
            alarmRemindInfo.setAddress(facilitiesAlarm.getStructAddress());
            alarmRemindInfo.setAlarmContent(facilitiesAlarm.getAlarmContent());
            alarmRemindInfo.setAlarmTime(facilitiesAlarm.getAlarmTime());

            alarmRemindInfo.setProvinceId(facilitiesAlarm.getProvinceId());
            alarmRemindInfo.setCityId(facilitiesAlarm.getCityId());
            alarmRemindInfo.setAreaId(facilitiesAlarm.getAreaId());
            alarmRemindInfo.setPrecinctId(facilitiesAlarm.getPrecinctId());
            alarmRemindInfo.setUnitId(facilitiesAlarm.getUnitId());

            if(StringUtils.isNotEmpty(facilitiesAlarm.getUnitId())){
                Unit unit = unitService.selectByPrimaryKey(facilitiesAlarm.getUnitId());
                Precinct precinct = precinctService.selectByPrimaryKey(unit.getPrecinctId());
                alarmRemindInfo.setUnitName(unit.getUnitName());
                alarmRemindInfo.setDutyName(precinct.getDutyName());
                alarmRemindInfo.setDutyPhone(precinct.getDutyPhone());
                alarmRemindInfo.setLng(unit.getLng());
                alarmRemindInfo.setLat(unit.getLat());
            }
            remindInfos.add(alarmRemindInfo);
        }

        if(StringUtils.isNotEmpty(remindInfos)) {
            Collections.sort(remindInfos, new Comparator<AlarmRemindInfo>() {
                @Override
                public int compare(AlarmRemindInfo o1, AlarmRemindInfo o2) {
                    return o2.getAlarmTime().compareTo(o1.getAlarmTime());
                }
            });
            remindCache.addReminds(remindInfos);
        }
    }
}
