package com.mailian.firecontrol.dao.manual;

import com.mailian.firecontrol.dao.auto.model.Role;
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
}
