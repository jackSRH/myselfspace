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

    }

    @Override
    protected void packageConfig() {
        packageConfig.setModuleName("firecontrol");
    }

    @Override
    protected void templateConfig() {

    }

    @Override
    protected void globalConfig() {
        String parentPath = System.getProperty("user.dir")+File.separator+"ml-generator";
        String targetPath = parentPath+File.separator+"src"+File.separator+"main"+File.separator;
        globalConfig.setJavaTargetPath(targetPath+"java");
        globalConfig.setXmlTargetPath(targetPath+"resources");
    }
}
