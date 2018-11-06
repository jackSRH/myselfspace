package com.mailian.firecontrol.service.component;

import com.mailian.firecontrol.common.enums.DictType;
import com.mailian.firecontrol.dto.web.response.DictDataResp;
import com.mailian.firecontrol.service.DictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/6
 * @Description:
 */
@Component
public class DictDataComponent {
    @Autowired
    private DictDataService dictDataService;

    /**
     * 根据 字典类型 键值 获取对应字典数据对象
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return
     */
    public DictDataResp getDictDataByTypeAndValue(DictType dictType, String dictValue){
        List<DictDataResp> dictDataRespList =  dictDataService.queryDataTreeByType(dictType.code);

        for (DictDataResp dictDataResp : dictDataRespList) {
            if(dictDataResp.getDictValue().equals(dictValue)){
                return dictDataResp;
            }
        }
        return null;
    }


    /**
     * 根据设备设施分类id 获取对应设备设施类型信息
     * @param dictValue 字典键值
     * @return
     */
    public DictDataResp getEqFaByValue(String dictValue){
        List<DictDataResp> monitorList =  dictDataService.queryDataTreeByType(DictType.POWER_MONITOR.code);
        List<DictDataResp> fireList =  dictDataService.queryDataTreeByType(DictType.FIRE_CONROL.code);

        for (DictDataResp dictDataResp : monitorList) {
            if(dictDataResp.getDictValue().equals(dictValue)){
                return dictDataResp;
            }
        }
        for (DictDataResp dictDataResp : fireList) {
            if(dictDataResp.getDictValue().equals(dictValue)){
                return dictDataResp;
            }
        }
        return null;
    }
}
