package com.mailian.firecontrol.service.util;

import com.mailian.core.bean.SpringContext;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.AreaRank;
import com.mailian.firecontrol.common.enums.UnitType;
import com.mailian.firecontrol.dao.auto.model.Area;
import com.mailian.firecontrol.dto.web.response.PieData;
import com.mailian.firecontrol.dto.web.response.PieResp;
import com.mailian.firecontrol.service.AreaService;
import com.mailian.firecontrol.service.impl.AreaServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/6
 * @Description:
 */
public class BuildDefaultResultUtil {
    /**
     * 构建场所数量
     * @param pieDataMap
     * @return
     */
    public static PieResp buildDefaultPieResp(Map<Integer,PieData> pieDataMap){
        List<PieData> pieDataList = new ArrayList<>();
        for (UnitType unitType : UnitType.values()) {
            PieData pieData = new PieData();
            pieData.setName(unitType.value);
            pieData.setValue(0);
            pieDataMap.put(unitType.id, pieData);
            pieDataList.add(pieData);
        }

        PieResp pieResp = new PieResp();
        pieResp.setPieDataList(pieDataList);
        pieResp.setTotal(0);
        return pieResp;
    }

    /**
     * 添加区域id
     * @param areaId
     * @param queryMap
     */
    public static void putAreaSearchMap(Integer areaId, Map<String, Object> queryMap) {
        if(StringUtils.isNotEmpty(areaId)){
            AreaService areaService = SpringContext.getBean(AreaServiceImpl.class);
            Area area = areaService.getAreaById(areaId);
            Integer areaRank = area.getAreaRank();
            if(AreaRank.PROVINCE.id.equals(areaRank)){
                queryMap.put("provinceId",areaId);
            }else if(AreaRank.CITY.id.equals(areaRank)){
                queryMap.put("cityId",areaId);
            }else{
                queryMap.put("areaId",areaId);
            }
        }
    }
}
