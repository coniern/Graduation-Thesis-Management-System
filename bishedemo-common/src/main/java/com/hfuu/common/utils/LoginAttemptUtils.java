package com.hfuu.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 登录尝试工具类，用于防止暴力破解登录
 * 
 * @author hfuu
 */
@Component
public class LoginAttemptUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 登录失败次数阈值
     */
    @Value("${security.login.failure.max-attempts:5}")
    private int maxAttempts;

    /**
     * 锁定时间（分钟）
     */
    @Value("${security.login.lock-time:15}")
    private int lockTime;

    /**
     * 记录登录失败次数
     * 
     * @param username 用户名
     * @return 失败次数
     */
    public int incrementLoginAttempts(String username) {
        String key = "login:attempts:" + username;
        Object value = redisTemplate.opsForValue().get(key);
        int attempts = value == null ? 0 : Integer.parseInt(value.toString());
        attempts++;
        redisTemplate.opsForValue().set(key, attempts, lockTime, TimeUnit.MINUTES);
        return attempts;
    }

    /**
     * 重置登录失败次数
     * 
     * @param username 用户名
     */
    public void resetLoginAttempts(String username) {
        String key = "login:attempts:" + username;
        redisTemplate.delete(key);
    }

    /**
     * 检查用户是否被锁定
     * 
     * @param username 用户名
     * @return 是否被锁定
     */
    public boolean isLocked(String username) {
        String key = "login:attempts:" + username;
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return false;
        }
        int attempts = Integer.parseInt(value.toString());
        return attempts >= maxAttempts;
    }

    /**
     * 获取剩余锁定时间（秒）
     * 
     * @param username 用户名
     * @return 剩余锁定时间（秒），如果未锁定则返回0
     */
    public long getRemainingLockTime(String username) {
        String key = "login:attempts:" + username;
        Long expireTime = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expireTime == null ? 0 : expireTime;
    }

    /**
     * 锁定用户
     * 
     * @param username 用户名
     */
    public void lockUser(String username) {
        String key = "login:attempts:" + username;
        redisTemplate.opsForValue().set(key, maxAttempts, lockTime, TimeUnit.MINUTES);
    }

    /**
     * 解锁用户
     * 
     * @param username 用户名
     */
    public void unlockUser(String username) {
        resetLoginAttempts(username);
    }
}