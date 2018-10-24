package com.mailian.core.enums;


import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ResponseMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/21
 * @Description:
 */
public enum ResponseCode {
    OK(0,"操作成功"),
    FAIL(1,"操作失败"),
    NO_AUTH(2,"没有权限，请联系管理员授权"),
    INVALID_TOKEN(3,"无效token"),
    REFRESH_TOKEN(4,"刷新token"),
    EXPIRED_TOKEN(5,"登录失效,请重新登录"),
    ERR_PASSWORD(6,"账号密码错误"),
    NO_LOGIN(7,"未登录");

    public Integer code;
    public String msg;

    ResponseCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static List<ResponseMessage> getResponseMessageList(){
        List<ResponseMessage> responseMessageBuilderList = new ArrayList<>();
        ResponseCode [] responseCodes = ResponseCode.values();
        for (ResponseCode responseCode : responseCodes) {
            responseMessageBuilderList.add(new ResponseMessageBuilder()//500
                    .code(responseCode.code)
                    .message(responseCode.msg)
                    .build());
        }
        return responseMessageBuilderList;
    }
}
