package com.mailian.firecontrol.dto.push;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceItemRealTimeData implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String time;              //
	private String gwid;          //设备外键，t_device表fcode
	private Float val;               //当前值

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Float getVal() {
		return val;
	}
	public void setVal(Float val) {
		this.val = val;
	}
	public String getGwid() {
		return gwid;
	}
	public void setGwid(String gwid) {
		this.gwid = gwid;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}


}
