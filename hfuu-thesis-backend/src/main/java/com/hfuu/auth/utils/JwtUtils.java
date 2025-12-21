package com.hfuu.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT工具类
 * 
 * @author hfuu
 */
public class JwtUtils {
    
    // 密钥，实际项目中应该从配置文件读取
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    // 过期时间：24小时
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;
    
    /**
     * 生成JWT令牌
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @param userType 用户类型
     * @return JWT令牌
     */
    public static String generateToken(Long userId, String username, Integer userType) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + EXPIRE_TIME);
        
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("username", username)
                .claim("userType", userType)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SECRET_KEY)
                .compact();
    }
    
    /**
     * 解析JWT令牌
     * 
     * @param token JWT令牌
     * @return 令牌中的声明
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * 验证JWT令牌是否有效
     * 
     * @param token JWT令牌
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 从JWT令牌中获取用户ID
     * 
     * @param token JWT令牌
     * @return 用户ID
     */
    public static Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }
    
    /**
     * 从JWT令牌中获取用户名
     * 
     * @param token JWT令牌
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }
    
    /**
     * 从JWT令牌中获取用户类型
     * 
     * @param token JWT令牌
     * @return 用户类型
     */
    public static Integer getUserTypeFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userType", Integer.class);
    }
}