package com.hfuu.system.service.impl;

import com.hfuu.system.entity.SysUser;
import com.hfuu.system.service.TokenService;
import com.hfuu.system.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 令牌服务实现类
 * 
 * @author hfuu
 */
@Service
public class TokenServiceImpl implements TokenService {
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 令牌前缀
     */
    private static final String TOKEN_PREFIX = "token:";
    
    /**
     * 黑名单前缀
     */
    private static final String BLACKLIST_PREFIX = "blacklist:";
    
    /**
     * 生成访问令牌
     */
    @Override
    public String generateToken(SysUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("userType", user.getUserType());
        
        String token = jwtUtils.generateAccessToken(claims);
        
        // 将令牌存入Redis，设置相同的过期时间
        redisTemplate.opsForValue().set(
            TOKEN_PREFIX + user.getId(), 
            token, 
            3600, 
            TimeUnit.SECONDS
        );
        
        return token;
    }
    
    /**
     * 生成JWT令牌对（访问令牌和刷新令牌）
     */
    @Override
    public TokenResponse generateTokenPair(SysUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("userType", user.getUserType());
        
        String accessToken = jwtUtils.generateAccessToken(claims);
        String refreshToken = jwtUtils.generateRefreshToken(claims);
        
        // 将令牌存入Redis，设置相应的过期时间
        redisTemplate.opsForValue().set(
            TOKEN_PREFIX + user.getId(), 
            accessToken, 
            3600, 
            TimeUnit.SECONDS
        );
        
        redisTemplate.opsForValue().set(
            TOKEN_PREFIX + "refresh:" + user.getId(), 
            refreshToken, 
            7, 
            TimeUnit.DAYS
        );
        
        return new TokenResponse(accessToken, refreshToken);
    }
    
    /**
     * 解析访问令牌
     */
    @Override
    public SysUser parseToken(String token) {
        // 检查令牌是否在黑名单中
        if (isTokenBlacklisted(token)) {
            throw new IllegalArgumentException("令牌已失效");
        }
        
        Claims claims = jwtUtils.parseToken(token);
        
        SysUser user = new SysUser();
        user.setId((Long) claims.get("userId"));
        user.setUsername((String) claims.get("username"));
        user.setUserType((Integer) claims.get("userType"));
        
        return user;
    }
    
    /**
     * 刷新访问令牌
     */
    @Override
    public String refreshAccessToken(String refreshToken) {
        // 检查刷新令牌是否有效
        if (!jwtUtils.validateToken(refreshToken)) {
            throw new IllegalArgumentException("刷新令牌无效");
        }
        
        // 检查刷新令牌是否在黑名单中
        if (isTokenBlacklisted(refreshToken)) {
            throw new IllegalArgumentException("刷新令牌已失效");
        }
        
        // 生成新的访问令牌
        String newAccessToken = jwtUtils.refreshAccessToken(refreshToken);
        
        // 解析刷新令牌获取用户信息
        Claims claims = jwtUtils.parseToken(refreshToken);
        Long userId = (Long) claims.get("userId");
        
        // 更新Redis中的访问令牌
        redisTemplate.opsForValue().set(
            TOKEN_PREFIX + userId, 
            newAccessToken, 
            3600, 
            TimeUnit.SECONDS
        );
        
        return newAccessToken;
    }
    
    /**
     * 使令牌失效
     */
    @Override
    public void invalidateToken(String token) {
        // 解析令牌获取用户信息
        Claims claims = jwtUtils.parseToken(token);
        Long userId = (Long) claims.get("userId");
        
        // 将令牌加入黑名单
        redisTemplate.opsForValue().set(
            BLACKLIST_PREFIX + token, 
            "true", 
            jwtUtils.parseToken(token).getExpiration().getTime() - System.currentTimeMillis(), 
            TimeUnit.MILLISECONDS
        );
        
        // 从Redis中移除令牌
        redisTemplate.delete(TOKEN_PREFIX + userId);
        redisTemplate.delete(TOKEN_PREFIX + "refresh:" + userId);
    }
    
    /**
     * 验证令牌有效性
     */
    @Override
    public boolean validateToken(String token) {
        // 检查令牌是否在黑名单中
        if (isTokenBlacklisted(token)) {
            return false;
        }
        
        // 验证令牌
        return jwtUtils.validateToken(token);
    }
    
    /**
     * 检查令牌是否即将过期
     */
    @Override
    public boolean isTokenExpiringSoon(String token) {
        return jwtUtils.isTokenExpiringSoon(token);
    }
    
    /**
     * 检查令牌是否在黑名单中
     */
    private boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }
    
    /**
     * 刷新访问令牌（重命名方法以匹配接口）
     */
    @Override
    public String refreshToken(String refreshToken) {
        return refreshAccessToken(refreshToken);
    }
}