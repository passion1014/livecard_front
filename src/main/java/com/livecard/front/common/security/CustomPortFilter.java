package com.livecard.front.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomPortFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (httpRequest.getLocalPort() == 8079) {
            // 8079 포트로 들어온 요청이면 세션 검증을 건너뛰고 체인을 계속 진행
            chain.doFilter(request, response);
        } else {
            // 기타 요청에 대해서는 다음 필터로 전달
            chain.doFilter(request, response);
        }
    }
}