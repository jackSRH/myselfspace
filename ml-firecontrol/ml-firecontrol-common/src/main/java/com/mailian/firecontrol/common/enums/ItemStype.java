package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/27
 * @Description:数据项类型
 */
public enum ItemStype {
    TRANSPORTYC(1,"传输遥测"),TRANSPORTYX(2,"传输遥信"),TRANSPORTYT(3,"传输遥调"),TRANSPORTYK(4,"传输遥控"),
    OPERATIONYC(5,"计算遥测"),OPERATIONYX(6,"计算遥信");

    public Integer id;
    public String value;

    ItemStype(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    /**
     * 获取对应属性值
     * @param id
     * @return
     */
    public static String getValue(Integer id){
        ItemStype[] itemStypes = ItemStype.values();
        for (ItemStype itemStype : itemStypes) {
            if(id.intValue() == itemStype.id.intValue()){
                return itemStype.value;
            }
        }
        return "";
    }
}
