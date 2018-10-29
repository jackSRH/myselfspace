/*
*
* SerialNumber.java
* Copyright(C) 2018-2099 深圳市脉联电子有限公司
* @date 2018-10-29
*/
package com.mailian.firecontrol.dao.auto.model;

import com.mailian.core.base.model.BaseDomain;
import java.io.Serializable;

public class SerialNumber extends BaseDomain implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 模块编码
     */
    private String moduleCode;

    /**
     * 使用的序列号模板
     */
    private String configTemplet;

    /**
     * 当前序列号
     */
    private Integer curSerial;

    /**
     * 预生成序列号存放在缓存中的个数
     */
    private Integer preMaxNum;

    /**
     * 是否自增长 0否 1是
     */
    private Integer isAutoIncrement;

    /**
     * t_sys_serial_number
     */
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName == null ? null : moduleName.trim();
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode == null ? null : moduleCode.trim();
    }

    public String getConfigTemplet() {
        return configTemplet;
    }

    public void setConfigTemplet(String configTemplet) {
        this.configTemplet = configTemplet == null ? null : configTemplet.trim();
    }

    public Integer getCurSerial() {
        return curSerial;
    }

    public void setCurSerial(Integer curSerial) {
        this.curSerial = curSerial;
    }

    public Integer getPreMaxNum() {
        return preMaxNum;
    }

    public void setPreMaxNum(Integer preMaxNum) {
        this.preMaxNum = preMaxNum;
    }

    public Integer getIsAutoIncrement() {
        return isAutoIncrement;
    }

    public void setIsAutoIncrement(Integer isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }
}