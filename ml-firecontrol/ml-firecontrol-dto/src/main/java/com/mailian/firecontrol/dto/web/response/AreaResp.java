package com.mailian.firecontrol.dto.web.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mailian.core.bean.TreeEntity;
import com.mailian.core.util.FilterUtil;
import com.mailian.core.util.StringUtils;
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
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    @ApiModelProperty(value = "子集区域")
    private List<AreaResp> childAreaList;
    @ApiModelProperty(value = "区分id用")
    private String disStr;

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

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public List<AreaResp> getChildAreaList() {
        return childAreaList;
    }

    public void setChildAreaList(List<AreaResp> childAreaList) {
        this.childAreaList = childAreaList;
    }

    public String getDisStr() {
        return disStr;
    }

    public void setDisStr(String disStr) {
        this.disStr = disStr;
    }

    @JsonIgnore
    @Override
    public String getIdStr() {
        return id.toString()+StringUtils.nvl(disStr,"");
    }

    @JsonIgnore
    @Override
    public String getParentIdStr() {
        return parentId.toString();
    }

    @JsonIgnore
    @Override
    public Integer getSortNo() {
        return orderNum;
    }

    @JsonIgnore
    @Override
    public String getNameStr() {
        return areaName;
    }

    @JsonIgnore
    @Override
    public void setChildList(List<AreaResp> childList) {
        this.childAreaList = childList;
    }

    @JsonIgnore
    @Override
    public boolean filterByParam(Object... params) {
        if(FilterUtil.likeStr(areaName,params[0])){
            return true;
        }

        return false;
    }

    @JsonIgnore
    @Override
    public void setChildBizCount(Integer bizCount) {

    }

    @JsonIgnore
    @Override
    public Integer getBizCount() {
        return null;
    }
}
