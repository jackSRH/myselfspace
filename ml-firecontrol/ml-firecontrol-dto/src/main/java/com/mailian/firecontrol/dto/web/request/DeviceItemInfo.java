package com.mailian.firecontrol.dto.web.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "数据项信息")
public class DeviceItemInfo implements Serializable {

    private static final long serialVersionUID = -3929919728797332010L;
    /*id*/
    @ApiModelProperty(value = "自增id")
    private String id;
    /*网关ID，即gwid*/
    @ApiModelProperty(value = "网关ID")
    private String gwid;             
    /*通讯端口：例如com1即为1,com2即为2*/
    @ApiModelProperty(value = "通讯端口")
    private Integer comid;
    /*RTU设备ID*/
    @ApiModelProperty(value = "RTU设备ID")
    private String rtuid;
    /*模型文件ID*/
    @ApiModelProperty(value = "模型文件ID")
    private String modelid;
    /*数据项分类 1传输遥测2传输遥信3传输遥调4传输遥控5计算遥测6计算遥信*/
    @ApiModelProperty(value = "数据项分类")
    private Integer stype;
    /* 数据项模型内编号,RTU内唯一*/
    @ApiModelProperty(value = "数据项模型内编号")
    private Integer sid;
    /*名称*/
    @ApiModelProperty(value = "名称")
    private String name;
    /*单位*/
    @ApiModelProperty(value = "单位")
    private String unit;
    /*数据类型，0 有符号整形;1 无符号整形;2 浮点型*/
    @ApiModelProperty(value = "数据类型")
    private Integer type;
    /*比例系数*/
    @ApiModelProperty(value = "比例系数")
    private Float ratio;
    /*上限，用于告警判断*/
    @ApiModelProperty(value = "上限")
    private Float ulimit;
    /*下限，用于告警判断*/
    @ApiModelProperty(value = "下限")
    private Float llimit;
    /*存储，默认-1不存; 0存储，大于1表示存储周期，单位为秒，并且按存储周期存*/
    @ApiModelProperty(value = "存储")
    private Integer store;
    /*遥调最大值*/
    @ApiModelProperty(value = "遥调最大值")
    private Float maxval;
    /*遥调最小值*/
    @ApiModelProperty(value = "遥调最小值")
    private Float minval;
    /*分组值*/
    @ApiModelProperty(value = "分组值")
    private Integer group;
    /*数据异常持续时间，单位秒，超过这个时间触发告警*/
    @ApiModelProperty(value = "数据异常持续时间")
    private Integer duration;
    /*是否计算虚量，最大、最小、平均*/
    @ApiModelProperty(value = "是否计算虚量")
    private Integer virtual;
    /*计算规则，0实时1加2减，尖峰平谷和存储周期，两种时间点的数据相加相减*/
    @ApiModelProperty(value = "计算规则")
    private Integer calcrule;
    /*遥信,desc_0*/
    @ApiModelProperty(value = "遥信 0描述")
    private String desc0;
    /*遥信,desc_1*/
    @ApiModelProperty(value = "遥信 1描述")
    private String desc1;
    /*遥控,val0*/
    @ApiModelProperty(value = "遥控 val0")
    private String val0;
    /* 遥控,val_1*/
    @ApiModelProperty(value = "遥控 val1")
    private String val1;
    /*基值*/
    @ApiModelProperty(value = "基值")
    private Float basic;
    /*计算值规则，即JS脚本*/
    @ApiModelProperty(value = "计算值规则")
    private String getval;
    /*中文简称*/
    @ApiModelProperty(value = "中文简称")
    private String shortname;
    /*上上限*/
    @ApiModelProperty(value = "上上限")
    private Float uulimit;
    /*下下限*/
    @ApiModelProperty(value = "下下限")
    private Float lllimit;
    /*计算规则，主计算元素，单个数据项id*/
    @ApiModelProperty(value = "主计算元素")
    private String relid;
    /* 计算规则，参与计算元素，单个数据项id*/
    @ApiModelProperty(value = "参与计算元素")
    private String relids;
    /* 0,"无",1,"电表",2,"水表",3,"气量",4,"电压",5,"电流",6,"门禁",7,"燃气",8,"烟感",9,"开关",10,"通信",11,"跳闸" 商业类型*/
    @ApiModelProperty(value = "商业类型")
    private Integer btype;
    /* 描述*/
    @ApiModelProperty(value = " 描述")
    private String desc;
    /* 平台是否告警*/
    @ApiModelProperty(value = " 平台是否告警")
    private Integer palarm;
    /* 告警是否发送短信，0不发，1发，默认0*/
    @ApiModelProperty(value = "告警是否发送短信")
    private Integer alarmsms;
    /*告警是否发送邮件，0不发，1发，默认0*/
    @ApiModelProperty(value = "告警是否发送邮件")
    private Integer alarmemail;
    /*告警信息推送地址，默认空*/
    @ApiModelProperty(value = "告警信息推送地址")
    private String alarmurl;
    /*遥信告警值，取0或1，默认-1标识未设置*/
    @ApiModelProperty(value = "遥信告警值")
    private Integer yxalarmval;
    /*遥信告警时，告警等级，0预警，1普通，2严重，同告警信息表告警等级*/
    @ApiModelProperty(value = "遥信告警时")
    private Integer yxalarmlevel;
    /*组合的RTUID*/
    @ApiModelProperty(value = "组合的RTUID")
    private String rtuidcb;
    /*CT变比*/
    @ApiModelProperty(value = "CT变比")
    private Float ctratio;
    /*实时数据*/
    @ApiModelProperty(value = "实时数据")
    private Float val;
    /*创建时间*/
    @ApiModelProperty(value = "创建时间")
    private String createtime;
    /*英文简称*/
    @ApiModelProperty(value = "英文简称")
    private String displayname;
    /*展示的描述*/
    @ApiModelProperty(value = "展示的描述")
    private String displaydesc;

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

