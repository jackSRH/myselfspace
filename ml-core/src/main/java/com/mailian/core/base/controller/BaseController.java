package com.mailian.core.base.controller;

import com.mailian.core.bean.ResponseResult;
import com.mailian.core.enums.ResponseCode;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/24
 * @Description: 通用数据处理
 */
public class BaseController {
    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
   /* @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }*/

    /**
     * 返回操作失败消息
     * @param msg
     * @return
     */
    public ResponseResult error(String msg){
        return new ResponseResult(ResponseCode.FAIL.code,msg);
    }
}
