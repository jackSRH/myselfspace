package com.mailian.firecontrol.dao.manual.mapper;


import com.mailian.firecontrol.dao.auto.model.DiagramStruct;
import com.mailian.firecontrol.dao.auto.model.FacilitiesAlarm;
import com.mailian.firecontrol.dao.manual.model.FaNumGySystem;
import com.mailian.firecontrol.dto.DiagramItemDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface ManageManualMapper {
    void deleteDiagramItemByMap(Map<String, Object> queryMap);

    List<FacilitiesAlarm> selectFacilitiesAlarmByMap(Map<String,Object> queryMap);

    DiagramStruct selectDiagramStructByAlarmItemId(@Param(value = "itemId") String itemId, @Param(value = "unitId") Integer unitId);

    int updateFaAlarmBatch(@Param(value = "alarmList") List<FacilitiesAlarm> upFacilitiesAlarmList);

    int countUnfinishAlarmNumByType(Integer alaramType);

    List<FaNumGySystem> countFaNumGySystem(@Param(value = "unitIds") List<Integer> unitIds);


    List<DiagramItemDto> selectDiagramItemByMap(Map<String,Object> queryMap);

    List<Map<String,Object>> countFaTypeNumByMap(Map<String,Object> queryMap);

    List<Map<String,Object>> countFaRealNumByMap(Map<String,Object> queryMap);
}
