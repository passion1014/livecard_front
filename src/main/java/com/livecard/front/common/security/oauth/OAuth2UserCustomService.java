package com.livecard.front.common.security.oauth;


import com.livecard.front.domain.entity.MbrUserEntity;
import com.livecard.front.domain.repository.MbrUserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final MbrUserRepository mbrUserRepository;
    //private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User user = super.loadUser(userRequest); // ❶ 요청을 바탕으로 유저 정보를 담은 객체 반환

        saveOrUpdate(user, provider);
        return user;
    }

    // ❷ 유저가 있으면 업데이트, 없으면 유저 생성
    private MbrUserEntity saveOrUpdate(OAuth2User oAuth2User, String provider) {
        String socialId = "";
        String name ;
        String profileImage;

        //================================================
        // Provider 별로 프로필 정보 획득
        //================================================
        if (Provider.KAKAO.getName().equals(provider)) {
            Map<String, Object> properties = oAuth2User.getAttribute("properties");
            long id = oAuth2User.getAttribute("id");
            socialId = String.valueOf(id);
            name = (String)properties.get("nickname");
            profileImage = (String)properties.get("profile_image");
        }
        else if (Provider.NAVER.getName().equals(provider)) {
            Map<String, Object> attributes =  oAuth2User.getAttributes();
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");;

            socialId = (String) response.get("id");
            profileImage = (String) response.get("profile_image");
            name = (String) response.get("name");
        }
        else if (Provider.GOOGLE.getName().equals(provider)) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            socialId = oAuth2User.getAttribute("sub");
            name = oAuth2User.getAttribute("name");
            profileImage = oAuth2User.getAttribute("picture");
        } else {
            profileImage = null;
            name = null;
        }

        //================================================
        // 프로필 정보를 MbrUser 테이블에 Insert or Update
        //================================================
        if (StringUtils.isEmpty(socialId)) {
            //TODO: throw
            throw new OAuth2AuthenticationException("인증오류");
        }

        MbrUserEntity user = mbrUserRepository.findBySocialId(socialId)
                //User user = mbrUserRepository.findByEmail(email)
                .map(entity -> {
                            entity.setName(name);
                            entity.setProfileImg(profileImage);
                            return entity;
                        }
                )
                .orElse(MbrUserEntity.builder()
                        .socialId(socialId)
                        .name(name)
                        .role("User")
                        .providerCd(Provider.getCodeByName(provider))
                        .profileImg(profileImage)
                        .build());

        //user = mbrUserRepository.save(user);
        return mbrUserRepository.save(user);

//        //================================================
//        // Token Insert or Update
//        //================================================
//        String refreshToken = (String)oAuth2User.getAttribute("refresh_token");
//        RefreshToken refreshTokenEntiry = refreshTokenRepository.findByMbrUserIdAndProviderCd(user.getId(), providerCd)
//                .map(entity -> entity.update(refreshToken))
//                .orElse(new RefreshToken(user.getId(), providerCd, refreshToken));
//        refreshTokenRepository.save(refreshTokenEntiry);
//        return user;
    }

}