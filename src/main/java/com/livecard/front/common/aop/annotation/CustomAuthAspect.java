package com.livecard.front.common.aop.annotation;

import com.livecard.front.common.annotation.CustomAuth;
import com.livecard.front.common.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.naming.NoPermissionException;

@Aspect
@Component
@RequiredArgsConstructor
public class CustomAuthAspect {
//    private final MoGradePermissionsService moGradePermissionsService;

    @Before("@annotation(customAuth)")
    public void beforeCustomAuth(JoinPoint joinPoint, CustomAuth customAuth) throws NoPermissionException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String menuId = customAuth.menuId();
        String groupId = customAuth.groupId();
        String permission = customAuth.permission();

//        boolean hasPermission = moGradePermissionsService.checkPermission(menuId, groupId, moGrade, permission);
        boolean hasPermission = true;
        if (!hasPermission) {
            // 권한이 없을 경우 예외를 던집니다.
            throw new NoPermissionException("No sufficient permission.");
        }

    }
}