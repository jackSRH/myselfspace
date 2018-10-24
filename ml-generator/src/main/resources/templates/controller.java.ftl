package ${controllerPackage};


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${tableComment} 前端控制器
 * </p>
 *
 * @Auther ${author}
 * @Date ${date}
 * @Description
 */
@RestController
@RequestMapping("<#if moduleName??>/${moduleName}</#if>/${entity}")
    <#if superControllerClass??>
public class ${controllerName} extends ${superControllerClass} {
    <#else>
public class ${controllerName} {

}
    </#if>