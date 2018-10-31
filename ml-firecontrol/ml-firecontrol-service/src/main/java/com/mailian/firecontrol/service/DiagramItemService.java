package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.firecontrol.dao.auto.model.DiagramItem;
import com.mailian.firecontrol.dto.web.request.DiagramItemReq;

import java.util.List;

public interface DiagramItemService extends BaseService<DiagramItem> {

    /**
     * 查找模块数据项
     * @param structIds
     * @return
     */
    List<DiagramItem> getItemsByStructIds(List<Integer> structIds);

    /**
     * 新增模块数据项
     * @param diagramItemReqs 数据项参数
     * @param dsId 模块id
     * @return
     */
    Boolean insert(List<DiagramItemReq> diagramItemReqs,Integer dsId);

    void deleteByDsId(Integer dsId);

}
