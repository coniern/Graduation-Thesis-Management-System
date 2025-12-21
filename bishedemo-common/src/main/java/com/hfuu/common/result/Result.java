package com.hfuu.common.result;

import com.hfuu.common.constant.HttpStatus;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果
 * 
 * @author hfuu
 */
@Data
public class Result<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * 构造方法
     */
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 构造方法
     * 
     * @param code    响应码
     * @param message 响应消息
     */
    public Result(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 构造方法
     * 
     * @param code    响应码
     * @param message 响应消息
     * @param data    响应数据
     */
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应
     * 
     * @return Result
     */
    public static <T> Result<T> success() {
        return new Result<>(HttpStatus.SUCCESS.getCode(), HttpStatus.SUCCESS.getMessage());
    }

    /**
     * 成功响应
     * 
     * @param message 响应消息
     * @return Result
     */
    public static <T> Result<T> success(String message) {
        return new Result<>(HttpStatus.SUCCESS.getCode(), message);
    }

    /**
     * 成功响应
     * 
     * @param data 响应数据
     * @return Result
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(HttpStatus.SUCCESS.getCode(), HttpStatus.SUCCESS.getMessage(), data);
    }

    /**
     * 成功响应
     * 
     * @param message 响应消息
     * @param data    响应数据
     * @return Result
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(HttpStatus.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败响应
     * 
     * @return Result
     */
    public static <T> Result<T> error() {
        return new Result<>(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), HttpStatus.INTERNAL_SERVER_ERROR.getMessage());
    }

    /**
     * 失败响应
     * 
     * @param message 响应消息
     * @return Result
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), message);
    }

    /**
     * 失败响应
     * 
     * @param code    响应码
     * @param message 响应消息
     * @return Result
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message);
    }

    /**
     * 失败响应
     * 
     * @param status HttpStatus
     * @return Result
     */
    public static <T> Result<T> error(HttpStatus status) {
        return new Result<>(status.getCode(), status.getMessage());
    }

    /**
     * 失败响应
     * 
     * @param status  HttpStatus
     * @param message 响应消息
     * @return Result
     */
    public static <T> Result<T> error(HttpStatus status, String message) {
        return new Result<>(status.getCode(), message);
    }
}