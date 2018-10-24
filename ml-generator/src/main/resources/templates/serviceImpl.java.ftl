package ${serviceImplPackage};

import ${modelPackage};
import ${mapperPackage};
import ${servicePackage}.${serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.Resource;

/**
 * <p>
    * ${tableComment} 服务实现类
    * </p>
 *
 * @Auther ${author}
 * @Date ${date}
 * @Description
 */
@Service
public class ${serviceImplName} extends ${superServiceImplClass}<${entity},${mapperName}> implements ${serviceName} {

}
