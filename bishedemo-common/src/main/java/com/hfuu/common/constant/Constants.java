package com.hfuu.common.constant;

/**
 * 通用常量信息
 * 
 * @author hfuu
 */
public class Constants
{
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * 成功标记
     */
    public static final Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    public static final Integer FAIL = 500;

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌后缀
     */
    public static final String TOKEN_SUFFIX = "_hfuu";

    /**
     * 用户ID
     */
    public static final String USER_ID = "userId";

    /**
     * 用户名
     */
    public static final String USERNAME = "username";

    /**
     * 用户类型
     */
    public static final String USER_TYPE = "userType";

    /**
     * 角色权限
     */
    public static final String AUTHORITIES = "authorities";

    /**
     * 学生用户类型
     */
    public static final Integer USER_TYPE_STUDENT = 1;

    /**
     * 导师用户类型
     */
    public static final Integer USER_TYPE_MENTOR = 2;

    /**
     * 院系管理员用户类型
     */
    public static final Integer USER_TYPE_DEPT_ADMIN = 3;

    /**
     * 校级管理员用户类型
     */
    public static final Integer USER_TYPE_SCHOOL_ADMIN = 4;
}