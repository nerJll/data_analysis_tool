package com.ner.common.exception;

import com.ner.common.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.net.SocketTimeoutException;

/**
 * 功能描述：全局异常拦截
 *
 * @author jll
 * @date 2019/4/8 11:37
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * @description 处理所有不可知的异常
     */
    @ExceptionHandler({Exception.class})    //申明捕获那个异常类
    public ApiResult globalExceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ApiResult.errMessage(ex.getMessage());
    }

    @ExceptionHandler({NullPointerException.class})
    public ApiResult nullPointerException(NullPointerException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResult.errMessage(ex.getMessage());
    }


    @ExceptionHandler({BizException.class})
    public ApiResult bizException(BizException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResult.errMessage(ex.getMessage());
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult constraintViolationException(ConstraintViolationException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResult.errMessage(ex.getMessage()).setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult IllegalArgumentException(IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResult.errMessage(ex.getMessage()).setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(value = {NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResult noHandlerFoundException(NoHandlerFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResult.errMessage(ex.getMessage()).setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(value = {SocketTimeoutException.class})
    public ApiResult socketTimeoutException(SocketTimeoutException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResult.errMessage("服务连接超时");
    }

    @ExceptionHandler(value = {RedisConnectionFailureException.class})
    public ApiResult redisConnectionFailureException(RedisConnectionFailureException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResult.errMessage("服务连接超时");
    }

    @ExceptionHandler(value = {MyBatisSystemException.class})
    public ApiResult myBatisSystemException(MyBatisSystemException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResult.errMessage("服务连接超时");
    }
}
