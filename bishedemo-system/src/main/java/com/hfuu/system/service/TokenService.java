package com.hfuu.system.service;

import com.hfuu.system.entity.SysUser;

/**
 * 令牌服务接口
 * 
 * @author hfuu
 */
public interface TokenService {
    
    /**
     * 生成访问令牌
     * 
     * @param user 用户信息
     * @return 访问令牌
     */
    String generateToken(SysUser user);
    
    /**
     * 生成JWT令牌对（访问令牌和刷新令牌）
     * 
     * @param user 用户信息
     * @return 包含访问令牌和刷新令牌的TokenResponse对象
     */
    TokenResponse generateTokenPair(SysUser user);
    
    /**
     * 解析访问令牌
     * 
     * @param token 访问令牌
     * @return 用户信息
     */
    SysUser parseToken(String token);
    
    /**
     * 刷新访问令牌
     * 
     * @param refreshToken 刷新令牌
     * @return 新的访问令牌
     */
    String refreshToken(String refreshToken);
    
    /**
     * 使令牌失效
     * 
     * @param token 令牌
     */
    void invalidateToken(String token);
    
    /**
     * 验证令牌有效性
     * 
     * @param token 令牌
     * @return 是否有效
     */
    boolean validateToken(String token);
    
    /**
     * 检查令牌是否即将过期
     * 
     * @param token 令牌
     * @return 是否即将过期
     */
    boolean isTokenExpiringSoon(String token);
    
    /**
     * 令牌响应类
     */
    class TokenResponse {
        private String accessToken;
        private String refreshToken;
        
        public TokenResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
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
}