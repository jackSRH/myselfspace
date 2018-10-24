package com.mailian.generator;

import com.mailian.core.util.StringUtils;
import com.mailian.generator.config.FireControlGeneratorConfig;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.*;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/20
 * @Description:
 */
public class MlCodeGenerator {

    public static void generatorMyBatis(Properties extraProperties) throws Exception{
        System.out.println("---------开始生成myBatis相关文件");
        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;
        //指定 逆向工程配置文件
        File configFile = ResourceUtils.getFile("classpath:mybatis-generator.xml");
        ConfigurationParser cp = new ConfigurationParser(extraProperties,warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                callback, warnings);
        myBatisGenerator.generate(null);

        if(StringUtils.isNotEmpty(warnings)) {
            for (String warning : warnings) {
                System.err.println(warning);
            }
        }else{
            System.out.println("生成成功!!!");
        }
    }

    public static void main(String[] args) {
        /*FireControlGeneratorConfig fireControlGeneratorConfig = new FireControlGeneratorConfig();
        fireControlGeneratorConfig.execute();*/
        try {
            FireControlGeneratorConfig fireControlGeneratorConfig = new FireControlGeneratorConfig();
            generatorMyBatis(fireControlGeneratorConfig.getProperties());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
