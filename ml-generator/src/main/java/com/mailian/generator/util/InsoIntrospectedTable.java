package com.mailian.generator.util;

import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/27
 * @Description:
 */
public class InsoIntrospectedTable extends IntrospectedTableMyBatis3Impl {

    @Override
    protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator, List<String> warnings, ProgressCallback progressCallback) {
        this.xmlMapperGenerator = new InsoXMLMapperGenerator();

        this.initializeAbstractGenerator(this.xmlMapperGenerator, warnings, progressCallback);
    }
}
