package com.livecard.front.common.util;

import com.livecard.front.common.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SessionUtil {
//    private final HttpServletRequest request;
//
//    @Autowired
//    public SessionUtil(HttpServletRequest request) {
//        this.request = request;
//    }

//    public static CustomUserDetails getCustomUserDetails() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//        return customUserDetails;
//    }
//
//    public static Long getFarmCode() {
//        return getCustomUserDetails().getMemberFarmCode();
//    }
//
//    public static String getMemberId() {
//        return getCustomUserDetails().getMemberId();
//    }
//
//    public static Long getMemberPK() {
//        return getCustomUserDetails().getId();
//    }

}
