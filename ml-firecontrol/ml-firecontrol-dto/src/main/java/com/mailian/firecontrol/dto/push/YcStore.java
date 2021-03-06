package com.mailian.firecontrol.dto.push;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YcStore {

	private String id;              //数据项id
	private Integer store;          //存储周期
	private Integer calcrule;       //存储规则，0实时，1加，2减
	
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
