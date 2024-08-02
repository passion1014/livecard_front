package com.livecard.front.common.security.oauth.service;

import com.livecard.front.common.exception.SearchNotFoundException;
import com.livecard.front.common.security.CustomUserDetails;
import com.livecard.front.common.security.oauth.Provider;
import com.livecard.front.common.security.oauth.TokenProvider;
import com.livecard.front.common.util.SecurityUtil;
import com.livecard.front.domain.entity.MbrUserEntity;
import com.livecard.front.domain.entity.OauthToken;
import com.livecard.front.domain.repository.MbrUserRepository;
import com.livecard.front.domain.repository.OauthTokenRepository;
import com.livecard.front.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuthServiceImpl implements OAuthService {
    private static final Logger logger = LoggerFactory.getLogger(OAuthServiceImpl.class);

    private final TokenProvider tokenProvider;
    private final OauthTokenRepository oauthTokenRepository;
    private final MbrUserRepository mbrUserRepository;
    private final RestTemplate restTemplate;
    private final MemberService memberService;

    @Value("${spring.security.oauth2.client.provider.kakao.unlink-uri}") private String kakaoUnlinkUri;
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}") private String kakaoTokenUri;
    @Value("${spring.security.oauth2.client.provider.kakao.token-info-uri}") private String kakaoTokenInfoUri;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}") private String kakaoClientId;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}") private String kakaoClientSecret;

    @Value("${spring.security.oauth2.client.provider.naver.unlink-uri}") private String naverUnlinkUri;
    @Value("${spring.security.oauth2.client.provider.naver.token-uri}") private String naverTokenUri;
    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}") private String naverUserInfoUri;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}") private String naverClientSecret;
    @Value("${spring.security.oauth2.client.registration.naver.client-id}") private String naverClientId;

    @Value("${spring.security.oauth2.client.provider.google.unlink-uri}") private String googleUnlinkUri;
    @Value("${spring.security.oauth2.client.provider.google.token-info-uri}") private String googleTokenInfoUri;
    @Value("${spring.security.oauth2.client.provider.google.token-uri}") private String googleTokenUri;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}") private String googleClientSecret;
    @Value("${spring.security.oauth2.client.registration.google.client-id}") private String googleClientId;

    public String getNewAccessToken(String providerCd, String refreshToken) {
        HttpHeaders headers = new HttpHeaders();

        String url = null;
        String data = null;

        if (providerCd.equals(Provider.KAKAO.getCode())) {
            url = kakaoTokenUri;
            data = "grant_type=refresh_token" +
                    "&client_id=" + kakaoClientId +
                    "&refresh_token=" + refreshToken +
                    "&client_secret=" + kakaoClientSecret;
            //headers.setBearerAuth(accessToken);
        }
        else if (providerCd.equals(Provider.NAVER.getCode())) {
            url = naverTokenUri;
            data = "grant_type=refresh_token" +
                    "&client_id=" + naverClientId +
                    "&client_secret=" + naverClientSecret +
                    "&refresh_token=" + refreshToken;
        }
        else if (providerCd.equals(Provider.GOOGLE.getCode())) {
            url = googleTokenUri;
            data = "grant_type=refresh_token" +
                    "&client_id=" + googleClientId +
                    "&client_secret=" + googleClientSecret +
                    "&refresh_token=" + refreshToken;
        }
        HttpEntity<String> entity = new HttpEntity<>(data, headers);
        Map<String,?> responseBody = sendHttpRequest(url, headers, entity, HttpMethod.POST);
        String accessToken = (String) responseBody.get("access_token");
        
        //TODO:KJM accessToken null이면 예외
        return accessToken;
    }


    public String createNewAccessToken(String refreshToken) {
        // refresh token 유효성 검사해서 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)) {
            //TODO EXception변경
            throw new IllegalArgumentException("Unexpected token");
        }

        Long mbrUserId =  SecurityUtil.getMbrUserId();

        //TODO: throw 일으키기
        MbrUserEntity mbrUserEntity = mbrUserRepository.findById(mbrUserId).orElseThrow();

        //TODO:KJM 토큰갱신정보 프로퍼티에서 가져오기
        return tokenProvider.generateToken(mbrUserEntity, Duration.ofHours(2));
    }

    public void unlinkUser() {

        CustomUserDetails customUserDetails = SecurityUtil.getUserDetails();
        Long mbrUserId = customUserDetails.getId();
        String providerCd = customUserDetails.getProviderCd(); //oauth 제공자

        //============================
        // Provider의 access token조회
        //============================
        OauthToken oauthToken = oauthTokenRepository.findByMbrUserIdAndProviderCd(mbrUserId, providerCd)
                .orElseThrow(()->new SearchNotFoundException("no access token"));
        String accessToken = oauthToken.getProviderAccessToken();

        //=====================
        // 사용자 계정관련 테이블 삭제
        //=====================
        memberService.deleteMember(mbrUserId);

        //========================
        // Provider 별 unlink 호출
        //=========================
        HttpHeaders headers = new HttpHeaders();
        String url = null;
        String data = null;

        if (providerCd.equals(Provider.KAKAO.getCode())) {
            url = kakaoUnlinkUri;
            headers.setBearerAuth(accessToken);
        }
        else if (providerCd.equals(Provider.NAVER.getCode())) {
            url = naverUnlinkUri;
            data = "client_id=" + naverClientId +
                    "&client_secret=" + naverClientSecret +
                    "&access_token=" + accessToken +
                    "&service_provider=NAVER" +
                    "&grant_type=delete";
        }
        else if (providerCd.equals(Provider.GOOGLE.getCode())) {
            url = googleUnlinkUri;
            data = "token=" + accessToken;
        }
        HttpEntity<String> entity = new HttpEntity<>(data, headers);
        sendHttpRequest(url, headers, entity, HttpMethod.POST);
    }

