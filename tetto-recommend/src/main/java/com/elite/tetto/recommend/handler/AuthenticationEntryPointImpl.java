/*
    Author: 刘子阳.
    Date: 2022-07-15 11:32.
    Created by IntelliJ IDEA.
*/
package com.elite.tetto.recommend.handler;

import com.alibaba.fastjson.JSON;
import com.elite.tetto.common.exception.ExceptionCode;
import com.elite.tetto.common.utils.R;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 *
 * @author Elziy
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        R result = R.error(ExceptionCode.UNAUTHORIZED.getCode(), ExceptionCode.UNAUTHORIZED.getMsg());
        String json = JSON.toJSONString(result);
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
