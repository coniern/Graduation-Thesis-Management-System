package com.hfuu.system.controller;

import com.hfuu.system.entity.SysUser;

/**
 * 登录响应类
 * 
 * @author hfuu
 */
public class LoginResponse {
    
    private SysUser user;
    private String accessToken;
    private String refreshToken;
    
    /**
     * 构造函数（兼容旧版，只返回访问令牌）
     */
    public LoginResponse(SysUser user, String accessToken) {
        this.user = user;
        this.accessToken = accessToken;
    }
    
    /**
     * 构造函数（返回访问令牌和刷新令牌）
     */
    public LoginResponse(SysUser user, String accessToken, String refreshToken) {
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
    
    public SysUser getUser() {
        return user;
    }
    
    public void setUser(SysUser user) {
        this.user = user;
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}