package com.livecard.front.common.service;


import com.livecard.front.domain.entity.OauthToken;

public interface RefreshTokenService {
    public OauthToken findByRefreshToken(String refreshToken);

    public void removeRefreshTokenByMbrUserId(Long mbrUserid);


}
