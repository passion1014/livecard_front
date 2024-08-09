package com.livecard.front.common.security.oauth;

import com.livecard.front.common.util.CookieUtil;
import com.livecard.front.domain.entity.MbrUserEntity;
import com.livecard.front.domain.entity.OauthToken;
import com.livecard.front.domain.repository.MbrUserRepository;
import com.livecard.front.domain.repository.OauthTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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

    @Value("${jwt.expiration}") String accessTokenExpiration;
    @Value("${jwt.refresh-token.expiration}") String refreshTokenExpiration;
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    //TODO:개발기/운영기 주소 설정
    public static final String REDIRECT_PATH = "http://localhost:3000/loginCallback";

    private final TokenProvider tokenProvider;
    private final MbrUserRepository mbrUserRepository;
    private final OauthTokenRepository oauthTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        Duration durationOfAccessTokenExpiration = Duration.ofHours(Long.parseLong(accessTokenExpiration));
        Duration  durationOfRefreshTokenExpiration = Duration.ofHours(Long.parseLong(refreshTokenExpiration));

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

        //===========================================
        // provider의 Access-token / Refresh-token 조회
        //===========================================
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
        String principalName = oauthToken.getName();
        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(clientRegistrationId, principalName);
        String providerAccessToken = client.getAccessToken().getTokenValue();
        String providerRefreshToken = client.getRefreshToken().getTokenValue();

        //==================================
        // Refresh-token 생성 후 쿠키로 내보내기
        //==================================
        String refreshToken = tokenProvider.generateToken(user, durationOfRefreshTokenExpiration); //refresh token생성
        int cookieMaxAge = (int) durationOfRefreshTokenExpiration.toSeconds();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

        //===================================
        // Token 정보 DB에 저장
        //====================================
        this.saveRefreshToken(user.getId(),
                Provider.getCodeByName(clientRegistrationId),
                refreshToken,
                providerAccessToken,
                providerRefreshToken);

        //==================================
        // Access Token생성 후 페이지 리다이렉트
        //==================================
        String accessToken = tokenProvider.generateToken(user, durationOfAccessTokenExpiration); //access token생성
        String targetUrl =  UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", accessToken)
                .build()
                .toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void saveRefreshToken(Long userId, String providerCd, String refreshToken, String providerAccessToken, String providerRefreshToken ) {
        OauthToken oauthToken = oauthTokenRepository.findByMbrUserIdAndProviderCd(userId, providerCd)
                .map(entity -> entity.update(refreshToken, providerAccessToken, providerRefreshToken))
                .orElse(new OauthToken(userId, providerCd, refreshToken, providerAccessToken, providerRefreshToken));
        oauthTokenRepository.save(oauthToken);
    }

}