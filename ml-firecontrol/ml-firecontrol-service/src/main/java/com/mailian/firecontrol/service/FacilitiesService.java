package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.core.bean.PageBean;
import com.mailian.core.db.DataScope;
import com.mailian.firecontrol.dao.auto.model.Facilities;
import com.mailian.firecontrol.dao.manual.model.FaNumGySystem;
import com.mailian.firecontrol.dto.web.FacilitiesInfo;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.FacilitiesListResp;

import java.util.List;

public interface FacilitiesService extends BaseService<Facilities> {

    /**
     * 查找设施列表
     * @param searchReq
     * @param dataScope
     * @return
     */
    PageBean<FacilitiesListResp> getFacilitiesList(SearchReq searchReq, DataScope dataScope);

    /**
     * 查找设施列表
     * @param facilitiesInfo 设施信息
     * @return
     */
    Boolean insertOrUpdate(FacilitiesInfo facilitiesInfo);

    /**
     * 按照系统类型统计设施数量
     * @param unitIds 单位id列表
     * @return
     */
    List<FaNumGySystem> countFaNumGySystem(List<Integer> unitIds);

    int delFacilitiesById(Integer faId);
}
