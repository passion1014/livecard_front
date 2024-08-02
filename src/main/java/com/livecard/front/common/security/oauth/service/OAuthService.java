package com.livecard.front.common.security.oauth.service;


import com.livecard.front.domain.entity.MbrUserEntity;

public interface OAuthService {

    public String getNewAccessToken(String providerCd, String refreshToken);

    public String createNewAccessToken(String refreshToken);

    void unlinkUser();

    //public MbrUserEntity validateAccessToken(String accessToken);
}

