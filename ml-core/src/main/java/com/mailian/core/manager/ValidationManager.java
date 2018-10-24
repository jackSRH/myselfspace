package com.mailian.core.manager;

import javax.validation.GroupSequence;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/28
 * @Description:校验管理器
 */
public class ValidationManager {

    public interface CommonValidation{}

    public interface AddValidation extends CommonValidation{}

    public interface UpdateValidation extends CommonValidation{}

    @GroupSequence({AddValidation.class, UpdateValidation.class})
    public interface GroupValidation{}
}
