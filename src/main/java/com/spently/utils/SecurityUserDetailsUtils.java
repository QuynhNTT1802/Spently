package com.spently.utils;


import com.spently.config.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

public class SecurityUserDetailsUtils {
    private static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (isNull(authentication)) {
            return null;
        }
        return authentication;
    }

    public static Object getAuthenticatedUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            return (CustomUserDetails) authentication.getPrincipal();
        }
        else {
            return authentication.getPrincipal();
        }
    }

    public static List<String> getAuthorities() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    public static Boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return Boolean.FALSE;
        }
        if (authentication.getName().equals("anonymousUser")) {
            return Boolean.FALSE;
        }
        if (isNull(authentication.getPrincipal())) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static Boolean isNull(Object object) {
        return object == null;
    }

}
