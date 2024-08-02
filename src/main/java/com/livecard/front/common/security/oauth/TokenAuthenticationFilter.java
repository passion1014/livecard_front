package com.livecard.front.common.security.oauth;

import com.livecard.front.common.security.oauth.service.OAuthService;
import com.livecard.front.common.util.SecurityUtil;
import com.livecard.front.domain.entity.MbrUserEntity;
import com.livecard.front.domain.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final OAuthService oAuthService;

    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)  throws ServletException, IOException {


        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String token = getAccessToken(authorizationHeader);

        //if (tokenProvider.validToken(token)) {
//        if (oAuthService.isValidAccessToken(token)) {
//            Authentication authentication = tokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }

        MbrUserEntity mbrUserEntity = oAuthService.validateAccessToken(token);
        Authentication authentication = tokenProvider.getAuthentication(token, mbrUserEntity);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}

