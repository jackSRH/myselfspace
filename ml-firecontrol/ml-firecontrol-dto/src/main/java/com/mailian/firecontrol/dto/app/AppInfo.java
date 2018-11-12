package com.mailian.firecontrol.dto.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mailian.core.bean.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "app信息")
public class AppInfo extends BaseInfo {
	@ApiModelProperty(value = "appid")
	private Integer id;
	@ApiModelProperty(value = "版本")
	private String version;
	@ApiModelProperty(value = "名称")
	private String name;
	@ApiModelProperty(value = "路径")
	private String path;
	@ApiModelProperty(value = "更新内容")
	private String log;
	@ApiModelProperty(value = "MD5编码")
	private String md5;
	@ApiModelProperty(value = "是否强制升级 0：是，1否")
	private Integer forceup;
	@ApiModelProperty(value = "类型  0：管理端，1：用户端")
	private Integer type;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getForceup() {
		return forceup;
	}

	public void setForceup(Integer forceup) {
		this.forceup = forceup;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
