package com.mailian.firecontrol.dto.web.request;

import com.mailian.core.manager.ValidationManager;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/31
 * @Description:
 */
@ApiModel(value = "管辖区信息")
public class PrecinctReq implements Serializable {
    private static final long serialVersionUID = -9168142903260077464L;
    @ApiModelProperty(value = "管辖区id")
    private Integer id;
    @ApiModelProperty(value = "管辖区名称")
    @NotEmpty(message = "{precinct.name.notNull}",groups = ValidationManager.CommonValidation.class)
    private String precinctName;
    @ApiModelProperty(value = "负责人")
    private String dutyName;
    @ApiModelProperty(value = "负责人电话")
    private String dutyPhone;
    @ApiModelProperty(value = "区域id")
    @NotNull(message = "{precinct.areaId.notNull}",groups = ValidationManager.CommonValidation.class)
    private Integer areaId;
    @ApiModelProperty(value = "状态（0正常 1停用）")
    private Integer status;
    @ApiModelProperty(value = "省份id")
    private Integer provinceId;
    @ApiModelProperty(value = "城市id")
    private Integer cityId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrecinctName() {
        return precinctName;
    }

    public void setPrecinctName(String precinctName) {
        this.precinctName = precinctName;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getDutyPhone() {
        return dutyPhone;
    }

    public void setDutyPhone(String dutyPhone) {
        this.dutyPhone = dutyPhone;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
}
