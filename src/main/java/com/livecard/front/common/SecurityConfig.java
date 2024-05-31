package com.livecard.front.common;

import com.livecard.front.common.security.CustomAuthenticationFilter;
import com.livecard.front.common.security.CustomPortFilter;
import com.livecard.front.common.util.CommUtil;
//import com.livecard.front.member.service.CustomUserDetailServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
//    private final CustomUserDetailServiceImpl customUserDetailService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt 암호화
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(customUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(Arrays.asList(provider));
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(authenticationManagerBean());
        filter.setFilterProcessesUrl("/auth/authenticate"); // 로그인 경로 설정
        // 필터에 필요한 추가 설정 (예: setFilterProcessesUrl 등)
        return filter;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {


        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable
                ))
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .sessionConcurrency(concurrencyControlConfigurer ->
                                concurrencyControlConfigurer
                                        .maximumSessions(1) // 한 사용자당 최대 세션 수
                                        .expiredUrl("/login?expired") // 세션이 만료된 경우 리디렉션할 URL
                                        .maxSessionsPreventsLogin(true) // 새 세션 로그인 차단 여부
                        )
                )
                .authorizeHttpRequests( (authorizeRequest) -> authorizeRequest
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/", "/login", "/mobile/login").permitAll()
                        .requestMatchers("/member/findPass", "/mobile/member/findPass","/api/member/findPass").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/error/**").permitAll() // '/error' 경로에 대한 접근 허용
                        .requestMatchers("/css/**","/js/**","/images/**", "/vendor/**").permitAll() // '/정적 recource' 경로에 대한 접근 허용
                        .requestMatchers("/mobile/css/**","/mobile/js/**","/mobile/images/**").permitAll() // '/정적 recource' 경로에 대한 접근 허용
                        .anyRequest().authenticated()
                )
                .exceptionHandling( (exceptionConfig) -> exceptionConfig
                        .authenticationEntryPoint(unauthorizedEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .addFilterAfter(new SecurityContextPersistenceFilter(), CustomAuthenticationFilter.class)
                .addFilterBefore(new CustomPortFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // 기타 설정...
                // 401 403 관련 예외처리
//                .formLogin((formLogin) -> formLogin
//                        .loginPage("/login")
//                        .usernameParameter("username")
//                        .passwordParameter("password")
//                        .loginProcessingUrl("/auth/authenticate")
//                        .defaultSuccessUrl("/", true)
//                ) // 로그인
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                ) // 로그아웃
//                .addFilter()
//                .userDetailsService(customUserDetailService)
                .build();
        /*
        JWT 인증 방식 적용
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .authorizeHttpRequests(
                        authorize -> authorize.anyRequest().authenticated()
//                        (authorizeHttpRequests) -> authorizeHttpRequests.requestMatchers("/**").hasRole("USER")
                )
                // [로그인 방식에 대한 설정]
                // .httpBasic(Customizer.withDefaults()) // dialog box
                // .formLogin(Customizer.withDefaults()) // default login page
                .formLogin(form -> form.loginPage("/user/login").permitAll()) // login page
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
         */
    }



    public final AuthenticationEntryPoint unauthorizedEntryPoint =
            (request, response, authException) -> {
                CommUtil.handleRequestTypeError(request, response,

                        HttpStatus.UNAUTHORIZED.value(),
                        "Spring security unauthorized...");
            };

    public final AccessDeniedHandler accessDeniedHandler =
            (request, response, accessDeniedException) -> {
                CommUtil.handleRequestTypeError(request, response,
                        HttpStatus.FORBIDDEN.value(),
                        "Spring security forbidden...");
            };


    @Getter
    @RequiredArgsConstructor
    public static class ErrorResponse {
        private final HttpStatus status;
        private final String message;
    }
}
