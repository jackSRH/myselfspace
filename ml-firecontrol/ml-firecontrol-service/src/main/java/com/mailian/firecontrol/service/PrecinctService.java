package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.core.bean.PageBean;
import com.mailian.core.db.DataScope;
import com.mailian.firecontrol.dao.auto.model.Precinct;
import com.mailian.firecontrol.dto.web.request.PrecinctQueryReq;
import com.mailian.firecontrol.dto.web.request.PrecinctReq;
import com.mailian.firecontrol.dto.web.response.PrecinctResp;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/31
 * @Description:
 */
public interface PrecinctService extends BaseService<Precinct> {
    /**
     * 按条件搜索分页
     * @param queryReq
     * @return
     */
    PageBean<PrecinctResp> queryByPage(PrecinctQueryReq queryReq, DataScope dataScope);

    /**
     * 新增或修改管辖区
     * @param precinctReq
     * @return
     */
    int insertOrUpdate(PrecinctReq precinctReq);
}
