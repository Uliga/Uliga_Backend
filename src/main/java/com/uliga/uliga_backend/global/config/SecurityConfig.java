package com.uliga.uliga_backend.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.global.jwt.CustomLogoutSuccessHandler;
import com.uliga.uliga_backend.global.jwt.JwtAccessDeniedHandler;
import com.uliga.uliga_backend.global.jwt.JwtAuthenticationEntryPoint;
import com.uliga.uliga_backend.global.jwt.JwtTokenProvider;
import com.uliga.uliga_backend.global.oauth2.application.CustomOAuth2UserService;
import com.uliga.uliga_backend.global.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.uliga.uliga_backend.global.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.uliga.uliga_backend.global.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final RedisTemplate<String, String> redisTemplate;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;
    private final ObjectMapper mapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource());

        // exception handling 할때 우리가 만든 클래스 추가
        http
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)


                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)

                .and()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.OPTIONS, "**").permitAll()
                        .requestMatchers("/actuator/**").authenticated()
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/logout-redirect").permitAll()
                        .requestMatchers("/member/**").hasRole("USER")
                        .requestMatchers("/post/**").authenticated()
                        .requestMatchers("/accountBook/**").authenticated()
                        .requestMatchers("/budget/**").authenticated()
                        .requestMatchers("/record/**").authenticated()
                        .requestMatchers("/income/**").authenticated()
                        .requestMatchers("/schedule/**").authenticated()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v1/api-docs/**").permitAll()
                );


        http
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization") //default
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository)
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);


        http
                .apply(new JwtSecurityConfig(jwtTokenProvider, mapper, redisTemplate))
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/logout-redirect")
                .clearAuthentication(true)
                .logoutSuccessHandler(customLogoutSuccessHandler);


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "POST", "GET", "DELETE", "PUT", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
