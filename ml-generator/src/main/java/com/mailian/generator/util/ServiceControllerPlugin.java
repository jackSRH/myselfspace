package com.mailian.generator.util;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.mailian.core.util.DateUtil;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.*;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/23
 * @Description:
 */
public class ServiceControllerPlugin extends PluginAdapter {
    private static final String DEFAULT_DAO_SUPER_CLASS = "com.mailian.core.base.mapper.BaseMapper";
    private static final String DEFAULT_SERVICE_SUPER_CLASS = "com.mailian.core.base.service.BaseService";
    private static final String DEFAULT_SERVICE_IMPL_SUPER_CLASS = "com.mailian.core.base.service.impl.BaseServiceImpl";
    private static final String DEFAULT_CONTROLLER_SUPER_CLASS = "com.mailian.core.base.controller.BaseController";
    private static final String DEFAULT_MODEL_SUPER_CLASS = "com.mailian.core.base.model.BaseDomain";

    private String targetDir;
    private String controllerPackage;
    private String servicePackage;
    private String serviceImplPackage;
    private String moduleName;


    @Override
    public boolean validate(List<String> warnings) {
        targetDir = properties.getProperty("targetDir");
        controllerPackage = properties.getProperty("controllerPackage");
        servicePackage = properties.getProperty("servicePackage");
        serviceImplPackage = properties.getProperty("serviceImplPackage");
        moduleName = properties.getProperty("moduleName");
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        cfg.setDefaultEncoding(ConstVal.UTF8);
        try {
            cfg.setTemplateLoader(new FileTemplateLoader(ResourceUtils.getFile("classpath:templates")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String javaClassName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.clear();
        objectMap.put("controllerPackage",controllerPackage);
        objectMap.put("superControllerClassPackage",DEFAULT_CONTROLLER_SUPER_CLASS);
        objectMap.put("tableComment",introspectedTable.getRemarks());
        objectMap.put("author",System.getProperty("user.name"));
        objectMap.put("date",DateUtil.format(new Date(),DateUtil.DATE_FORMAT_FOR_YMD));
        objectMap.put("moduleName",moduleName);
        objectMap.put("entity",javaClassName);
        objectMap.put("superControllerClass","BaseController");
        objectMap.put("controllerName",javaClassName+"Controller");


        objectMap.put("servicePackage",servicePackage);
        objectMap.put("modelPackage",DEFAULT_MODEL_SUPER_CLASS);
        objectMap.put("superServiceClassPackage",DEFAULT_SERVICE_SUPER_CLASS);
        objectMap.put("serviceName",javaClassName+"Service");
        objectMap.put("superServiceClass","BaseService");

        objectMap.put("serviceImplPackage",serviceImplPackage);
        objectMap.put("mapperPackage",introspectedTable.getMyBatis3SqlMapNamespace());

        /*genService(cfg,javaClassName);
        genServiceImpl(cfg,javaClassName);
        genController(cfg,javaClassName);*/
        return null;
    }


}
