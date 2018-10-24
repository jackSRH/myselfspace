package com.mailian.generator.config;

import com.mailian.core.util.StringUtils;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/23
 * @Description:
 */
public class PackageConfig {
    private String parent = "com.mailian";
    private String moduleName = null;
    private String model = "model";
    private String service = "service";
    private String serviceImpl = "service.impl";
    private String mapper = "mapper";
    private String xml = "mapper";
    private String controller = "controller";

    public PackageConfig() {
    }

    public String getParent() {
        return StringUtils.isNotEmpty(this.moduleName) ? this.parent + "." + this.moduleName : this.parent;
    }

    public PackageConfig setParent(String parent) {
        this.parent = parent;
        return this;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public PackageConfig setModuleName(String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    public String getModel() {
        return model;
    }

    public PackageConfig setModel(String model) {
        this.model = model;
        return this;
    }

    public String getService() {
        return this.service;
    }

    public PackageConfig setService(String service) {
        this.service = service;
        return this;
    }

    public String getServiceImpl() {
        return this.serviceImpl;
    }

    public PackageConfig setServiceImpl(String serviceImpl) {
        this.serviceImpl = serviceImpl;
        return this;
    }

    public String getMapper() {
        return this.mapper;
    }

    public PackageConfig setMapper(String mapper) {
        this.mapper = mapper;
        return this;
    }

    public String getXml() {
        return this.xml;
    }

    public PackageConfig setXml(String xml) {
        this.xml = xml;
        return this;
    }

    public String getController() {
        return StringUtils.isEmpty(this.controller) ? "controller" : this.controller;
    }

    public PackageConfig setController(String controller) {
        this.controller = controller;
        return this;
    }

}
