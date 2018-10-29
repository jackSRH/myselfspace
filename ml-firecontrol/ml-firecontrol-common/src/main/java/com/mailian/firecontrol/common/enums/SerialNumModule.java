package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/29
 * @Description:
 */
public enum SerialNumModule {
    SYS_DIAGRAM_STRUCT("sys_diagram_struct","系统图模块");

    public String code;
    public String name;

    SerialNumModule(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
