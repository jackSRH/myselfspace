package com.mailian.firecontrol.dto.web.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mailian.core.bean.TreeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/31
 * @Description:
 */
@ApiModel(description = "字典数据信息")
public class DictDataResp implements TreeEntity<DictDataResp> {
    @ApiModelProperty(value = "数据id")
    private Integer id;

    @ApiModelProperty(value = "字典排序")
    private Integer dictSort;

    @ApiModelProperty(value = "字典名称")
    private String dictLabel;

    @ApiModelProperty(value = "字典键值")
    private String dictValue;

    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "父字典数据id")
    private Integer parentId;

    @ApiModelProperty(value = "是否系统默认,(0是 1否)",allowableValues = "0,1")
    private Byte isDefault;

    @ApiModelProperty(value = "状态(0正常 1停用)",allowableValues = "0,1")
    private Byte status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "子字典数据")
    private List<DictDataResp> childDictDataList;

    @ApiModelProperty(value = "父字典数据")
    private DictDataResp parentDictData;

    @ApiModelProperty(value = "所属级别")
    private Integer dictRank;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDictSort() {
        return dictSort;
    }

    public void setDictSort(Integer dictSort) {
        this.dictSort = dictSort;
    }

    public String getDictLabel() {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Byte getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Byte isDefault) {
        this.isDefault = isDefault;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<DictDataResp> getChildDictDataList() {
        return childDictDataList;
    }

    public void setChildDictDataList(List<DictDataResp> childDictDataList) {
        this.childDictDataList = childDictDataList;
    }

    public DictDataResp getParentDictData() {
        return parentDictData;
    }

    public void setParentDictData(DictDataResp parentDictData) {
        this.parentDictData = parentDictData;
    }

    public Integer getDictRank() {
        return dictRank;
    }

    public void setDictRank(Integer dictRank) {
        this.dictRank = dictRank;
    }

    @JsonIgnore
    @Override
    public String getIdStr() {
        return this.dictValue;
    }

    @JsonIgnore
    @Override
    public String getParentIdStr() {
        return this.parentId.toString();
    }

    @JsonIgnore
    @Override
    public Integer getSortNo() {
        return this.dictSort;
    }

    @JsonIgnore
    @Override
    public String getNameStr() {
        return this.dictLabel;
    }

    @JsonIgnore
    @Override
    public void setChildList(List<DictDataResp> childList) {
        this.childDictDataList = childList;
    }

    @Override
    public boolean filterByParam(Object... params) {
        return false;
    }

    @JsonIgnore
    @Override
    public void setChildBizCount(Integer bizCount) {

    }

    @JsonIgnore
    @Override
    public Integer getBizCount() {
        return 0;
    }
}
