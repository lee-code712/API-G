package com.digital.v3.interceptor;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
	
	public static List<String> authEssential = Arrays.asList("/rest/**");
	public static List<String> authInessential = Arrays.asList("/rest/person/signUp", "/rest/person/login", 
			"/rest/auth/generateToken");
    
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String token = request.getHeader("Authorization");

        // token null 확인 (token 값이 null일 때 header에 넣어 request를 요청하면 400 에러 발생)
        if (token == null) {
        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        	response.setContentType("application/json");
        	response.setCharacterEncoding("UTF-8");
        	response.getWriter().write("{\"errorCode\":\"401\",\"errorMsg\":\"인증 토큰이 유효하지 않습니다.\"}");
        	return false;
        }
        return true;
    }
    
}
