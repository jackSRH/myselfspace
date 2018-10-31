package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.core.bean.PageBean;
import com.mailian.core.db.DataScope;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dto.web.UnitInfo;
import com.mailian.firecontrol.dto.web.response.UnitListResp;

public interface UnitService extends BaseService<Unit> {

    /**
     * 查找单位列表
     * @param dataScope 管辖区id列表
     * @param unitName 单位名称 支持模糊查询
     * @param currentPage 页数
     * @param pageSize 每页条数
     * @return
     */
    PageBean<UnitListResp> getUnitList(DataScope dataScope, String unitName, Integer currentPage, Integer pageSize);

    /**
     * 新增或者更新单位信息
     * @param unitInfo
     * @return
     */
    Boolean insertOrUpdate(UnitInfo unitInfo);

}
