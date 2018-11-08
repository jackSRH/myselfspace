package com.mailian.firecontrol.dao.manual.mapper;

import com.mailian.firecontrol.dao.auto.model.UnitDevice;
import com.mailian.firecontrol.dto.UnitRedisInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/1
 * @Description:
 */
public interface UnitManualMapper {

    List<UnitRedisInfo> selectUnitDeviceByDeviceIds(@Param(value = "deviceIds") List<String> deviceIds);

    List<String> selectDevicesExcludeUnitId(@Param(value = "unitId") Integer unitId);

    List<String> selectDevicesByUnitId(@Param(value = "unitId") Integer unitId);

    int deleteDeviceByUnitId(@Param(value = "unitId") Integer unitId);

    List<UnitDevice> selectUnitDeviceByMap(Map<String,Object> queryMap);

    int updateUnitDeviceBatch(@Param(value = "upList") List<UnitDevice> upList);
}
