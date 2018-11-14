package com.mailian.firecontrol.common.util;

import java.util.List;

public class StringUtil {
    public static String listTurnString(List<String> strs) {
        String res = "";
        if(null == strs || strs.isEmpty()) {
            return res;
        }
        for(String str : strs) {
            res+=str + ",";
        }
        return res.substring(0, res.length() - 1);
    }

}
