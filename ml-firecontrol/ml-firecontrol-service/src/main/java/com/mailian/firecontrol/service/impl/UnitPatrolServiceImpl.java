package com.mailian.firecontrol.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.bean.PageBean;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.PatrolResultStatus;
import com.mailian.firecontrol.dao.auto.mapper.UnitPatrolMapper;
import com.mailian.firecontrol.dao.auto.model.Precinct;
import com.mailian.firecontrol.dao.auto.model.UnitCamera;
import com.mailian.firecontrol.dao.auto.model.UnitPatrol;
import com.mailian.firecontrol.dao.auto.model.User;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.request.UnitPatrolReq;
import com.mailian.firecontrol.dto.web.response.UnitPatrolListResp;
import com.mailian.firecontrol.service.PrecinctService;
import com.mailian.firecontrol.service.UnitCameraService;
import com.mailian.firecontrol.service.UnitPatrolService;
import com.mailian.firecontrol.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UnitPatrolServiceImpl extends BaseServiceImpl<UnitPatrol, UnitPatrolMapper> implements UnitPatrolService {
    @Resource
    private UnitCameraService unitCameraService;
    @Resource
    private PrecinctService precinctService;
    @Resource
    private UserService userService;

    @Override
    public Boolean insert(Integer uid, UnitPatrolReq unitPatrolReq){
        UnitPatrol unitPatrol = new UnitPatrol();
        BeanUtils.copyProperties(unitPatrolReq,unitPatrol);
        unitPatrol.setUid(uid);
        Integer cameraId = unitPatrolReq.getCameraId();
        UnitCamera unitCamera = unitCameraService.selectByPrimaryKey(cameraId);
        if(StringUtils.isNotNull(unitCamera)){
            Integer precinctId = unitCamera.getPrecinctId();
            unitPatrol.setPrecinctId(precinctId);
            unitPatrol.setUnitId(unitCamera.getUnitId());
            Precinct precinct = precinctService.selectByPrimaryKey(precinctId);
            if(StringUtils.isNotNull(precinct)){
                unitPatrol.setPrecinctName(precinct.getPrecinctName());
            }
        }
        return super.insert(unitPatrol) > 0;
    }

    @Override
    public PageBean<UnitPatrolListResp> getUnitPatrolList(DataScope dataScope, SearchReq searchReq){
        Integer currentPage = searchReq.getCurrentPage();
        Integer pageSize = searchReq.getPageSize();
        Integer patrolStatus = searchReq.getPatrolStatus();
        Page page = PageHelper.startPage(currentPage,pageSize);
        page.setOrderBy("update_time desc");
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("precinctScope", dataScope);
        if(StringUtils.isNotEmpty(patrolStatus)){
            queryMap.put("status",patrolStatus);
        }
        List<UnitPatrol> unitPatrols = super.selectByMap(queryMap);
        if(StringUtils.isEmpty(unitPatrols)){
            return new PageBean<>();
        }

        Set<Integer> uid = new HashSet<>();
        for(UnitPatrol unitPatrol : unitPatrols){
            uid.add(unitPatrol.getUid());
        }
        List<User> users = userService.selectBatchIds(uid);
        Map<Integer,String> userId2Name = new HashMap<>();
        for(User user : users){
            userId2Name.put(user.getId(),user.getFullName());
        }

        List<UnitPatrolListResp> unitPatrolListResps = new ArrayList<>();
        UnitPatrolListResp unitPatrolListResp;
        for(UnitPatrol unitPatrol : unitPatrols){
            unitPatrolListResp = new UnitPatrolListResp();
            BeanUtils.copyProperties(unitPatrol,unitPatrolListResp);
            unitPatrolListResp.setUserName(userId2Name.get(unitPatrol.getUid()));
            unitPatrolListResp.setResult(PatrolResultStatus.getValue(unitPatrol.getPatrolResult()));
            unitPatrolListResps.add(unitPatrolListResp);
        }
        PageBean<UnitPatrolListResp> pageBean = new PageBean<>(currentPage,pageSize,(int)page.getTotal(),unitPatrolListResps);
        return pageBean;
    }


}
