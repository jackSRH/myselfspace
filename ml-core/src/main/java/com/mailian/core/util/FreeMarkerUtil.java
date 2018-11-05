package com.mailian.core.util;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/29
 * @Description:
 */
public class FreeMarkerUtil {
    private static final Logger log = LoggerFactory.getLogger(FreeMarkerUtil.class);

    /**
     * 根据模板文件生成字符串
     * @param fltFile
     * @param templatePath
     * @param datas
     * @return
     */
    public static String genStrByFileTemplate(String fltFile,String templatePath,Map<String, Object> datas){
        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        try {
            cfg.setDirectoryForTemplateLoading(new File(templatePath));
            Template template = cfg.getTemplate(fltFile,"utf-8");

            StringWriter out = new StringWriter();
            template.process(datas,out);

            out.flush();
            out.close();

            return out.getBuffer().toString();
        } catch (IOException e) {
            log.error("模板读取异常",e);
        } catch (TemplateException e) {
            log.error("模板处理异常",e);
        }
        return null;
    }


    /**
     * 根据字符串模板生成字符串
     * @param templateName
     * @param templateContent
     * @param datas
     * @return
     */
    public static String genStrByStrTemplate(String templateName,String templateContent,Map<String, Object> datas){
        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        try {
            StringTemplateLoader strTemplateLoader = new StringTemplateLoader();
            strTemplateLoader.putTemplate(templateName,templateContent);
            cfg.setTemplateLoader(strTemplateLoader);
            Template template = cfg.getTemplate(templateName,"utf-8");

            StringWriter out = new StringWriter();
            template.process(datas,out);

            out.flush();
            out.close();
            return out.getBuffer().toString();
        } catch (IOException e) {
            log.error("模板读取异常",e);
        } catch (TemplateException e) {
            log.error("模板处理异常",e);
        }
        return null;
    }

    public static String genStrBySerialNumberStrTemplate(String templateName,String templateContent,Integer seed){
        Map<String,Object> datas = new HashMap<>();
        datas.put("date",DateUtil.format(new Date(),"yyyyMMdd"));
        datas.put("number",seed);
        return genStrByStrTemplate(templateName,templateContent,datas);
    }


    /**
     * 获取模板
     * @param templateName
     * @param templateContent
     * @return
     */
    public static Template getStrTemplate(String templateName,String templateContent){
        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        try {
            StringTemplateLoader strTemplateLoader = new StringTemplateLoader();
            strTemplateLoader.putTemplate(templateName, templateContent);
            cfg.setTemplateLoader(strTemplateLoader);
            Template template = cfg.getTemplate(templateName, "utf-8");
            return template;
        }catch (IOException e) {
            log.error("模板读取异常",e);
        }
        return null;
    }

    /**
     * 根据字符串模板生成字符串
     * @param template
     * @param datas
     * @return
     */
    public static String genStrByTemplate(Template template,Map<String, Object> datas){
        if(StringUtils.isNull(template)){
            return "";
        }
        try {
            StringWriter out = new StringWriter();
            template.process(datas,out);

            out.flush();
            out.close();
            return out.getBuffer().toString();
        } catch (IOException e) {
            log.error("模板读取异常",e);
        } catch (TemplateException e) {
            log.error("模板处理异常",e);
        }
        return "";
    }
}
