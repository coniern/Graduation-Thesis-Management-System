package com.hfuu.auth.config;

import com.hfuu.auth.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT过滤器，用于验证请求中的JWT令牌
 * 
 * @author hfuu
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    public JwtFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求头中的Authorization字段
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // 检查Authorization头是否存在且以Bearer开头
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                // 从JWT令牌中提取用户名
                username = JwtUtils.getUsernameFromToken(token);
            } catch (Exception e) {
                logger.error("Invalid JWT token: " + e.getMessage());
            }
        }

        // 如果令牌有效且用户未认证
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 加载用户详情
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            
            // 验证令牌
            if (JwtUtils.validateToken(token)) {
                // 创建认证令牌
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                
                // 设置认证详情
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 将认证令牌存储到安全上下文中
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // 继续过滤链
        filterChain.doFilter(request, response);
    }
}
