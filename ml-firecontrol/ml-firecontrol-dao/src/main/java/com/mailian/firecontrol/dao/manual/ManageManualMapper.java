package com.mailian.firecontrol.dao.manual;


import com.mailian.firecontrol.dao.auto.model.FacilitiesAlarm;

import java.util.List;
import java.util.Map;


public interface ManageManualMapper {
    void deleteDiagramItemByMap(Map<String, Object> queryMap);
    List<FacilitiesAlarm> selectFacilitiesAlarmByMap(Map<String,Object> queryMap);
}
