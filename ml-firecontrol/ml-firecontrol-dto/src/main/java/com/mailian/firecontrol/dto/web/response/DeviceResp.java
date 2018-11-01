package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/1
 * @Description:
 */
@ApiModel(description = "网关信息")
public class DeviceResp implements Serializable {
    private static final long serialVersionUID = -4145744935971965628L;
    /*设备编号*/
    @ApiModelProperty(value = "设备编号")
    private String code;
    /*名称*/
    @ApiModelProperty(value = "名称")
    private String name;
    /*网关描述*/
    @ApiModelProperty(value = "网关描述")
    private String desc;
    /*网关所属公司*/
    @ApiModelProperty(value = "网关所属公司")
    private String appid;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
