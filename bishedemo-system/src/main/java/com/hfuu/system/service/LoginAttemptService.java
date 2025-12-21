package com.hfuu.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 登录尝试服务类，用于防止暴力破解登录
 * 
 * @author hfuu
 */
@Service
public class LoginAttemptService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 登录尝试前缀
     */
    private static final String LOGIN_ATTEMPT_PREFIX = "login_attempt:";
    
    /**
     * 登录锁定前缀
     */
    private static final String LOGIN_LOCK_PREFIX = "login_lock:";
    
    /**
     * 最大失败次数
     */
    private static final int MAX_FAILED_ATTEMPTS = 5;
    
    /**
     * 锁定时间（秒）
     */
    private static final long LOCK_TIME = 300; // 5分钟
    
    /**
     * 记录登录失败次数
     * 
     * @param username 用户名
     */
    public void recordFailedAttempt(String username) {
        String key = LOGIN_ATTEMPT_PREFIX + username;
        Integer attempts = (Integer) redisTemplate.opsForValue().get(key);
        
        if (attempts == null) {
            // 第一次失败，初始化失败次数
            redisTemplate.opsForValue().set(key, 1, LOCK_TIME, TimeUnit.SECONDS);
        } else {
            // 增加失败次数
            attempts++;
            redisTemplate.opsForValue().set(key, attempts, LOCK_TIME, TimeUnit.SECONDS);
            
            // 如果失败次数达到阈值，锁定用户
            if (attempts >= MAX_FAILED_ATTEMPTS) {
                lockUser(username);
            }
        }
    }
    
    /**
     * 清除登录失败次数
     * 
     * @param username 用户名
     */
    public void clearFailedAttempts(String username) {
        String key = LOGIN_ATTEMPT_PREFIX + username;
        redisTemplate.delete(key);
    }
    
    /**
     * 锁定用户
     * 
     * @param username 用户名
     */
    private void lockUser(String username) {
        String key = LOGIN_LOCK_PREFIX + username;
        redisTemplate.opsForValue().set(key, "locked", LOCK_TIME, TimeUnit.SECONDS);
    }
    
    /**
     * 检查用户是否被锁定
     * 
     * @param username 用户名
     * @return 是否被锁定
     */
    public boolean isUserLocked(String username) {
        String key = LOGIN_LOCK_PREFIX + username;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    
    /**
     * 获取登录失败次数
     * 
     * @param username 用户名
     * @return 失败次数
     */
    public int getFailedAttempts(String username) {
        String key = LOGIN_ATTEMPT_PREFIX + username;
        Integer attempts = (Integer) redisTemplate.opsForValue().get(key);
        return attempts == null ? 0 : attempts;
    }
    
    /**
     * 解锁用户
     * 
     * @param username 用户名
     */
    public void unlockUser(String username) {
        String lockKey = LOGIN_LOCK_PREFIX + username;
        String attemptKey = LOGIN_ATTEMPT_PREFIX + username;
        
        redisTemplate.delete(lockKey);
        redisTemplate.delete(attemptKey);
    }
}