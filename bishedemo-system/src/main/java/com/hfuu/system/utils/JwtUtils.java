package com.hfuu.system.utils;

import com.hfuu.common.constant.Constants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 
 * @author hfuu
 */
@Component
@Slf4j
public class JwtUtils
{
    /**
     * 令牌密钥
     */
    @Value("${jwt.secret:hfuu-thesis-management-system-secret-key}")
    private String secret;

    /**
     * 访问令牌有效期（毫秒）
     */
    @Value("${jwt.accessExpireTime:3600000}")
    private long accessExpireTime;

    /**
     * 刷新令牌有效期（毫秒）
     */
    @Value("${jwt.refreshExpireTime:604800000}")
    private long refreshExpireTime;

    /**
     * 生成访问令牌
     * 
     * @param claims JWT声明
     * @return 访问令牌
     */
    public String generateAccessToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpireTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 生成刷新令牌
     * 
     * @param claims JWT声明
     * @return 刷新令牌
     */
    public String generateRefreshToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpireTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 生成JWT令牌
     * 
     * @param userId   用户ID
     * @param username 用户名
     * @param userType 用户类型
     * @return 包含访问令牌和刷新令牌的Map
     */
    public Map<String, String> generateToken(Long userId, String username, Integer userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.USER_ID, userId);
        claims.put(Constants.USERNAME, username);
        claims.put(Constants.USER_TYPE, userType);
        
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", generateAccessToken(claims));
        tokens.put("refreshToken", generateRefreshToken(claims));
        return tokens;
    }

    /**
     * 从JWT令牌中获取声明
     * 
     * @param token JWT令牌
     * @return JWT声明
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.error("JWT解析失败: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 验证JWT令牌
     * 
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            log.error("JWT验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 验证JWT令牌是否即将过期
     * 
     * @param token JWT令牌
     * @return 是否即将过期
     */
    public boolean isTokenExpiringSoon(String token) {
        try {
            Claims claims = parseToken(token);
            Date expiration = claims.getExpiration();
            long timeUntilExpiration = expiration.getTime() - System.currentTimeMillis();
            // 令牌在30分钟内过期
            return timeUntilExpiration < 30 * 60 * 1000;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 刷新访问令牌
     * 
     * @param refreshToken 刷新令牌
     * @return 新的访问令牌
     */
    public String refreshAccessToken(String refreshToken) {
        try {
            // 验证刷新令牌
            Claims claims = parseToken(refreshToken);
            
            // 生成新的访问令牌
            Map<String, Object> newClaims = new HashMap<>();
            newClaims.put(Constants.USER_ID, claims.get(Constants.USER_ID));
            newClaims.put(Constants.USERNAME, claims.get(Constants.USERNAME));
            newClaims.put(Constants.USER_TYPE, claims.get(Constants.USER_TYPE));
            
            return generateAccessToken(newClaims);
        } catch (JwtException e) {
            log.error("刷新令牌验证失败: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 获取签名密钥
     * 
     * @return Key
     */
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}