package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.firecontrol.dao.auto.model.Area;
import com.mailian.firecontrol.dto.web.response.AreaResp;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/31
 * @Description:
 */
public interface AreaService extends BaseService<Area> {

    /**
     * 获取所有省份城市区域
     * @return
     */
    List<AreaResp> selectAll();
}
