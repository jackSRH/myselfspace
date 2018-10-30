package com.mailian.firecontrol.dto.push;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mailian.core.base.model.BaseDomain;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Alarm extends BaseDomain {

	private static final long serialVersionUID = 1L;
	private String id;
	private String gwid;			//关联通讯机编号
	private Float val;              //数值
	private String stime;             //告警开始时间
	private String etime;             //告警结束时间
	private Integer level;		    //告警等级 1:普通，2:严重
	private Integer alarmid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGwid() {
		return gwid;
	}
	public void setGwid(String gwid) {
		this.gwid = gwid;
	}
	public Float getVal() {
		return val;
	}
	public void setVal(Float val) {
		this.val = val;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getEtime() {
		return etime;
	}
	public void setEtime(String etime) {
		this.etime = etime;
	}

	public Integer getAlarmid() {
		return alarmid;
	}

	public void setAlarmid(Integer alarmid) {
		this.alarmid = alarmid;
	}
}