    public String getModelid() {
        return modelid;
    }

    public void setModelid(String modelid) {
        this.modelid = modelid;
    }

    public Integer getStype() {
        return stype;
    }

    public void setStype(Integer stype) {
        this.stype = stype;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Float getRatio() {
        return ratio;
    }

    public void setRatio(Float ratio) {
        this.ratio = ratio;
    }

    public Float getUlimit() {
        return ulimit;
    }

    public void setUlimit(Float ulimit) {
        this.ulimit = ulimit;
    }

    public Float getLlimit() {
        return llimit;
    }

    public void setLlimit(Float llimit) {
        this.llimit = llimit;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public Float getMaxval() {
        return maxval;
    }

    public void setMaxval(Float maxval) {
        this.maxval = maxval;
    }

    public Float getMinval() {
        return minval;
    }

    public void setMinval(Float minval) {
        this.minval = minval;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getVirtual() {
        return virtual;
    }

    public void setVirtual(Integer virtual) {
        this.virtual = virtual;
    }

    public Integer getCalcrule() {
        return calcrule;
    }

    public void setCalcrule(Integer calcrule) {
        this.calcrule = calcrule;
    }

    public String getDesc0() {
        return desc0;
    }

    public void setDesc0(String desc0) {
        this.desc0 = desc0;
    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getVal0() {
        return val0;
    }

    public void setVal0(String val0) {
        this.val0 = val0;
    }

    public String getVal1() {
        return val1;
    }

    public void setVal1(String val1) {
        this.val1 = val1;
    }

    public Float getBasic() {
        return basic;
    }

    public void setBasic(Float basic) {
        this.basic = basic;
    }

    public String getGetval() {
        return getval;
    }

    public void setGetval(String getval) {
        this.getval = getval;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public Float getUulimit() {
        return uulimit;
    }

    public void setUulimit(Float uulimit) {
        this.uulimit = uulimit;
    }

    public Float getLllimit() {
        return lllimit;
    }

    public void setLllimit(Float lllimit) {
        this.lllimit = lllimit;
    }

    public String getRelid() {
        return relid;
    }

    public void setRelid(String relid) {
        this.relid = relid;
    }

    public String getRelids() {
        return relids;
    }

    public void setRelids(String relids) {
        this.relids = relids;
    }

    public Integer getBtype() {
        return btype;
    }

    public void setBtype(Integer btype) {
        this.btype = btype;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getPalarm() {
        return palarm;
    }

    public Integer getAlarmsms() {
        return alarmsms;
    }

    public void setAlarmsms(Integer alarmsms) {
        this.alarmsms = alarmsms;
    }

    public Integer getAlarmemail() {
        return alarmemail;
    }

    public void setAlarmemail(Integer alarmemail) {
        this.alarmemail = alarmemail;
    }

    public String getAlarmurl() {
        return alarmurl;
    }

    public void setAlarmurl(String alarmurl) {
        this.alarmurl = alarmurl;
    }

    public Integer getYxalarmval() {
        return yxalarmval;
    }

    public void setYxalarmval(Integer yxalarmval) {
        this.yxalarmval = yxalarmval;
    }

    public Integer getYxalarmlevel() {
        return yxalarmlevel;
    }

    public void setYxalarmlevel(Integer yxalarmlevel) {
        this.yxalarmlevel = yxalarmlevel;
    }

    public String getRtuidcb() {
        return rtuidcb;
    }

    public void setRtuidcb(String rtuidcb) {
        this.rtuidcb = rtuidcb;
    }

    public Float getCtratio() {
        return ctratio;
    }

    public void setCtratio(Float ctratio) {
        this.ctratio = ctratio;
    }

    public Float getVal() {
        return val;
    }

    public void setVal(Float val) {
        this.val = val;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getDisplaydesc() {
        return displaydesc;
    }

    public void setDisplaydesc(String displaydesc) {
        this.displaydesc = displaydesc;
    }
}
