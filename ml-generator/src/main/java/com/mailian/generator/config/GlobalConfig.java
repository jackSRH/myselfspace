package com.mailian.generator.config;

import java.io.File;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/23
 * @Description: 全局配置
 */
public class GlobalConfig {
    private String javaTargetPath;
    private String xmlTargetPath;

    public GlobalConfig() {
        String parentPath = System.getProperty("user.dir");
        javaTargetPath = parentPath+File.separator+"src"+File.separator+"main"+File.separator+"java";
        xmlTargetPath = parentPath+File.separator+"src"+File.separator+"main"+File.separator+"resources";
    }

    public String getJavaTargetPath() {
        return javaTargetPath;
    }

    public void setJavaTargetPath(String javaTargetPath) {
        this.javaTargetPath = javaTargetPath;
    }

    public String getXmlTargetPath() {
        return xmlTargetPath;
    }

    public void setXmlTargetPath(String xmlTargetPath) {
        this.xmlTargetPath = xmlTargetPath;
    }
}
