package com.mailian.firecontrol.framework.config;

import com.mailian.core.annotation.AppAPI;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.constants.CoreCommonConstant;
import com.mailian.core.enums.ResponseCode;
import com.mailian.firecontrol.dto.ShiroUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/24
 * @Description:swagger2配置
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${mailian.version}")
    private String version;

    @Bean
    public Docket webApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name(CoreCommonConstant.ML_TOKEN).description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());

        List<ResponseMessage> responseMessages = ResponseCode.getResponseMessageList();
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(webApiInfo())
                .groupName("WebAPI")
                .ignoredParameterTypes(ShiroUser.class) //忽略公共参数
                .useDefaultResponseMessages(false) //禁用默认的状态码
                .globalOperationParameters(pars)
                .globalResponseMessage(RequestMethod.GET,responseMessages) //设置公共响应参数
                .globalResponseMessage(RequestMethod.POST,responseMessages)
                .globalResponseMessage(RequestMethod.PUT,responseMessages)
                .globalResponseMessage(RequestMethod.DELETE,responseMessages)
                .select()
                //.apis(RequestHandlerSelectors.basePackage("com.mailian.web.api.controller"))
                .apis(RequestHandlerSelectors.withClassAnnotation(WebAPI.class))
                .paths(PathSelectors.any())
                .build();
    }


    @Bean
    public Docket appApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name(CoreCommonConstant.ML_TOKEN).description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());

        List<ResponseMessage> responseMessages = ResponseCode.getResponseMessageList();
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(appApiInfo())
                .groupName("AppAPI")
                .ignoredParameterTypes(ShiroUser.class) //忽略公共参数
                .useDefaultResponseMessages(false) //禁用默认的状态码
                .globalOperationParameters(pars)
                .globalResponseMessage(RequestMethod.GET,responseMessages) //设置公共响应参数
                .globalResponseMessage(RequestMethod.POST,responseMessages)
                .globalResponseMessage(RequestMethod.PUT,responseMessages)
                .globalResponseMessage(RequestMethod.DELETE,responseMessages)
                .select()
                //.apis(RequestHandlerSelectors.basePackage("com.mailian.web.api.controller"))
                .apis(RequestHandlerSelectors.withClassAnnotation(AppAPI.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("标题：消防云平台后台WEB API文档")
                .description("描述：消防云平台后台WEB API")
                .contact(new Contact("mailian","http://www.szmlink.com/","info@szmlink.com"))
                .version("版本号:" + version)
                .build();
    }

    private ApiInfo appApiInfo() {
        return new ApiInfoBuilder()
                .title("标题：消防云平台后台APP API文档")
                .description("描述：消防云平台后台APP API")
                .contact(new Contact("mailian","http://www.szmlink.com/","info@szmlink.com"))
                .version("版本号:" + version)
                .build();
    }


}
