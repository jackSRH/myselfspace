package com.mailian.firecontrol.service;


import com.mailian.core.base.service.BaseService;
import com.mailian.firecontrol.common.enums.SysConfigType;
import com.mailian.firecontrol.dao.auto.model.SysConfig;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/23
 * @Description:
 */
public interface SysConfigService extends BaseService<SysConfig> {
    /**
     * 根据类型更新对应的值
     * @param sysConfigType
     * @param value
     * @return
     */
    int updateBySysConfigType(SysConfigType sysConfigType, String value);

    /**
     * 根据参数键获取对应参数对象
     * @param sysConfigType
     * @return
     */
    SysConfig getConfigByType(SysConfigType sysConfigType);


    int deleteBySysConfig(SysConfig sysConfig);
}
