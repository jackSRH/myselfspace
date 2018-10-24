package com.mailian.core.exception;

import com.mailian.core.enums.ResponseCode;
import java.io.Serializable;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/21
 * @Description: 请求异常类
 */
public class RequestException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -2628776913342931606L;

    private Integer code;
    private String msg;
    private Exception e;

    public RequestException(ResponseCode responseCode, Exception e) {
        this.code = responseCode.code;
        this.msg = responseCode.msg;
        this.e = e;
    }

    public RequestException(ResponseCode responseCode) {
        this.code = responseCode.code;
        this.msg = responseCode.msg;
    }

    public RequestException(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }
}
