package com.hfuu.auth.entity;

/**
 * 登录响应实体
 * 
 * @author hfuu
 */
public class LoginResponse {
    /**
     * 响应码
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * JWT令牌
     */
    private String token;

    /**
     * 用户信息
     */
    private UserInfo userInfo;

    /**
     * 获取响应码
     * 
     * @return 响应码
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置响应码
     * 
     * @param code 响应码
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取响应消息
     * 
     * @return 响应消息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置响应消息
     * 
     * @param message 响应消息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取JWT令牌
     * 
     * @return JWT令牌
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置JWT令牌
     * 
     * @param token JWT令牌
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    public UserInfo getUserInfo() {
        return userInfo;
    }

    /**
     * 设置用户信息
     * 
     * @param userInfo 用户信息
     */
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
