package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.dao.auto.mapper.AreaMapper;
import com.mailian.firecontrol.dao.auto.model.Area;
import com.mailian.firecontrol.dto.web.response.AreaResp;
import com.mailian.firecontrol.service.AreaService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/31
 * @Description:
 */
@Service
public class AreaServiceImpl extends BaseServiceImpl<Area,AreaMapper> implements AreaService {

    @Cacheable(value = CommonConstant.SYS_AREA_KEY)
    @Override
    public List<AreaResp> selectAll() {
        List<Area> areaList = selectByMap(null);

        List<AreaResp> areaResps = new ArrayList<>();
        AreaResp areaResp;
        for (Area area : areaList) {
            areaResp = new AreaResp();
            BeanUtils.copyProperties(area,areaResp);
            areaResps.add(areaResp);
        }
        return areaResps;
    }
}
