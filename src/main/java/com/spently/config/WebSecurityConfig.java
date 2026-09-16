package com.spently.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spently.enums.RestData;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@Slf4j
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-resources/**",
            "/auth/**",
            "/images/**",
            "/menu/**",
    };

    private static final String[] ADMIN = {
            "/dashboard/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors-> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(WHITELIST).permitAll()

                        .requestMatchers(HttpMethod.PUT, "/user/profile").authenticated()
                        .requestMatchers(HttpMethod.GET, "/user/profile").authenticated()

                        .requestMatchers(ADMIN).hasRole(Constant.ROLE_ADMIN)
                        .requestMatchers(HttpMethod.GET, "/user/*").hasRole(Constant.ROLE_ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/user/*").hasRole(Constant.ROLE_ADMIN)
                        .requestMatchers(HttpMethod.GET, "/user").hasRole(Constant.ROLE_ADMIN)
                        .requestMatchers(HttpMethod.POST, "/user").hasRole(Constant.ROLE_ADMIN)
                        .requestMatchers(HttpMethod.PATCH, "/user/reset-pass/*").hasRole(Constant.ROLE_ADMIN)
                        .requestMatchers(HttpMethod.PATCH, "/user/change-status/*").hasRole(Constant.ROLE_ADMIN)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.info("Go into accessDeniedHandler");
                            try {
                                if (!response.isCommitted()) {
                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                    response.setContentType("application/json;charset=UTF-8");
                                    Map<String,Object> errorResponse = new LinkedHashMap<>();
                                    errorResponse.put("status", RestData.ERROR);
                                    errorResponse.put("message", "Access Denied");
                                    response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(errorResponse));
                                }
                            } catch (IOException e) {
                                log.error("Error writing Access Denied response: {}", e.getMessage(), e);
                            }
                        }))
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
