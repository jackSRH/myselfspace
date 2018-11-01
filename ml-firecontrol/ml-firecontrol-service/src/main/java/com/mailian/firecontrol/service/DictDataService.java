package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.core.bean.ResponseResult;
import com.mailian.firecontrol.dao.auto.model.DictData;
import com.mailian.firecontrol.dto.web.request.DictDataReq;
import com.mailian.firecontrol.dto.web.response.DictDataResp;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/1
 * @Description:
 */
public interface DictDataService extends BaseService<DictData> {
    /**
     * 根据字典类型获取字典数据
     * @param dictType
     * @return
     */
    List<DictDataResp> queryDataByType(String dictType);

    /**
     * 根据字典类型获取字典数据 树结构
     * @param dictType
     * @return
     */
    List<DictDataResp> queryDataTreeByType(String dictType);

    /**
     * 新增或修改 字典数据
     * @param dictDataReq
     * @return
     */
    ResponseResult insertOrUpdate(DictDataReq dictDataReq);

    /**
     * 根据类型
     * @param dictData
     * @return
     */
    int deleteByDictData(DictData dictData);

}
