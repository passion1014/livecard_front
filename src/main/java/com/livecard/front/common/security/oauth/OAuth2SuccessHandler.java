package com.livecard.front.common.security.oauth;

import com.livecard.front.common.util.CookieUtil;
import com.livecard.front.domain.entity.MbrUserEntity;
import com.livecard.front.domain.entity.RefreshToken;
import com.livecard.front.domain.repository.MbrUserRepository;
import com.livecard.front.domain.repository.RefreshTokenRepository;
import com.livecard.front.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);

    //TODO:개발기/운영기 주소 설정
    public static final String REDIRECT_PATH = "http://localhost:3000/loginCallback";

    private final TokenProvider tokenProvider;
    private final MbrUserRepository mbrUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> responseAttribute = oAuth2User.getAttribute("response");
        String socialId = null;
        if (responseAttribute == null) { //naver
            socialId = oAuth2User.getName();
        }
        else {
            socialId = (String) responseAttribute.get("id");
        }

        //TODO: throw 처리
        MbrUserEntity user = mbrUserRepository.findBySocialId(socialId).orElseThrow();


        //==============================
        // Refresh-token 처리
        //==============================
        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION); //토큰생성
        this.saveRefreshToken(user.getId(), refreshToken); //토큰을 DB에 저장
        this.addRefreshTokenToCookie(request, response, refreshToken); // 토큰을 cookie로 보내기

        clearAuthenticationAttributes(request, response);

        //==============================
        // 페이지 리다이렉트
        //==============================
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        String targetUrl =  UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", accessToken)
                .build()
                .toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByMbrUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));
        refreshTokenRepository.save(refreshToken);
    }

    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }


}