package com.mailian.firecontrol.dao.manual;


import com.mailian.firecontrol.dao.auto.model.DiagramStruct;
import com.mailian.firecontrol.dao.auto.model.FacilitiesAlarm;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface ManageManualMapper {
    void deleteDiagramItemByMap(Map<String, Object> queryMap);
    List<FacilitiesAlarm> selectFacilitiesAlarmByMap(Map<String,Object> queryMap);


    DiagramStruct selectDiagramStructByAlarmItemId(@Param(value = "itemId") String itemId, @Param(value = "unitId") Integer unitId);

    int updateFaAlarmBatch(@Param(value = "alarmList") List<FacilitiesAlarm> upFacilitiesAlarmList);
}
