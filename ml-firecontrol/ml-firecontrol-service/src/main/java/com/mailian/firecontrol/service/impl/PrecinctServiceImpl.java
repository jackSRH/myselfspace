package com.mailian.firecontrol.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.bean.PageBean;
import com.mailian.core.db.DataScope;
import com.mailian.core.enums.ResponseCode;
import com.mailian.core.exception.RequestException;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.dao.auto.mapper.PrecinctMapper;
import com.mailian.firecontrol.dao.auto.mapper.UnitMapper;
import com.mailian.firecontrol.dao.auto.model.Area;
import com.mailian.firecontrol.dao.auto.model.Precinct;
import com.mailian.firecontrol.dao.manual.mapper.SystemManualMapper;
import com.mailian.firecontrol.dto.web.request.PrecinctQueryReq;
import com.mailian.firecontrol.dto.web.request.PrecinctReq;
import com.mailian.firecontrol.dto.web.response.PrecinctResp;
import com.mailian.firecontrol.service.AreaService;
import com.mailian.firecontrol.service.PrecinctService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/31
 * @Description:
 */
@Service
public class PrecinctServiceImpl extends BaseServiceImpl<Precinct,PrecinctMapper> implements PrecinctService {
    @Autowired
    private AreaService areaService;
    @Resource
    private UnitMapper unitMapper;
    @Resource
    private SystemManualMapper systemManualMapper;

    @Override
    public PageBean<PrecinctResp> queryByPage(PrecinctQueryReq queryReq, DataScope dataScope) {
        Integer currentPage = queryReq.getCurrentPage();
        Integer pageSize = queryReq.getPageSize();
        Page page = PageHelper.startPage(currentPage,pageSize);
        page.setOrderBy("id desc");

        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("provinceId",queryReq.getProvinceId());
        queryMap.put("cityId",queryReq.getCityId());
        queryMap.put("areaId",queryReq.getAreaId());
        if(StringUtils.isNotNull(dataScope)) {
            if("precinct_id".equals(dataScope.getScopeName())) {
                queryMap.put("ids", dataScope.getDataIds());
            }else{
                queryMap.put("unitId",dataScope.getDataIds().get(0));
            }
        }
        List<Precinct> precinctList = systemManualMapper.selectPrecinctsByMap(queryMap);
        List<PrecinctResp> precinctRespList = new ArrayList<>();

        List<Integer> areaIds = new ArrayList<>();
        PrecinctResp precinctResp;
        for (Precinct precinct : precinctList) {
            precinctResp = new PrecinctResp();
            BeanUtils.copyProperties(precinct,precinctResp);
            precinctRespList.add(precinctResp);

            if(StringUtils.isNotEmpty(precinct.getProvinceId())){
                areaIds.add(precinct.getProvinceId());
            }
            if(StringUtils.isNotEmpty(precinct.getCityId())){
                areaIds.add(precinct.getCityId());
            }
            if(StringUtils.isNotEmpty(precinct.getAreaId())){
                areaIds.add(precinct.getAreaId());
            }
        }

        if(StringUtils.isNotEmpty(areaIds)) {
            List<Area> areaList = areaService.selectBatchIds(areaIds);
            Map<Integer,String> areaNameMap = new HashMap<>();
            for (Area area : areaList) {
                areaNameMap.put(area.getId(),area.getAreaName());
            }

            for (PrecinctResp resp : precinctRespList) {
                if(StringUtils.isNotEmpty(resp.getProvinceId())){
                    resp.setProvinceName(areaNameMap.get(resp.getProvinceId()));
                }
                if(StringUtils.isNotEmpty(resp.getCityId())){
                    resp.setCityName(areaNameMap.get(resp.getCityId()));
                }
                if(StringUtils.isNotEmpty(resp.getAreaId())){
                    resp.setAreaName(areaNameMap.get(resp.getAreaId()));
                }
            }
        }
        return new PageBean<>(currentPage,pageSize,(int)page.getTotal(),precinctRespList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertOrUpdate(PrecinctReq precinctReq) {
        if(StringUtils.isEmpty(precinctReq.getId())){
            return insertPrecinct(precinctReq);
        }else{
            return udpatePrecinct(precinctReq);
        }
    }

    private int udpatePrecinct(PrecinctReq precinctReq) {
        Precinct precinctDb = selectByPrimaryKey(precinctReq.getId());
        if(StringUtils.isNull(precinctDb)){
            throw new RequestException(ResponseCode.FAIL.code,"管辖区不存在");
        }

        String precinctName = precinctReq.getPrecinctName();
        Integer areaId = precinctReq.getAreaId();
        if(!precinctDb.getPrecinctName().equals(precinctName)){
            //校验管辖区名称是否重复
            int count = systemManualMapper.countPrecinctByNameAndAreaId(precinctName,areaId);
            if(count > 0){
                throw new RequestException(ResponseCode.FAIL.code,"管辖区名称重复");
            }
        }

        Precinct precinct = new Precinct();
        BeanUtils.copyProperties(precinctReq,precinct);
        return updateByPrimaryKeySelective(precinct);
    }

    /**
     * 新增管辖区
     * @param precinctReq
     * @return
     */
    private int insertPrecinct(PrecinctReq precinctReq) {
        String precinctName = precinctReq.getPrecinctName();
        Integer areaId = precinctReq.getAreaId();
        //校验管辖区名称是否重复
        int count = systemManualMapper.countPrecinctByNameAndAreaId(precinctName,areaId);
        if(count > 0){
            throw new RequestException(ResponseCode.FAIL.code,"管辖区名称重复");
        }

        Precinct precinct = new Precinct();
        BeanUtils.copyProperties(precinctReq,precinct);
        return insert(precinct);
    }
}
