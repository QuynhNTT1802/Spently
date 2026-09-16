package com.spently.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spently.enums.RestData;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String,Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("status", RestData.ERROR);
        errorResponse.put("message", authException.getMessage());
        response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(errorResponse));
    }
}
//handle exception when request rejected because of not authorization (not yet login, expired token, wrong token...)
//this is required in JWT security flow of Spring Security
