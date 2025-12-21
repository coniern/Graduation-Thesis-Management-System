package com.hfuu.common.constant;

/**
 * 返回状态码
 * 
 * @author hfuu
 */
public enum HttpStatus
{
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 对象创建成功
     */
    CREATED(201, "对象创建成功"),

    /**
     * 请求已经被接受
     */
    ACCEPTED(202, "请求已经被接受"),

    /**
     * 操作已经执行成功，但是没有返回数据
     */
    NO_CONTENT(204, "操作已经执行成功，但是没有返回数据"),

    /**
     * 请求参数有误，无法完成请求
     */
    BAD_REQUEST(400, "请求参数有误，无法完成请求"),

    /**
     * 权限不足，拒绝访问
     */
    UNAUTHORIZED(401, "权限不足，拒绝访问"),

    /**
     * 访问被禁止
     */
    FORBIDDEN(403, "访问被禁止"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 请求方法不支持
     */
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    /**
     * 请求的资源冲突
     */
    CONFLICT(409, "请求的资源冲突"),

    /**
     * 请求的资源已被删除
     */
    GONE(410, "请求的资源已被删除"),

    /**
     * 参数格式错误
     */
    UNPROCESSABLE_ENTITY(422, "参数格式错误"),

    /**
     * 请求过于频繁
     */
    TOO_MANY_REQUESTS(429, "请求过于频繁"),

    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    /**
     * 网关错误
     */
    GATEWAY_TIMEOUT(504, "网关错误");

    private final int code;
    private final String message;

    HttpStatus(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public int getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }
}