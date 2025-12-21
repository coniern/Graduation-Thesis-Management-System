package com.hfuu.common.exception;

import com.hfuu.common.constant.HttpStatus;
import com.hfuu.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 
 * @author hfuu
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler
{
    /**
     * 处理自定义异常
     * 
     * @param e 自定义异常
     * @return Result
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.error("业务异常：{}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理认证异常
     * 
     * @param e 认证异常
     * @return Result
     */
    @ExceptionHandler(AuthenticationException.class)
    public Result<?> handleAuthenticationException(AuthenticationException e) {
        log.error("认证异常：{}", e.getMessage());
        return Result.error(HttpStatus.UNAUTHORIZED.getCode(), "认证失败，请重新登录");
    }

    /**
     * 处理访问拒绝异常
     * 
     * @param e 访问拒绝异常
     * @return Result
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<?> handleAccessDeniedException(AccessDeniedException e) {
        log.error("访问拒绝异常：{}", e.getMessage());
        return Result.error(HttpStatus.FORBIDDEN.getCode(), "权限不足，拒绝访问");
    }

    /**
     * 处理验证异常
     * 
     * @param e 验证异常
     * @return Result
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("验证异常：{}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数验证失败";
        return Result.error(HttpStatus.BAD_REQUEST.getCode(), message);
    }

    /**
     * 处理绑定异常
     * 
     * @param e 绑定异常
     * @return Result
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        log.error("绑定异常：{}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数绑定失败";
        return Result.error(HttpStatus.BAD_REQUEST.getCode(), message);
    }

    /**
     * 处理SQL异常
     * 
     * @param e SQL异常
     * @return Result
     */
    @ExceptionHandler(SQLException.class)
    public Result<?> handleSQLException(SQLException e) {
        log.error("SQL异常：{}", e.getMessage());
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "数据库操作失败");
    }

    /**
     * 处理重复键异常
     * 
     * @param e 重复键异常
     * @return Result
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<?> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("重复键异常：{}", e.getMessage());
        return Result.error(HttpStatus.CONFLICT.getCode(), "数据已存在，请勿重复提交");
    }

    /**
     * 处理空指针异常
     * 
     * @param e 空指针异常
     * @return Result
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<?> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常：{}", e.getMessage());
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "系统内部错误，请联系管理员");
    }

    /**
     * 处理运行时异常
     * 
     * @param e 运行时异常
     * @return Result
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常：{}", e.getMessage());
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "系统运行时错误，请联系管理员");
    }

    /**
     * 处理异常
     * 
     * @param e 异常
     * @return Result
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统异常：{}", e.getMessage());
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "系统内部错误，请联系管理员");
    }

    /**
     * 重写handleExceptionInternal方法
     * 
     * @param ex      异常
     * @param body    响应体
     * @param headers 响应头
     * @param statusCode 响应状态码
     * @param request 请求
     * @return ResponseEntity
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        log.error("系统异常：{}", ex.getMessage());
        Result<?> result = Result.error(statusCode.value(), ex.getMessage());
        return new ResponseEntity<>(result, headers, statusCode);
    }
}