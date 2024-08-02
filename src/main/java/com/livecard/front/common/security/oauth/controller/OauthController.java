package com.livecard.front.common.security.oauth.controller;


import com.livecard.front.common.security.CustomUserDetails;
import com.livecard.front.common.security.oauth.service.OAuthService;
import com.livecard.front.common.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class OauthController {

    private final OAuthService OAuthService;

    /**
     * 신규 acces-tocken 발행
     * @param refreshToken
     * @return
     */
    @PostMapping("/token")
    public ResponseEntity<String> getNewAccessToken(@RequestBody String refreshToken) {
        refreshToken = refreshToken.replaceAll("\"", "");

        String providerCd = SecurityUtil.getUserOauthProviderCd(); //oauth 제공자
        String newAccessToken = OAuthService.getNewAccessToken(providerCd, refreshToken);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newAccessToken);
    }

    /**
     *  Oauth2 탈퇴
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/unlink")
    public ResponseEntity<String>  unlink(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken oauthToken = (UsernamePasswordAuthenticationToken) authentication;
        CustomUserDetails customUserDetails = (CustomUserDetails)oauthToken.getPrincipal();

        String providerCd = customUserDetails.getProviderCd(); //oauth 제공자
        String accessToken = (String)oauthToken.getCredentials();

        // 사용자 연결 해제
        OAuthService.unlinkUser(providerCd, accessToken);

        // 로그아웃 처리
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return ResponseEntity.status(HttpStatus.OK).build();

    }
}
