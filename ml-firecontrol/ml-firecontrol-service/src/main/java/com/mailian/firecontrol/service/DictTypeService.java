package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.firecontrol.dao.auto.model.DictType;
import com.mailian.firecontrol.dto.web.request.DictTypeReq;
import com.mailian.firecontrol.dto.web.request.DictTypeSearchReq;
import com.mailian.firecontrol.dto.web.response.DictTypeResp;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/1
 * @Description:
 */
public interface DictTypeService extends BaseService<DictType> {
    /**
     * 分页获取字典类型
     * @param searchReq
     * @return
     */
    PageBean<DictTypeResp> queryListByPage(DictTypeSearchReq searchReq);

    /**
     * 新增修改字典类型
     * @param dictTypeReq
     * @return
     */
    ResponseResult insertOrUpdate(DictTypeReq dictTypeReq);

    /**
     * 根据id删除字典类型
     * @param dictTypeId
     * @return
     */
    int deleteById(Integer dictTypeId);
}
