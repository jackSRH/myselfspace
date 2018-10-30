package com.mailian.firecontrol.dto.push;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mailian.core.base.model.BaseDomain;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceSub extends BaseDomain {

	private static final long serialVersionUID = 1L;

	private String id;
	private String gwid;           //网关编号，即gwid,全局唯一标识一个网关
	private String gwname;         //网关名称，"机房网关"
	private Integer comid;         //通讯端口：例如com1,com2
	private String rtuid;          //RTU设备ID，非全局唯一
	private String rtuname;        //RTU设备名称
	private Integer upload;        //上传间隔,单位为秒
	private String appid;         //网关所属公司  
	private String desc;		   //RTU设备描述信息
	private String mid;       	   //模型id
	private String mname;     	   //模型名称
	private String rtuidcb;        //RTU组合id

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
	public String getGwname() {
		return gwname;
	}
	public void setGwname(String gwname) {
		this.gwname = gwname;
	}
	public Integer getComid() {
		return comid;
	}
	public void setComid(Integer comid) {
		this.comid = comid;
	}
	public String getRtuid() {
		return rtuid;
	}
	public void setRtuid(String rtuid) {
		this.rtuid = rtuid;
	}
	public String getRtuname() {
		return rtuname;
	}
	public void setRtuname(String rtuname) {
		this.rtuname = rtuname;
	}
	public Integer getUpload() {
		return upload;
	}
	public void setUpload(Integer upload) {
		this.upload = upload;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getRtuidcb() {
		return rtuidcb;
	}
	public void setRtuidcb(String rtuidcb) {
		this.rtuidcb = rtuidcb;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	
}
