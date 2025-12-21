package com.hfuu.system.service;

import com.hfuu.system.entity.SysUser;

/**
 * 认证服务接口
 * 
 * @author hfuu
 */
public interface AuthService {
    
    /**
     * 用户登录
     * 
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户信息
     */
    SysUser login(String username, String password);
    
    /**
     * 验证用户密码
     * 
     * @param user 用户信息
     * @param password 密码
     * @return 密码是否正确
     */
    boolean validatePassword(SysUser user, String password);
    
    /**
     * 生成加密密码
     * 
     * @param password 原始密码
     * @return 加密后的密码
     */
    String generatePassword(String password);
}