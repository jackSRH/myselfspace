package com.mailian.firecontrol.dao.manual.mapper;

import com.mailian.firecontrol.dto.UnitRedisInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/1
 * @Description:
 */
public interface UnitManualMapper {

    List<UnitRedisInfo> selectUnitDeviceByDeviceIds(@Param(value = "deviceIds") List<String> deviceIds);

    List<String> selectDevices();

    int deleteDeviceByUnitId(@Param(value = "unitId") Integer unitId);
}
