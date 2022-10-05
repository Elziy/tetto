package com.elite.tetto.auth.filter;

import com.alibaba.fastjson.JSON;
import com.elite.tetto.auth.util.JwtUtil;
import com.elite.tetto.common.constant.AuthConstant;
import com.elite.tetto.common.entity.vo.LoginUserRes;
import io.jsonwebtoken.Claims;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * token认证过滤器
 *
 * @author Elziy
 */
@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {
    
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader("token");
        if (token == null) {
            // 没有token直接放行，没有认证后续会被拦截
            filterChain.doFilter(request, response);
            // 过滤器链返回不再执行下面的代码
            return;
        }
        // 解析token
        String uid;
        try {
            // 解析token
            Claims claims = JwtUtil.parseJWT(token);
            uid = claims.getSubject();
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // 从redis中获取登录用户
        String redisKey = AuthConstant.LOGIN_USER_KEY + uid;
        String json = redisTemplate.opsForValue().get(redisKey);
        if (Objects.isNull(json)) {
            filterChain.doFilter(request, response);
            return;
        }
        LoginUserRes loginUser = JSON.parseObject(json, LoginUserRes.class);
        
        // todo 权限信息未设置
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, null);
        // 存入SecurityContextHolder,设置为已认证状态
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        
        // 放行
        filterChain.doFilter(request, response);
    }
}
