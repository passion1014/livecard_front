package com.livecard.front.common.util;

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
}
