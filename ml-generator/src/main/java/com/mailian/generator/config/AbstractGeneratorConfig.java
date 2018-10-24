package com.mailian.generator.config;

import com.mailian.core.util.StringUtils;

import java.util.Properties;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/20
 * @Description:
 */
public abstract class AbstractGeneratorConfig {
    DataSourceConfig dataSourceConfig = new DataSourceConfig();
    PackageConfig packageConfig = new PackageConfig();
    GlobalConfig globalConfig = new GlobalConfig();
    Properties properties = new Properties();

    /**
     * 数据源配置
     */
    protected abstract void dataSourceConfig();

    /**
     * 包名配置，通过该配置，指定生成代码的包路径
     */
    protected abstract void packageConfig();

    /**
     * 模板配置，可自定义代码生成的模板，实现个性化操作
     */
    protected abstract void templateConfig();

    /**
     * 全局配置
     */
    protected abstract void globalConfig();

    private String joinPackage(String parent, String subPackage) {
        return StringUtils.isEmpty(parent) ? subPackage : parent + "." + subPackage;
    }

    public Properties getProperties() {
        dataSourceConfig();
        packageConfig();
        templateConfig();
        globalConfig();

        properties.setProperty("jdbc.driver",dataSourceConfig.getDriver());
        properties.setProperty("jdbc.url",dataSourceConfig.getUrl());
        properties.setProperty("jdbc.username",dataSourceConfig.getUserName());
        properties.setProperty("jdbc.password",dataSourceConfig.getPassword());


        properties.setProperty("model.package",this.joinPackage(packageConfig.getParent(), packageConfig.getModel()));
        properties.setProperty("mapper.package",this.joinPackage(packageConfig.getParent(), packageConfig.getMapper()));
        properties.setProperty("service.package",this.joinPackage(packageConfig.getParent(), packageConfig.getService()));
        properties.setProperty("service.impl.package",this.joinPackage(packageConfig.getParent(), packageConfig.getServiceImpl()));
        properties.setProperty("controller.package",this.joinPackage(packageConfig.getParent(), packageConfig.getController()));
        properties.setProperty("xml.package",packageConfig.getXml());

        properties.setProperty("target.project",globalConfig.getJavaTargetPath());
        properties.setProperty("xml.target.project",globalConfig.getXmlTargetPath());
        return properties;
    }
}
