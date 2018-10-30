package com.mailian.firecontrol.dto.push;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppTotalJFPG {

	private Integer ctg;              
	private Float total;
	public Integer getCtg() {
		return ctg;
	}
	public void setCtg(Integer ctg) {
		this.ctg = ctg;
	}
	public Float getTotal() {
		return total;
	}
	public void setTotal(Float total) {
		this.total = total;
	}
	
}
