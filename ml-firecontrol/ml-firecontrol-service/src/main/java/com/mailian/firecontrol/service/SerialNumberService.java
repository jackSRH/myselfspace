package com.mailian.firecontrol.service;


import com.mailian.core.base.service.BaseService;
import com.mailian.firecontrol.common.enums.SerialNumModule;
import com.mailian.firecontrol.dao.auto.model.SerialNumber;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/2
 * @Description:
 */
public interface SerialNumberService extends BaseService<SerialNumber> {

    /**
     * 根据模块编码生成序列号
     * @param serialNumModule
     * @return
     */
    String generateSerialNumberByModelCode(SerialNumModule serialNumModule);

}
