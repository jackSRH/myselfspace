package com.mailian.firecontrol.dto.web.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/1
 * @Description:
 */
public class DictTypeReq {
    @ApiModelProperty(value = "字典类型id")
    private Integer id;

    @ApiModelProperty(value = "字典名称")
    @NotEmpty(message = "{dicttype.dictName.notEmpty}")
    private String dictName;

    @ApiModelProperty(value = "字典类型")
    @NotEmpty(message = "{dicttype.dictType.notEmpty}")
    private String dictType;

    @ApiModelProperty(value = "状态 0正常 1停用")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
