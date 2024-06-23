package com.livecard.front.common.security.oauth.service;

import com.livecard.front.common.security.oauth.TokenProvider;
import com.livecard.front.common.service.RefreshTokenService;
import com.livecard.front.domain.entity.MbrUserEntity;
import com.livecard.front.domain.repository.MbrUserRepository;
import com.livecard.front.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService{

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MbrUserRepository mbrUserRepository;

    public String createNewAccessToken(String refreshToken) {


        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)) {
            //TODO EXception변경
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getMbrUserId();
        //TODO: throw 일으키기
        MbrUserEntity mbrUserEntity = mbrUserRepository.findById(userId).orElseThrow();

        return tokenProvider.generateToken(mbrUserEntity, Duration.ofHours(2));
    }
}

