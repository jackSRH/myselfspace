/*
*
* App.java
* Copyright(C) 2018-2099 深圳市脉联电子有限公司
* @date 2018-11-09
*/
package com.mailian.firecontrol.dao.auto.model;

import com.mailian.core.base.model.BaseDomain;
import java.io.Serializable;

public class App extends BaseDomain implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 安装包版本
     */
    private String version;

    /**
     * 名称
     */
    private String name;

    /**
     * 安装包路径
     */
    private String path;

    /**
     * 版本更新内容
     */
    private String log;

    /**
     * MD5编码
     */
    private String md5;

    /**
     * 是否强制升级 0：是，1否
     */
    private Integer forceup;

    /**
     * 0：管理端，1：用户端
     */
    private Integer type;

    /**
     * t_app
     */
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log == null ? null : log.trim();
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5 == null ? null : md5.trim();
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