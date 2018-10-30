package com.mailian.firecontrol.dto.push;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mailian.core.base.model.BaseDomain;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceItemHistoryData extends BaseDomain {

	private static final long serialVersionUID = 1L;

	private String id;
	private Date tm;              //时间
	private Integer ctg;            //该条数据的类别，1-总、2-尖、3-峰、4-平、5-谷
	private Float val;              //数据值

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTm() {
		return tm;
	}
	public void setTm(Date tm) {
		this.tm = tm;
	}
	public Integer getCtg() {
		return ctg;
	}
	public void setCtg(Integer ctg) {
		this.ctg = ctg;
	}
	public Float getVal() {
		return val;
	}
	public void setVal(Float val) {
		this.val = val;
	}

	
	
}
