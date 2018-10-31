package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.core.bean.PageBean;
import com.mailian.firecontrol.dao.auto.model.Facilities;
import com.mailian.firecontrol.dto.web.FacilitiesInfo;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.FacilitiesListResp;

public interface FacilitiesService extends BaseService<Facilities> {

    /**
     * 查找设施列表
     * @param searchReq
     * @return
     */
    PageBean<FacilitiesListResp> getFacilitiesList(SearchReq searchReq);

    /**
     * 查找设施列表
     * @param facilitiesInfo 设施信息
     * @return
     */
    Boolean insertOrUpdate(FacilitiesInfo facilitiesInfo);

}
