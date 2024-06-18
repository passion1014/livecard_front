package com.livecard.front.common.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
//    @Autowired
//    private AuthInterceptor authInterceptor; // 추가된 부분

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authInterceptor)
//                .excludePathPatterns("/login", "/login/**", "/member/findPass", "/api/member/findPass", "/error/**", "/css/**", "/js/**", "/images/**", "/vendor/**", "/mobile/css/**", "/mobile/js/**", "/mobile/images/**", "/auth/**");

        // 로그인 페이지 및 정적 리소스에 대한 경로를 제외
//        registry.addInterceptor(memberInfoInterceptor)
//                .excludePathPatterns("/login", "/login/**", "/error", "/css/**", "/js/**", "/images/**");
        // 회원정보 인터셉터도 로그인 페이지 및 정적 리소스에 대한 경로를 제외
    }

//    //TODO: 로컬일때만 되게 수정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowCredentials(true);
    }
}
