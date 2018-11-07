package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.firecontrol.dao.auto.mapper.UnitDeviceMapper;
import com.mailian.firecontrol.dao.auto.model.UnitDevice;
import com.mailian.firecontrol.dao.manual.mapper.UnitManualMapper;
import com.mailian.firecontrol.service.UnitDeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/1
 * @Description:
 */
@Service
public class UnitDeviceServiceImpl extends BaseServiceImpl<UnitDevice,UnitDeviceMapper> implements UnitDeviceService {
    @Resource
    private UnitManualMapper unitManualMapper;

    @Override
    public List<UnitDevice> selectByDeviceIds(List<String> deviceIds) {
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("deviceIds",deviceIds);
        return unitManualMapper.selectUnitDeviceByMap(queryMap);
    }

    @Override
    public int updateByList(List<UnitDevice> upList) {
        return unitManualMapper.updateUnitDeviceBatch(upList);
    }
}
