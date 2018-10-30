package com.mailian.firecontrol.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mailian.core.util.JsonUtils;
import com.mailian.core.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/29
 * @Description: 调用 接口响应处理工具类
 */
public class PushResponseUtil {

    /**
     * 处理响应结果 返回list
     * @param respContent
     * @param <T>
     * @return
     */
    public static <T> List<T> processResponseListData(String respContent)  {
        if(StringUtils.isEmpty(respContent)){
            return null;
        }

        JSONObject jsonObject = JSONObject.parseObject(respContent);
        if(jsonObject.containsKey("data")){
            return JsonUtils.getObjectFromJsonString(jsonObject.getString("data"),new TypeReference<List<T>>(){});
        }
        return null;
    }

    /**
     * 处理响应结果 返回map
     * @param respContent
     * @param <T>
     * @return
     */
    public static Map processResponseMapData(String respContent)  {
        if(StringUtils.isEmpty(respContent)){
            return null;
        }

        JSONObject jsonObject = JSONObject.parseObject(respContent);
        if(jsonObject.containsKey("data")){
            return JsonUtils.getMapFromJsonString(jsonObject.getString("data"));
        }
        return null;
    }

    /**
     * 判断响应结果是否
     * @param respContent
     * @return
     */
    public static boolean processSuccess(String respContent){
        JSONObject jsonObject = JSONObject.parseObject(respContent);
        if(jsonObject.containsKey("code")){
            String code = jsonObject.getString("code");
            if("0".equals(code)) {
                return true;
            }
        }
        return false;
    }

}
