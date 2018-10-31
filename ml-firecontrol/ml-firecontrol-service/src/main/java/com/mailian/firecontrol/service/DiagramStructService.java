package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.firecontrol.dao.auto.model.DiagramStruct;
import com.mailian.firecontrol.dto.web.request.DiagramStructReq;
import com.mailian.firecontrol.dto.web.response.DiagramStructResp;

import java.util.List;
import java.util.Map;

public interface DiagramStructService extends BaseService<DiagramStruct> {
    /**
     * 新增系统图模块
     * @param diagramStructReq
     * @return
     */
    Boolean insert(DiagramStructReq diagramStructReq,String picPath);

    /**
     * 获取系统图模块信息
     * @param queryMap
     * @return
     */
    List<DiagramStructResp> getDiagramStructByMap(Map<String,Object> queryMap);

    /**
     * 删除系统图模块
     * @param dsId
     * @return
     */
    Boolean delete(Integer dsId);

    /**
     * 更新系统图模块
     * @param diagramStructReq
     * @param picPath 图片路径
     * @return
     */
    Boolean update(DiagramStructReq diagramStructReq,String picPath);

}
