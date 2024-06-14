package com.livecard.front.common.service;

/**
 * OAuth2 처리 서비스
 */
public interface OAuthService {

    /**
     * 카카오 AccessToken 얻기
     * @param code
     * @return
     */
    public String getKakaoAccessToken(String code);

    /**
     * 네이버 AccessToken 얻기
     * @param code
     * @return
     */
    public String getNaverAccessToken(String code, String state);

    /**
     * 구글 AccessToken 얻기
     * @param code
     * @return
     */
    public String getGoogleAccessToken(String code);
}
