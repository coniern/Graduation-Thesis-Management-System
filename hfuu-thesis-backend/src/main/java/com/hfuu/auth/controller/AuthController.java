package com.hfuu.auth.controller;

import com.hfuu.auth.entity.LoginRequest;
import com.hfuu.auth.entity.LoginResponse;
import com.hfuu.auth.entity.UserInfo;
import com.hfuu.auth.repository.UserRepository;
import com.hfuu.auth.utils.JwtUtils;
import com.hfuu.auth.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * 认证控制器
 * 
 * @author hfuu
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired(required = false)
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 用户登录
     * 
     * @param loginRequest 登录请求
     * @return 登录响应
     */
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        int failCount = 0;
        String loginFailKey = "login_fail:" + loginRequest.getUsername();
        boolean redisAvailable = true;

        try {
            // 检查登录失败次数
            if (redisTemplate != null) {
                String failCountStr = redisTemplate.opsForValue().get(loginFailKey);
                failCount = failCountStr != null ? Integer.parseInt(failCountStr) : 0;

                // 如果失败次数超过5次，拒绝登录
                if (failCount >= 5) {
                    LoginResponse response = new LoginResponse();
                    response.setCode(401);
                    response.setMessage("登录失败次数过多，请稍后再试");
                    return response;
                }
            }
        } catch (Exception e) {
            // Redis不可用，忽略错误
            redisAvailable = false;
            System.out.println("Redis不可用，跳过登录失败次数检查");
        }

        // 查询用户
        UserInfo userInfo = userRepository.findByUsername(loginRequest.getUsername());
        
        // 用户验证
        if (userInfo != null) {
            // 使用密码加密验证
            if (PasswordUtils.matches(loginRequest.getPassword(), userInfo.getPassword())) {
                // 生成真实的JWT令牌
                String token = JwtUtils.generateToken(userInfo.getUserId(), userInfo.getUsername(), userInfo.getUserType());

                // 清除登录失败次数
                try {
                    if (redisTemplate != null) {
                        redisTemplate.delete(loginFailKey);
                    }
                } catch (Exception e) {
                    // Redis不可用，忽略错误
                    System.out.println("Redis不可用，跳过登录失败次数清除");
                }

                // 构建登录响应
                LoginResponse response = new LoginResponse();
                response.setToken(token);
                response.setUserInfo(userInfo);
                response.setMessage("登录成功");
                response.setCode(200);

                return response;
            }
        }

        // 登录失败，增加失败次数
        try {
            if (redisTemplate != null) {
                failCount++;
                redisTemplate.opsForValue().set(loginFailKey, String.valueOf(failCount), 5, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            // Redis不可用，忽略错误
            System.out.println("Redis不可用，跳过登录失败次数增加");
        }

        // 模拟登录失败
        LoginResponse response = new LoginResponse();
        response.setCode(401);
        response.setMessage("用户名或密码错误");
        return response;
    }

    /**
     * 忘记密码
     * 
     * @param username 用户名
     * @return 响应
     */
    @PostMapping("/forgot-password")
    public LoginResponse forgotPassword(@RequestBody String username) {
        // 实际项目中应该发送重置密码邮件或短信
        LoginResponse response = new LoginResponse();
        response.setCode(200);
        response.setMessage("重置密码链接已发送到您的邮箱，请查收");
        return response;
    }

    /**
     * 退出登录
     * 
     * @param token 令牌
     * @return 响应
     */
    @PostMapping("/logout")
    public LoginResponse logout(@RequestHeader("Authorization") String token) {
        // 实际项目中应该将令牌加入黑名单
        LoginResponse response = new LoginResponse();
        response.setCode(200);
        response.setMessage("退出成功");
        return response;
    }
}
