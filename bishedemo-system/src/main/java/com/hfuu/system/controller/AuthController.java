package com.hfuu.system.controller;

import com.hfuu.common.result.Result;
import com.hfuu.system.entity.LoginRequest;
import com.hfuu.system.entity.SysUser;
import com.hfuu.system.service.AuthService;
import com.hfuu.system.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 认证模块API
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证接口")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    /**
     * 登录接口
     * POST /api/auth/login
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录系统")
    public Result login(@Valid @RequestBody LoginRequest loginRequest) {
        // 调用认证服务进行登录
        SysUser user = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        // 生成JWT令牌对
        TokenService.TokenResponse tokens = tokenService.generateTokenPair(user);
        // 返回用户信息和令牌
        return Result.success("登录成功", new LoginResponse(user, tokens.getAccessToken(), tokens.getRefreshToken()));
    }

    /**
     * 刷新令牌接口
     * POST /api/auth/refresh
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新令牌", description = "使用刷新令牌获取新的访问令牌")
    public Result refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            // 使用刷新令牌获取新的访问令牌
            String newAccessToken = tokenService.refreshToken(request.getRefreshToken());
            return Result.success("令牌刷新成功", new TokenRefreshResponse(newAccessToken));
        } catch (Exception e) {
            return Result.error("令牌刷新失败", e.getMessage());
        }
    }

    /**
     * 登出接口
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户退出登录")
    public Result logout(@RequestHeader("Authorization") String token, @RequestBody(required = false) LogoutRequest request) {
        // 解析访问令牌并移除
        String accessToken = token.replace("Bearer ", "");
        tokenService.invalidateToken(accessToken);
        // 如果提供了刷新令牌，也将其移除
        if (request != null && request.getRefreshToken() != null) {
            tokenService.invalidateToken(request.getRefreshToken());
        }
        return Result.success("登出成功");
    }

    /**
     * 获取当前用户信息
     * GET /api/auth/user-info
     */
    @GetMapping("/user-info")
    @Operation(summary = "获取用户信息", description = "获取当前登录用户信息")
    public Result getUserInfo(@RequestHeader("Authorization") String token) {
        // 从令牌中解析用户信息
        SysUser user = tokenService.parseToken(token.replace("Bearer ", ""));
        return Result.success("获取成功", user);
    }

    /**
     * 刷新令牌请求类
     */
    public static class RefreshTokenRequest {
        private String refreshToken;

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    /**
     * 令牌刷新响应类
     */
    public static class TokenRefreshResponse {
        private String accessToken;

        public TokenRefreshResponse(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    /**
     * 登出请求类
     */
    public static class LogoutRequest {
        private String refreshToken;

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }
}