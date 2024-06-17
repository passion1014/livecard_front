package com.livecard.front.common;

import com.livecard.front.common.security.CustomAuthenticationFilter;
import com.livecard.front.common.security.CustomPortFilter;
import com.livecard.front.common.security.oauth.*;
import com.livecard.front.common.util.CommUtil;
//import com.livecard.front.member.service.CustomUserDetailServiceImpl;
import com.livecard.front.domain.repository.MbrUserRepository;
import com.livecard.front.domain.repository.RefreshTokenRepository;
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
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
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
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    //private final MemberService memberService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final MbrUserRepository mbrUserRepository;

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
                .httpBasic(AbstractHttpConfigurer::disable) //기본인증 미사용
                .formLogin(AbstractHttpConfigurer::disable) //스프링시큐리트 내장 폼로그인 미사용
                .logout(AbstractHttpConfigurer::disable)
                //세션 사용하지 않음
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests( (authorizeRequest) -> authorizeRequest
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/api/auth/**", "/api/token").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .clientRegistrationRepository(clientRepository())
                        //.loginPage("/login")
                        .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint.authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(oAuth2UserCustomService))
                        .successHandler(oAuth2SuccessHandler())
                )
                .exceptionHandling( (exceptionConfig) -> exceptionConfig
                        .authenticationEntryPoint(unauthorizedEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .addFilterAfter(new SecurityContextPersistenceFilter(), CustomAuthenticationFilter.class)
                .addFilterBefore(new CustomPortFilter(), UsernamePasswordAuthenticationFilter.class)

//                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

//                .addFilter()
//                .userDetailsService(customUserDetailService)

                .build();
    }


    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider,
                mbrUserRepository,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
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

    private ClientRegistrationRepository clientRepository() {
        return new InMemoryClientRegistrationRepository(kakaoClientRegistration(), naverClientRegistration(), googleClientRegistration());
    }

    private ClientRegistration kakaoClientRegistration() {
        return clientRegistrationRepository.findByRegistrationId(Provider.KAKAO.getName());
    }

    private ClientRegistration naverClientRegistration() {
        return clientRegistrationRepository.findByRegistrationId(Provider.NAVER.getName());
    }

    private ClientRegistration googleClientRegistration() {
        return clientRegistrationRepository.findByRegistrationId(Provider.GOOGLE.getName());
    }

}

