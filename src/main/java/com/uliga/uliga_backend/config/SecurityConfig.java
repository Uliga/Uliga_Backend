package com.uliga.uliga_backend.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.uliga.uliga_backend.security.JwtAccessDeniedHandler;
import com.uliga.uliga_backend.security.JwtAuthenticationEntryPoint;
import com.uliga.uliga_backend.security.LogoutSucessHandler;
import com.uliga.uliga_backend.security.filter.JwtAuthenticationFilter;
import com.uliga.uliga_backend.security.jwt.JwtTokenProvider;
import com.uliga.uliga_backend.security.oauth2.application.ReactiveCustomOAuth2UserService;
import com.uliga.uliga_backend.security.oauth2.handler.ReactiveOAuth2AuthenticationFailureHandler;
import com.uliga.uliga_backend.security.oauth2.handler.ReactiveOAuth2AuthenticationSuccessHandler;
import com.uliga.uliga_backend.security.oauth2.repository.ReactiveAuthorizationRequestRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
  private final RedisTemplate<String, String> redisTemplate;
  private final LogoutSucessHandler reactiveLogoutSuccessHandler;
  private final ReactiveOAuth2AuthenticationFailureHandler reactiveOAuth2AuthenticationFailureHandler;
  private final ReactiveOAuth2AuthenticationSuccessHandler reactiveOAuth2AuthenticationSuccessHandler;
  private final ReactiveCustomOAuth2UserService customOAuth2UserService;
  private final ReactiveAuthorizationRequestRepository reactiveAuthorizationRequestRepository;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public ReactiveAuthenticationManager jwtReactiveAuthenticationManager(JwtTokenProvider jwtTokenProvider) {
    return authentication -> {
      String token = authentication.getCredentials().toString();
      if (jwtTokenProvider.validateToken(token)) {
        Claims claims = jwtTokenProvider.getClaims(token);
        String userId = claims.getSubject();
        return Mono.just(new UsernamePasswordAuthenticationToken(
            userId,
            token,
            jwtTokenProvider.getAuthorities(token)));
      }
      return Mono.error(new BadCredentialsException("Invalid JWT token"));
    };
  }

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    return http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .logout(logout -> logout.logoutSuccessHandler(reactiveLogoutSuccessHandler))
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler))
        .authorizeExchange(exchange -> exchange
            .pathMatchers(HttpMethod.OPTIONS).permitAll()
            .pathMatchers("/", "/env_profile", "/actuator/health").permitAll()
            .pathMatchers("/actuator/**").authenticated()
            .pathMatchers("/oauth2/**", "/login/**", "/auth/**", "/logout-redirect").permitAll()
            .pathMatchers("/swagger-ui/**", "/v1/api-docs/**", "/rest-docs").permitAll()
            .pathMatchers("/member/**").hasRole("USER")
            .pathMatchers("/post/**", "/accountBook/**", "/budget/**", "/record/**", "/income/**", "/schedule/**")
            .authenticated()
            .anyExchange().denyAll())
        .addFilterAt(new JwtAuthenticationFilter(jwtReactiveAuthenticationManager(jwtTokenProvider),
            jwtTokenProvider,
            redisTemplate),
            SecurityWebFiltersOrder.AUTHENTICATION)
        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(Arrays.asList(
        "http://localhost:3000",
        "https://main.d211p9c5e1szy2.amplifyapp.com/",
        "https://ouruliga.com",
        "https://www.ouruliga.com",
        "https://api.ouruliga.com"));
    configuration.setAllowedMethods(Arrays.asList("HEAD", "POST", "GET", "DELETE", "PUT", "OPTIONS", "PATCH"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
