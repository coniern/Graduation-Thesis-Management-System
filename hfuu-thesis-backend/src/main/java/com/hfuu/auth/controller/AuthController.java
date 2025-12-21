package com.hfuu.auth.controller;

import com.hfuu.auth.entity.LoginRequest;
import com.hfuu.auth.entity.LoginResponse;
import com.hfuu.auth.entity.UserInfo;
import com.hfuu.auth.utils.JwtUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 * 
 * @author hfuu
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * 用户登录
     * 
     * @param loginRequest 登录请求
     * @return 登录响应
     */
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        // 模拟根据用户名和密码验证用户
        // 实际项目中应该调用真实的用户服务进行验证
        if (loginRequest.getUsername() != null && loginRequest.getPassword() != null) {
            // 模拟用户验证，实际项目中应该从数据库查询用户
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(1L);
            userInfo.setUsername(loginRequest.getUsername());
            userInfo.setRealName(loginRequest.getUsername().equals("admin") ? "系统管理员" : loginRequest.getUsername().equals("student") ? "张三" : "李四");
            userInfo.setUserType(loginRequest.getUsername().equals("admin") ? 4 : loginRequest.getUsername().equals("student") ? 1 : 2); // 1: 学生, 2: 导师, 3: 院系管理员, 4: 校级管理员

            // 生成真实的JWT令牌
            String token = JwtUtils.generateToken(userInfo.getUserId(), userInfo.getUsername(), userInfo.getUserType());

            // 构建登录响应
            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setUserInfo(userInfo);
            response.setMessage("登录成功");
            response.setCode(200);

            return response;
        } else {
            // 模拟登录失败
            LoginResponse response = new LoginResponse();
            response.setCode(401);
            response.setMessage("用户名或密码错误");
            return response;
        }
    }
}
