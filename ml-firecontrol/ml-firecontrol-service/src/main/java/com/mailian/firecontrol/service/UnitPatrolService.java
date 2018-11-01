package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.core.bean.PageBean;
import com.mailian.core.db.DataScope;
import com.mailian.firecontrol.dao.auto.model.UnitPatrol;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.request.UnitPatrolReq;
import com.mailian.firecontrol.dto.web.response.UnitPatrolListResp;

public interface UnitPatrolService extends BaseService<UnitPatrol> {

    /**
     * 新增巡查记录
     * @param uid 巡查人id
     * @param unitPatrolReq
     * @return
     */
    Boolean insert(Integer uid, UnitPatrolReq unitPatrolReq);

    /**
     * 获取巡查记录列表
     * @param dataScope
     * @param searchReq
     * @return
     */
     PageBean<UnitPatrolListResp> getUnitPatrolList(DataScope dataScope, SearchReq searchReq);

}
