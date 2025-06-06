package com.uliga.uliga_backend.security.jwt;

import static com.uliga.uliga_backend.common.constants.JwtConstants.ACCESS_TOKEN_EXPIRE_TIME;
import static com.uliga.uliga_backend.common.constants.JwtConstants.AUTHORITIES_KEY;
import static com.uliga.uliga_backend.common.constants.JwtConstants.BEARER_TYPE;
import static com.uliga.uliga_backend.common.constants.JwtConstants.REFRESH_TOKEN_EXPIRE_TIME;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.uliga.uliga_backend.domain.token.dto.TokenDTO.TokenInfoDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {
  @Value("${jwt.secret}")
  private String secretKey; // Base64-encoded secret key from application.yml

  @Value("${jwt.expirationMillis:86400000}")
  private long validityInMilliseconds; // Default 1 day

  private Key key;

  @PostConstruct
  public void init() {
    byte[] decoded = Base64.getDecoder().decode(secretKey);
    this.key = Keys.hmacShaKeyFor(decoded);
  }

  public TokenInfoDTO generateTokenDto(Authentication authentication) {
    // 권한 가져오기
    String authorities = authentication
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    long now = (new Date()).getTime();

    // AccessToken 생성
    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
    String accessToken = Jwts.builder()
        .setSubject(authentication.getName())
        .claim(AUTHORITIES_KEY, authorities)
        .setExpiration(accessTokenExpiresIn)
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();

    // Refresh Token 생성
    String refreshToken = Jwts.builder()
        .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();

    return TokenInfoDTO.builder()
        .grantType(BEARER_TYPE)
        .accessToken(accessToken)
        .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
        .refreshToken(refreshToken).build();
  }

  public Authentication getAuthentication(String accessToken) {
    // 토큰 복호화
    Claims claims = parseClaims(accessToken);

    if (claims.get(AUTHORITIES_KEY) == null) {
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    // 클레임에서 권한 정보 가져오기
    Collection<? extends GrantedAuthority> authorities = Arrays.stream(
        claims.get(AUTHORITIES_KEY).toString().split(",")).map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    // UserDetails 객체를 만들어서 Authentication 리턴
    UserDetails principal = new User(claims.getSubject(), "", authorities);
    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {
      log.info("만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {
      log.info("지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT 토큰이 잘못되었습니다.");
    }

    return false;
  }

  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  /**
   * Generate a JWT token with username and roles
   */
  public String createToken(String username, List<String> roles) {
    Claims claims = Jwts.claims().setSubject(username);
    claims.put("roles", roles);

    Date now = new Date();
    Date expiry = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * Validate token: signature and expiration
   */
  // public boolean validateToken(String token) {
  // try {
  // Jwts.parserBuilder()
  // .setSigningKey(key)
  // .build()
  // .parseClaimsJws(token);
  // return true;
  // } catch (Exception ex) {
  // return false;
  // }
  // }

  /**
   * Parse and return all claims from token
   */
  public Claims getClaims(String token) {
    Jws<Claims> jws = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token);
    return jws.getBody();
  }

  /**
   * Extract roles from claims and convert to GrantedAuthority list
   */
  @SuppressWarnings("unchecked")
  public Collection<? extends GrantedAuthority> getAuthorities(String token) {
    Claims claims = getClaims(token);
    Object rolesObject = claims.get("roles");
    if (rolesObject instanceof List) {
      List<String> roles = ((List<Object>) rolesObject).stream()
          .map(Object::toString)
          .collect(Collectors.toList());
      return roles.stream()
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  /**
   * Convenience method to get username (subject) from token
   */
  public String getUsername(String token) {
    return getClaims(token).getSubject();
  }
}
