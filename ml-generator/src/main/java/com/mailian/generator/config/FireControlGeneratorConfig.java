package com.mailian.generator.config;

import java.io.File;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/20
 * @Description:
 */
public class FireControlGeneratorConfig extends AbstractGeneratorConfig{
    public FireControlGeneratorConfig() {
    }

    @Override
    protected void dataSourceConfig() {
        dataSourceConfig.setUrl("jdbc:mysql://192.168.1.199:3306/ml_firecontrol?useSSL=false&characterEncoding=UTF-8");
        dataSourceConfig.setUserName("mailian");
        dataSourceConfig.setPassword("123456");
    }

    @Override
    protected void packageConfig() {
        packageConfig.setModuleName("firecontrol.dao");
        packageConfig.setModel("auto.model");
        packageConfig.setMapper("auto.mapper");
        packageConfig.setXml("mapper.auto");
    }

    @Override
    protected void templateConfig() {

    }

    @Override
    protected void globalConfig() {
        String parentPath = System.getProperty("user.dir")+File.separator+"ml-firecontrol"+File.separator+"ml-firecontrol-dao";
        String targetPath = parentPath+File.separator+"src"+File.separator+"main"+File.separator;
        globalConfig.setJavaTargetPath(targetPath+"java");
        globalConfig.setXmlTargetPath(targetPath+"resources");
    }
}
