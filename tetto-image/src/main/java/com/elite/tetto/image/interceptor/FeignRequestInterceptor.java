package com.elite.tetto.image.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Feign请求拦截器请求前加上token
 *
 * @author Elziy
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {
    
    @Override
    public void apply(RequestTemplate template) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attr = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attr.getRequest();
        String token = request.getHeader("token");//网关传过来的 token
        if (StringUtils.hasText(token)) {
            template.header("token", token);
        }
    }
}
