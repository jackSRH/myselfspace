package com.mailian.firecontrol.common.enums;

import java.util.Arrays;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/27
 * @Description: 菜单类型
 */
public enum MenuType {
    CATALOG(0,"目录"),MENU(1,"菜单"),BUTTON(2,"按钮"),LINK(3,"其它");

    public Integer id;
    public String value;

    MenuType(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public static List<Byte> getLoginMenuTypeIds(){
        return Arrays.asList(MenuType.CATALOG.id.byteValue(),MenuType.MENU.id.byteValue(),MenuType.LINK.id.byteValue());
    }
}
