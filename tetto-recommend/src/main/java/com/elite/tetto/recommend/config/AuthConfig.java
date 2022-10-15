package com.elite.tetto.recommend.config;

import com.elite.tetto.recommend.filter.JwtAuthTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import javax.annotation.Resource;

/**
 * 身份验证配置
 *
 * @author Elziy
 */
@Configuration
public class AuthConfig extends WebSecurityConfigurerAdapter {
    
    @Resource
    private JwtAuthTokenFilter jwtAuthTokenFilter;
    
    @Resource
    private AuthenticationEntryPoint authenticationEntryPoint;
    
    @Resource
    private AccessDeniedHandler accessDeniedHandler;
    
    /**
     * 密码加密器
     *
     * @return {@link BCryptPasswordEncoder}
     */
    @Bean(name = "bCryptPasswordEncoder")
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * 解决url不规范问题
     *
     * @return {@link HttpFirewall}
     */
    @Bean
    public HttpFirewall httpFirewall() {
        return new DefaultHttpFirewall();
    }
    
    /**
     * 暴露身份验证管理器
     *
     * @return {@link AuthenticationManager}
     * @throws Exception 异常
     */
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 anonymous只允许匿名访问 permitAll都能访问
                // .antMatchers("/auth/user/login").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
        
        // 添加JWT filter
        http.addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加自定义未授权和未登录结果返回
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        //允许跨域
        http.cors();
    }
}
