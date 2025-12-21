package com.hfuu.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * CSRF令牌工具类，用于生成和验证CSRF令牌
 * 
 * @author hfuu
 */
@Component
public class CsrfTokenUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * CSRF令牌前缀
     */
    private static final String CSRF_TOKEN_PREFIX = "csrf:token:";

    /**
     * CSRF令牌有效期（秒）
     */
    private static final long CSRF_TOKEN_EXPIRATION = 3600; // 1小时

    /**
     * 生成CSRF令牌
     * 
     * @param userId 用户ID
     * @return CSRF令牌
     */
    public String generateCsrfToken(Long userId) {
        String token = UUID.randomUUID().toString();
        String key = CSRF_TOKEN_PREFIX + userId;
        redisTemplate.opsForValue().set(key, token, CSRF_TOKEN_EXPIRATION, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 验证CSRF令牌
     * 
     * @param userId 用户ID
     * @param token CSRF令牌
     * @return 是否有效
     */
    public boolean validateCsrfToken(Long userId, String token) {
        if (userId == null || token == null) {
            return false;
        }
        String key = CSRF_TOKEN_PREFIX + userId;
        Object storedToken = redisTemplate.opsForValue().get(key);
        return token.equals(storedToken);
    }

    /**
     * 刷新CSRF令牌
     * 
     * @param userId 用户ID
     * @return 新的CSRF令牌
     */
    public String refreshCsrfToken(Long userId) {
        // 先删除旧令牌
        String key = CSRF_TOKEN_PREFIX + userId;
        redisTemplate.delete(key);
        // 生成新令牌
        return generateCsrfToken(userId);
    }

    /**
     * 删除CSRF令牌
     * 
     * @param userId 用户ID
     */
    public void deleteCsrfToken(Long userId) {
        String key = CSRF_TOKEN_PREFIX + userId;
        redisTemplate.delete(key);
    }
}