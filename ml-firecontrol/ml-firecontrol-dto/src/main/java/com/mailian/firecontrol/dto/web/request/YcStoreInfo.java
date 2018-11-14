package com.mailian.firecontrol.dto.web.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "传输遥控数据项信息")
public class YcStoreInfo implements Serializable {

    private static final long serialVersionUID = -5691640767896119168L;
    /*数据项id*/
    @ApiModelProperty(value = "数据项id")
    private String id;
    /*存储周期*/
    @ApiModelProperty(value = "存储周期")
    private Integer store;
    /*存储规则，0实时，1加，2减*/
    @ApiModelProperty(value = "存储规则")
    private Integer calcrule;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public Integer getCalcrule() {
        return calcrule;
    }

    public void setCalcrule(Integer calcrule) {
        this.calcrule = calcrule;
    }
}
