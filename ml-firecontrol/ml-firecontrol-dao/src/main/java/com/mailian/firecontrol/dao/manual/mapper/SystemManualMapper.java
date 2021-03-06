package com.mailian.firecontrol.dao.manual.mapper;

import com.mailian.firecontrol.dao.auto.model.FacilitiesAlarm;
import com.mailian.firecontrol.dao.auto.model.Precinct;
import com.mailian.firecontrol.dao.auto.model.Role;
import com.mailian.firecontrol.dao.manual.model.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统模块查询扩展
 *
 * @author wangqiaoqing
 */
public interface SystemManualMapper {
    List<Integer> selectMenuIdsByUserId(Integer userId);

    void deleteRoleMenuByMap(Map<String,Object> delRoleMenuMap);

    List<Role> selectRolesByUserId(Integer uid);

    int deleteUserRoleByMap(Map<String,Object> map);

    int deleteUserPrecinct(Map<String,Object> map);

    int countPrecinctByNameAndAreaId(@Param(value = "precinctName") String precinctName,@Param(value = "areaId") Integer areaId);

    List<Integer> selectUidsByPrecinctId(@Param(value = "precinctId") Integer precinctId);

    List<Integer> selectAdminUserIds();

    List<Integer> selectUidsByUnitId(@Param(value = "unitId") Integer unitId);

    List<UserVo> selectUsersByMap(Map<String,Object> map);

    List<Precinct> selectPrecinctsByMap(Map<String,Object> queryMap);

    void updateFacilitiesPrecinctIdByUnitId(@Param(value = "precinctId") Integer precinctId, @Param(value = "unitId") Integer unitId);

    void updateStructPrecinctIdByUnitId(@Param(value = "precinctId") Integer precinctId, @Param(value = "unitId") Integer unitId);

    void updateCameraPrecinctIdByUnitId(@Param(value = "precinctId") Integer precinctId, @Param(value = "unitId") Integer unitId);

    void updatePatrolPrecinctIdByUnitId(@Param(value = "precinctId") Integer precinctId, @Param(value = "unitId") Integer unitId);

    void updateAlarmInfoByUnitId(FacilitiesAlarm facilitiesAlarm);

    void updateAlarmInfoByPrecinctId(FacilitiesAlarm facilitiesAlarm);
}
