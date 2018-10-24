package com.mailian.core.handler;

import com.mailian.core.bean.ResponseResult;
import com.mailian.core.enums.ResponseCode;
import com.mailian.core.exception.RequestException;
import com.mailian.core.util.FileSizeUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/21
 * @Description:异常处理器
 */
@RestControllerAdvice
public class GlobalDefaultExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = RequestException.class)
    public ResponseResult requestExceptionHandler(RequestException e){
        if(e.getE()!=null) e.printStackTrace();
        return new ResponseResult(e.getCode(),e.getMsg());
    }


    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseResult requestExceptionHandler(DataIntegrityViolationException e){
        logger.error(e.getMessage(), e);
        return new ResponseResult(ResponseCode.FAIL.code,"数据操作格式异常");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        logger.error(e.getMessage(), e);
        BindingResult result = e.getBindingResult();
        String s = "参数验证失败";
        if(result.hasErrors()){
            List<ObjectError> errors = result.getAllErrors();
            s = errors.get(0).getDefaultMessage();
        }
        return new ResponseResult(ResponseCode.FAIL.code,s);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseResult handleAuthorizationException(AuthorizationException e){
        logger.error(e.getMessage(), e);
        return ResponseResult.buildResult(ResponseCode.NO_AUTH);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        logger.error("请求方法不正确 "+e.getMethod(), e);
        return new ResponseResult(ResponseCode.FAIL.code,"不支持' " + e.getMethod() + "'请求");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseResult handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e){
        logger.error(e.getMessage(), e);
        return new ResponseResult(ResponseCode.FAIL.code,"文件大小不能超过" + FileSizeUtil.formatFileSize(e.getMaxUploadSize()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseResult handleAuthenticationException(AuthenticationException e){
        logger.error(e.getMessage(), e);
        return new ResponseResult(ResponseCode.ERR_PASSWORD.code,e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseResult requestExceptionHandler(Exception e){
        logger.error(e.getMessage(), e);
        return new ResponseResult(ResponseCode.FAIL.code,"服务器飘了，管理员去拿刀修理了~");
    }


}
