package com.mailian.firecontrol.dto.web.response;

import com.mailian.firecontrol.dto.CountDataInfo;
import com.mailian.firecontrol.dto.web.UnitInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/6
 * @Description:
 */
@ApiModel(description = "区域单位地图相关信息")
public class AreaUnitMapResp implements Serializable {
    private static final long serialVersionUID = 6810931384642745787L;
    @ApiModelProperty(value = "数量统计信息")
    private CountDataInfo countDataInfo;
    @ApiModelProperty(value = "单位信息")
    private List<UnitInfo> unitInfos;

    public CountDataInfo getCountDataInfo() {
        return countDataInfo;
    }

    public void setCountDataInfo(CountDataInfo countDataInfo) {
        this.countDataInfo = countDataInfo;
    }

    public List<UnitInfo> getUnitInfos() {
        return unitInfos;
    }

    public void setUnitInfos(List<UnitInfo> unitInfos) {
        this.unitInfos = unitInfos;
    }

}
