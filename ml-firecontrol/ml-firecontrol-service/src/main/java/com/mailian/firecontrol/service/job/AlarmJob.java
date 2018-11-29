package com.mailian.firecontrol.service.job;

import com.mailian.core.util.DateUtil;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.AlarmHandleStatus;
import com.mailian.firecontrol.common.enums.SysConfigType;
import com.mailian.firecontrol.dao.auto.model.FacilitiesAlarm;
import com.mailian.firecontrol.dao.auto.model.Precinct;
import com.mailian.firecontrol.dao.auto.model.SysConfig;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dto.AlarmRemindInfo;
import com.mailian.firecontrol.service.*;
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
    @Autowired
    private SysConfigService sysConfigService;

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
        SysConfig sysConfig = sysConfigService.getConfigByType(SysConfigType.ALARM_RESPONSE_OVERTIME);

        int minute = -3;
        if(StringUtils.isNotNull(sysConfig) && StringUtils.isNotEmpty(sysConfig.getConfigValue())){
            minute = Integer.parseInt(sysConfig.getConfigValue());
        }

        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("endAlarmTime",DateUtil.addMinute(new Date(),minute));
        queryMap.put("handleStatus",AlarmHandleStatus.UNTREATED.id);
        List<FacilitiesAlarm> alarms = facilitiesAlarmService.selectFacilitiesAlarmByMap(queryMap);
        List<AlarmRemindInfo> remindInfos = new ArrayList<>();
        if(StringUtils.isNotEmpty(alarms)){
            Map<Integer,Unit> unitMap = new HashMap<>();
            Map<Integer,Precinct> precinctMap = new HashMap<>();
            for (FacilitiesAlarm alarm : alarms) {
                if(StringUtils.isNotEmpty(alarm.getUnitId())) {
                    unitMap.put(alarm.getUnitId(), null);
                }
                if(StringUtils.isNotEmpty(alarm.getPrecinctId())){
                    precinctMap.put(alarm.getPrecinctId(),null);
                }
            }
            if(StringUtils.isNotEmpty(unitMap)){
                List<Unit> unitList = unitService.selectBatchIds(unitMap.keySet());
                for (Unit unit : unitList) {
                    unitMap.put(unit.getId(),unit);
                }
            }
            if(StringUtils.isNotEmpty(precinctMap)){
                List<Precinct> precincts = precinctService.selectBatchIds(precinctMap.keySet());
                for (Precinct precinct : precincts) {
                    precinctMap.put(precinct.getId(),precinct);
                }
            }


            for (FacilitiesAlarm facilitiesAlarm : alarms) {
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

                if (StringUtils.isNotEmpty(facilitiesAlarm.getUnitId())) {
                    Unit unit = unitMap.get(facilitiesAlarm.getUnitId());
                    Precinct precinct = precinctMap.get(facilitiesAlarm.getPrecinctId());
                    if (StringUtils.isNotNull(unit)) {
                        alarmRemindInfo.setUnitName(unit.getUnitName());
                        alarmRemindInfo.setLng(unit.getLng());
                        alarmRemindInfo.setLat(unit.getLat());
                    }

                    if (StringUtils.isNotNull(precinct)) {
                        alarmRemindInfo.setDutyPhone(precinct.getDutyPhone());
                        alarmRemindInfo.setDutyName(precinct.getDutyName());
                    }
                }
                remindInfos.add(alarmRemindInfo);
            }
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
