package com.livecard.front.common.security.oauth;

import com.livecard.front.common.util.CookieUtil;
import com.livecard.front.domain.entity.MbrUserEntity;
import com.livecard.front.domain.entity.OauthToken;
import com.livecard.front.domain.repository.MbrUserRepository;
import com.livecard.front.domain.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
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
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

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

        //=================================
        // Access-token, Refresh-token 조회
        //=================================
        //String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION); //토큰생성
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
        String principalName = oauthToken.getName();

        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(clientRegistrationId, principalName);
        OAuth2AccessToken accessToken = client.getAccessToken();
        OAuth2RefreshToken refreshToken = client.getRefreshToken();

        //==============================
        // Refresh-token 처리
        //==============================
        this.saveRefreshToken(user.getId(),
                Provider.getCodeByName(clientRegistrationId),
                accessToken.getTokenValue(),
                refreshToken.getTokenValue()); //토큰을 DB에 저장
        this.addRefreshTokenToCookie(request, response, refreshToken.getTokenValue()); // 토큰을 cookie로 보내기

        clearAuthenticationAttributes(request, response);

        //==============================
        // 페이지 리다이렉트
        //==============================
        //String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        String targetUrl =  UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", accessToken.getTokenValue())
                .build()
                .toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void saveRefreshToken(Long userId, String providerCd, String accessToken, String refreshToken ) {
        OauthToken oauthToken = refreshTokenRepository.findByMbrUserIdAndProviderCd(userId, providerCd)
                .map(entity -> entity.update(accessToken, refreshToken))
                .orElse(new OauthToken(userId, providerCd, accessToken, refreshToken));
        refreshTokenRepository.save(oauthToken);
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