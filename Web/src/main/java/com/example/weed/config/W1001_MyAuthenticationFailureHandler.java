package com.example.weed.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class W1001_MyAuthenticationFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("Login failed: " + exception.getMessage());

        // 실패 이유를 request에 저장하여 로그인 페이지로 전달
        request.setAttribute("errorMessage", exception.getMessage());

        // 로그인 페이지로 이동
        request.getRequestDispatcher("/login").forward(request, response);
    }
}

