package com.mailian.core.bean;

import com.fasterxml.jackson.annotation.JsonView;
import com.mailian.core.enums.ResponseCode;
import com.mailian.core.manager.ViewManager;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/21
 * @Description: 公共返回对象
 */
@ApiModel(description = "返回响应数据")
public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = -6077038240985006773L;

    /*响应状态编码*/
    @ApiModelProperty(value = "状态编码")
    private Integer code;
    /*响应消息*/
    @ApiModelProperty(value = "错误信息")
    private String msg;
    /*响应数据*/
    @ApiModelProperty(value = "响应数据")
    private T data;

    public ResponseResult() {
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    /**
     * 根据状态码构建响应结果
     * @param responseCode
     * @return
     */
    public static ResponseResult buildResult(ResponseCode responseCode){
        return new ResponseResult(responseCode.code,responseCode.msg);
    }

    /**
     * 根据状态码、返回结果 构建响应结果
     * @param responseCode
     * @return
     */
    public static <T> ResponseResult buildResult(ResponseCode responseCode,T data){
        return new ResponseResult(responseCode.code,responseCode.msg,data);
    }

    /**
     * 构建成功响应结果
     * @return
     */
    public static ResponseResult buildOkResult(){
        return buildResult(ResponseCode.OK);
    }

    /**
     * 构建成功响应结果
     * @return
     */
    public static <T> ResponseResult buildOkResult(T data){
        return buildResult(ResponseCode.OK,data);
    }

    /**
     * 构建失败响应结果
     * @return
     */
    public static ResponseResult buildFailResult(){
        return buildResult(ResponseCode.FAIL);
    }

    /**
     * 构建失败响应结果
     * @return
     */
    public static <T> ResponseResult buildFailResult(T data){
        return buildResult(ResponseCode.FAIL,data);
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
