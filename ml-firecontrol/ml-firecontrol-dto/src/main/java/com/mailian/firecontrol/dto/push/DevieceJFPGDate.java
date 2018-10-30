package com.mailian.firecontrol.dto.push;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DevieceJFPGDate implements Serializable {
	private static final long serialVersionUID = -6924027240286246343L;
	private String gwid;             //网关编号
	private String top;              //尖
	private String peak;             //峰
	private String flat;             //平
	private String valley;           //谷
	
	public String getGwid() {
		return gwid;
	}
	public void setGwid(String gwid) {
		this.gwid = gwid;
	}
	public String getTop() {
		return top;
	}
	public void setTop(String top) {
		this.top = top;
	}
	public String getPeak() {
		return peak;
	}
	public void setPeak(String peak) {
		this.peak = peak;
	}
	public String getFlat() {
		return flat;
	}
	public void setFlat(String flat) {
		this.flat = flat;
	}
	public String getValley() {
		return valley;
	}
	public void setValley(String valley) {
		this.valley = valley;
	}
	
}
