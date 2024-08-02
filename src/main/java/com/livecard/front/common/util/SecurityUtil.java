package com.livecard.front.common.util;

import com.livecard.front.common.exception.SearchNotFoundException;
import com.livecard.front.common.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static CustomUserDetails getUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                CustomUserDetails customUserDetails = (CustomUserDetails) principal;
                return customUserDetails;
            }
        }

        return null;
    }

    /**
     * 로그인 된 사용자의 소셜인증 제공자 코드
     * (KAKAO=0, NAVER=1, GOOGLE=2)
     * @return
     */
    public static String getUserOauthProviderCd()  {
        CustomUserDetails customUserDetails = getUserDetails();
        if (customUserDetails == null) {
            //TODO:KJM 예외
            throw new SearchNotFoundException("No providerCd .");
        }
        return customUserDetails.getProviderCd();
    }
}
