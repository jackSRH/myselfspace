package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.firecontrol.dao.auto.model.UnitDevice;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/1
 * @Description:
 */
public interface UnitDeviceService extends BaseService<UnitDevice> {
    /**
     * 根据网关id 获取关联关系
     * @param deviceIds
     * @return
     */
    List<UnitDevice> selectByDeviceIds(List<String> deviceIds);

    /**
     * 批量修改关联关系
     * @param upList
     * @return
     */
    int updateByList(List<UnitDevice> upList);
}
