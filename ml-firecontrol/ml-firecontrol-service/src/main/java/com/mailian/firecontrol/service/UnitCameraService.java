package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.core.db.DataScope;
import com.mailian.firecontrol.dao.auto.model.UnitCamera;
import com.mailian.firecontrol.dto.web.CameraInfo;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.CameraListResp;

import java.util.List;

public interface UnitCameraService extends BaseService<UnitCamera> {

    /**
     * 新增更新摄像头
     * @param cameraInfo 摄像头信息
     * @return
     */
    Boolean insertOrUpdate(CameraInfo cameraInfo);

    /**
     * 查找摄像头列表
     * @param searchReq
     * @param dataScope
     * @return
     */
    List<CameraListResp> getCameraList(DataScope dataScope, SearchReq searchReq);

    /**
     * 根据单位获取摄像头信息
     * @param unitId
     * @return
     */
    List<CameraListResp> getListByUnitId(Integer unitId);
}
