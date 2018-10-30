package com.mailian.firecontrol.dto.push;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Device {
	
	private String code;					//设备编号
	private String name;					//名称
	private String desc;					//网关描述
	private String appid;					//网关所属公司

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


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