//    public MbrUserEntity validateAccessToken(String accessToken) {
//        Optional<OauthToken> optionalOauthToken = oauthTokenRepository.findByAccessToken(accessToken);
//        if (optionalOauthToken.isEmpty()) {
//            new SearchNotFoundException("no access token");
//        }
//
//        String providerCd = optionalOauthToken.get().getProviderCd();
//        HttpHeaders headers = new HttpHeaders();
//        String url = null;
//        String data = null;
//        HttpMethod httpMethod = null;
//
//        if (providerCd.equals(Provider.KAKAO.getCode())) {
//            url = kakaoTokenInfoUri;
//            httpMethod = HttpMethod.GET;
//            headers.setBearerAuth(accessToken);
//        }
//        else if (providerCd.equals(Provider.NAVER.getCode())) {
//            url = naverUserInfoUri;
//            httpMethod = HttpMethod.GET;
//            headers.setBearerAuth(accessToken);
//        }
//        else if (providerCd.equals(Provider.GOOGLE.getCode())) {
//            url = googleTokenInfoUri;
//            httpMethod = HttpMethod.POST;
//            data = "access_token=" + accessToken;
//        }
//        HttpEntity<String> entity = new HttpEntity<>(data, headers);
//        Map<String,?> responseBody = sendHttpRequest(url, headers, entity, httpMethod);
//
//        String socialId = null;
//        if (providerCd.equals(Provider.KAKAO.getCode())) {
//            socialId = String.valueOf(responseBody.get("id")); //socialId
//        }
//        else if (providerCd.equals(Provider.NAVER.getCode())) {
//            if (!"00".equals(responseBody.get("resultcode"))) {
//                //TODO:KJM 403 에러
//            }
//            Map<String, String> response = (Map<String, String>)responseBody.get("response");
//            socialId = String.valueOf(response.get("id")); //socialId
//        }
//        else if (providerCd.equals(Provider.GOOGLE.getCode())) {
//            socialId = String.valueOf(responseBody.get("sub")); //socialId
//        }
//
//        if (!"00".equals(responseBody.get("resultcode"))) {
//            //TODO:KJM 403 에러
//        }
//
//        return mbrUserRepository.findBySocialId(socialId).orElseThrow(() -> new SearchNotFoundException("Unexpected socialId"));
//    }

    //add callback function as a parameter
    private Map<String,?> sendHttpRequest(String url, HttpHeaders headers, HttpEntity<String> entity, HttpMethod httpMethod) {
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        restTemplate.setInterceptors(Collections.singletonList(
                new ClientHttpRequestInterceptor() {
                    private final Logger logger = LoggerFactory.getLogger(this.getClass());

                    @Override
                    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                        logRequest(request, body);
                        ClientHttpResponse response = execution.execute(request, body);
                        logResponse(response);
                        return response;
                    }

                    private void logRequest(HttpRequest request, byte[] body) throws IOException {
                        logger.debug("===========================request begin================================================");
                        logger.debug("URI         : {}", request.getURI());
                        logger.debug("Method      : {}", request.getMethod());
                        logger.debug("Headers     : {}", request.getHeaders());
                        logger.debug("Request body: {}", new String(body, StandardCharsets.UTF_8));
                        logger.debug("==========================request end================================================");
                    }

                    private void logResponse(ClientHttpResponse response) throws IOException {
                        logger.debug("============================response begin==========================================");
                        logger.debug("Status code  : {}", response.getStatusCode());
                        logger.debug("Status text  : {}", response.getStatusText());
                        logger.debug("Headers      : {}", response.getHeaders());
                        logger.debug("Response body: {}", StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8));
                        logger.debug("=======================response end=================================================");
                    }
                }
        ));

        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                url,
                httpMethod,
                entity,
                Map.class
        );

        // Get the response status code and body
        HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();

        Map<String,?> responseBody = responseEntity.getBody();
        logger.debug("responseBody={}", responseBody);

        if (statusCode.equals(HttpStatus.OK)) {
            return responseBody;
        }
        else {
            //TODO:KJM 예외
            throw new RuntimeException("Failed to revoke token");
        }
    }
}


