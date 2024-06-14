package com.livecard.front.common.web.controller;


import com.livecard.front.common.service.OAuthService;
import com.livecard.front.common.service.OAuthServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * OAuth2 인증 컨트롤ㄹ러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@Slf4j
public class OAuthController {

    private OAuthService oAuthService;

    /**
     * 카카오 인증
     * @param code
     * @return
     */
    @GetMapping("/kakao/accesstoken")
    public String getKakaoAccessToken (@RequestParam String code) {
        String token = oAuthService.getKakaoAccessToken(code);
        //KakaoDataForm res = kakaoService.createKakaoUser(token);

        //return loginService.KakaoLogin(res);
        return "";
    }

    /** 네이버 인증
     *
     * @param code
     * @param state
     * @return
     */
    @GetMapping("/naver")
    public String NaverLogin (@RequestParam String code, String state) {
        String accessToken = oAuthService.getNaverAccessToken(code, state);
        //return naverService.getUserInfo(accessToken);
        return "";
    }

    /**
     * 구글 인증
     * @param code
     * @return
     */
    @GetMapping("/google")
    public String GoogleLogin (@RequestParam String code) {
        String accessToken = oAuthService.getGoogleAccessToken(code);

        //return googleService.getUserInfo(accessToken);
        return "";
    }




}
