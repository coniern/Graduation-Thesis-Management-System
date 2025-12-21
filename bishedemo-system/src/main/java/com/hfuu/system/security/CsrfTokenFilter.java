package com.hfuu.system.security;

import com.hfuu.common.utils.CsrfTokenUtils;
import com.hfuu.system.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * CSRF令牌过滤器，用于验证CSRF令牌
 * 
 * @author hfuu
 */
@Component
public class CsrfTokenFilter extends OncePerRequestFilter {

    @Autowired
    private CsrfTokenUtils csrfTokenUtils;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 需要CSRF验证的HTTP方法
     */
    private static final List<String> CSRF_PROTECTED_METHODS = List.of("POST", "PUT", "DELETE");

    /**
     * 不需要CSRF验证的路径
     */
    private static final List<String> CSRF_EXCLUDED_PATHS = List.of("/api/auth/login", "/api/auth/refresh", "/api/auth/logout");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求方法和路径
        String method = request.getMethod();
        String path = request.getRequestURI();

        // 检查是否需要CSRF验证
        boolean needCsrfProtection = CSRF_PROTECTED_METHODS.contains(method) && !CSRF_EXCLUDED_PATHS.contains(path);

        if (needCsrfProtection) {
            // 从请求头获取CSRF令牌
            String csrfToken = request.getHeader("X-CSRF-Token");

            // 从JWT令牌中获取用户ID
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwtToken = authorizationHeader.substring(7);
                Claims claims = jwtUtils.getClaimsFromToken(jwtToken);
                Long userId = claims.get("userId", Long.class);

                // 验证CSRF令牌
                if (!csrfTokenUtils.validateCsrfToken(userId, csrfToken)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Invalid CSRF token");
                    return;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Missing JWT token");
                return;
            }
        }

        // 继续执行过滤器链
        filterChain.doFilter(request, response);
    }
}