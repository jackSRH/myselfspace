package com.mailian.core.util;

import com.mailian.core.enums.ResponseCode;
import com.mailian.core.exception.RequestException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/9/3
 * @Description: 校验工具
 */
public class ValidatorUtils {
    private static Validator validator;
    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    /**
     * 校验对象
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws  RequestException 校验不通过，则报BusinessException异常
     */
    public static void validateEntity(Object object, Class<?>... groups){
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            ConstraintViolation<Object> constraint = (ConstraintViolation<Object>)constraintViolations.iterator().next();
            throw new RequestException(ResponseCode.FAIL.code,constraint.getMessage());
        }
    }
}
