package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.core.bean.ResponseResult;
import com.mailian.firecontrol.dao.auto.model.PowerMonitoring;
import com.mailian.firecontrol.dto.web.request.PowerMonitoringReq;
import com.mailian.firecontrol.dto.web.response.LoadComparedResp;
import com.mailian.firecontrol.dto.web.response.VoltageComparedResp;

public interface PowerMonitoringService extends BaseService<PowerMonitoring> {
    /**
     * 新增或者更新用电监控配置
     * @return
     */
    ResponseResult insertOrUpdate(PowerMonitoringReq powerMonitoringReq);

    /**
     * 获取负荷今昨对比
     * @return
     */
    LoadComparedResp getLoadCompared(String itemId);

    /**
     * 获取电压今昨对比
     * @return
     */
    VoltageComparedResp getVoltageCompared(String itemIds);
}
