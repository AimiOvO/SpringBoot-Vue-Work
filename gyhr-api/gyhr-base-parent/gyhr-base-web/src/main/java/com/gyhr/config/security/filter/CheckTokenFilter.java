package com.gyhr.config.security.filter;

import com.gyhr.config.jwt.JwtUtils;
import com.gyhr.config.security.exception.CustomAuthenionException;
import com.gyhr.config.security.handler.LoginFailureHandler;
import com.gyhr.config.security.service.CustomUserDetailService;
import lombok.Data;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token验证过滤器
 */
@Data
@Component("CheckTokenFilter")
public class CheckTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CustomUserDetailService userDetailService;
    @Autowired
    private LoginFailureHandler loginFailureHandler;

    // 登录请求的地址
    @Value("${gyhr.loginUrl}")
    private String loginUrl;
    @Value("^/images/.*")
    private String imgUrl;
    @Value("/api/alipay/notify")
    private String alipayReturnUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 获取当前请求的地址
            String url = request.getRequestURI();
            // 登录、图片资源和支付宝支付回调不需要token认证
            if (
                !url.equals(loginUrl)   // 登录页面
                && !url.matches(imgUrl)     // 图片访问路径
                && !url.equals(alipayReturnUrl)     // 支付宝支付回调
            ) {
                validateToken(request);
            }
        } catch (AuthenticationException e) {
            loginFailureHandler.commence(request, response, e);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void validateToken(HttpServletRequest request) {
        // 从头部获取token
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
        if (StringUtils.isEmpty(token)) {
            throw new CustomAuthenionException("token获取失败！");
        }
        // 解析token
        String username = jwtUtils.getUsernameFromToken(token);
        if (StringUtils.isEmpty(username)) {
            throw new CustomAuthenionException("token验证失败！");
        }
        // 获取用户类型
        String userType = jwtUtils.getClaimsFromToken(token).get("userType").toString();
        // 查询用户信息
        UserDetails userDetails = userDetailService.loadUserByUsername(username + ":" + userType);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities()
        );
       authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
       // 设置到spring security上下文
       SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
