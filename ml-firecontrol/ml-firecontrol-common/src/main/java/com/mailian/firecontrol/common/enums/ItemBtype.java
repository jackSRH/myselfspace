package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/27
 * @Description:数据项业务类型
 */
public enum ItemBtype {
    NOATTRIBUTE(0,"无"),SMOKE(8,"烟感"),LEAKAGE(31,"漏电");


    public Integer id;
    public String value;

    ItemBtype(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    /**
     * 获取对应属性值
     * @param id
     * @return
     */
    public static String getValue(Integer id){
        ItemBtype[] itemBtypes = ItemBtype.values();
        for (ItemBtype itemBtype : itemBtypes) {
            if(id.intValue() == itemBtype.id.intValue()){
                return itemBtype.value;
            }
        }
        return "";
    }

    /**
     * 获取对应属性值
     * @param id
     * @return
     */
    public static ItemBtype getItemBtype(Integer id){
        ItemBtype[] itemBtypes = ItemBtype.values();
        for (ItemBtype itemBtype : itemBtypes) {
            if(id.intValue() == itemBtype.id.intValue()){
                return itemBtype;
            }
        }
        return null;
    }
}
