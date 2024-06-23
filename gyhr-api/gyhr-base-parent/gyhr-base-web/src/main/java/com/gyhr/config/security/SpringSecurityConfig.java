package com.gyhr.config.security;

import com.gyhr.config.security.filter.CheckTokenFilter;
import com.gyhr.config.security.handler.CustomAccessDeineHandler;
import com.gyhr.config.security.handler.LoginFailureHandler;
import com.gyhr.config.security.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * spring security配置类
 * Configuration：表明该类是一个配置类
 * EnableWebSecurity: 启用spring security
 * EnableGlobalMethodSecurity: 启用spring security注解
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private CustomAccessDeineHandler customAccessDeineHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private CheckTokenFilter checkTokenFilter;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 解决跨域
        http.cors(withDefaults()).headers(headers -> headers.frameOptions().disable());
        // 在验证用户名之前进行过滤处理
        http.addFilterBefore(checkTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 关闭跨域请求伪
        http.csrf(csrf -> csrf.disable())
            //基于token，所以不需要session
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeRequests(requests -> requests
            //放行指定请求,其他的所有请求都要认证
            .antMatchers(
                "/api/user/login",      // 登录页面
                "/images/**",           // 图片访问路径
                "/api/alipay/notify"    // 支付宝支付回调
            ).permitAll()
            .anyRequest().authenticated())
            .exceptionHandling(handling -> handling
                .authenticationEntryPoint(loginFailureHandler)
                .accessDeniedHandler(customAccessDeineHandler));
    }

    // 注入AuthenticationManager spring security需要
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 配置自定义的UserDetailService
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService);
    }
}
