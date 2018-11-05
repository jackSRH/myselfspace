package com.mailian.firecontrol.dto.web.response;

import com.mailian.core.bean.TreeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/1
 * @Description:
 */
@ApiModel(value = "区域信息")
public class AreaResp implements TreeEntity<AreaResp> {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "区域名")
    private String areaName;
    @ApiModelProperty(value = "区域编码")
    private String areaCode;
    @ApiModelProperty(value = "父级id")
    private Integer parentId;
    @ApiModelProperty(value = "级别 0:省,1:市,2:区域")
    private Integer areaRank;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getAreaRank() {
        return areaRank;
    }

    public void setAreaRank(Integer areaRank) {
        this.areaRank = areaRank;
    }

    @Override
    public String getIdStr() {
        return null;
    }

    @Override
    public String getParentIdStr() {
        return null;
    }

    @Override
    public Integer getSortNo() {
        return null;
    }

    @Override
    public String getNameStr() {
        return null;
    }

    @Override
    public void setChildList(List<AreaResp> childList) {

    }

    @Override
    public boolean filterByParam(Object... params) {
        return false;
    }

    @Override
    public void setChildBizCount(Integer bizCount) {

    }

    @Override
    public Integer getBizCount() {
        return null;
    }
}
