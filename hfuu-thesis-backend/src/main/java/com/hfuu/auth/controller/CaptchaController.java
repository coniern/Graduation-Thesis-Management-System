package com.hfuu.auth.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证码控制器
 * 
 * @author hfuu
 */
@RestController
@RequestMapping("/api/auth")
public class CaptchaController {

    @Autowired(required = false)
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 生成验证码
     * 
     * @param response 响应
     * @return 验证码信息
     * @throws IOException IO异常
     */
    @GetMapping("/captcha")
    public Map<String, Object> generateCaptcha(HttpServletResponse response) throws IOException {
        // 生成验证码图片
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(150, 50, 4, 5);
        
        // 生成唯一标识
        String captchaKey = UUID.randomUUID().toString().replace("-", "");
        
        // 尝试将验证码存入Redis，有效期5分钟
        // 如果Redis不可用，忽略错误，继续执行
        try {
            if (redisTemplate != null) {
                redisTemplate.opsForValue().set("captcha:" + captchaKey, captcha.getCode(), 5, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            // Redis不可用，忽略错误
            System.out.println("Redis不可用，验证码将不进行持久化存储");
        }
        
        // 返回验证码图片和标识
        Map<String, Object> result = new HashMap<>();
        result.put("captchaKey", captchaKey);
        result.put("captchaImage", captcha.getImageBase64Data());
        
        return result;
    }

    /**
     * 验证验证码
     * 
     * @param captchaKey 验证码标识
     * @param captcha 验证码
     * @return 是否验证成功
     */
    public boolean validateCaptcha(String captchaKey, String captcha) {
        if (StrUtil.isBlank(captchaKey) || StrUtil.isBlank(captcha)) {
            return false;
        }
        
        // 如果Redis不可用，直接返回true，跳过验证码验证
        if (redisTemplate == null) {
            return true;
        }
        
        try {
            String redisCaptcha = redisTemplate.opsForValue().get("captcha:" + captchaKey);
            if (StrUtil.isBlank(redisCaptcha)) {
                return false;
            }
            
            // 删除验证码，防止重复使用
            redisTemplate.delete("captcha:" + captchaKey);
            
            return redisCaptcha.equalsIgnoreCase(captcha);
        } catch (Exception e) {
            // Redis不可用，返回true，跳过验证码验证
            System.out.println("Redis不可用，跳过验证码验证");
            return true;
        }
    }
}
