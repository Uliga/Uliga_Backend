package com.uliga.uliga_backend.util;

import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.uliga.uliga_backend.common.annotation.WithMockCustomUser;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
    final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        annotation.userName(),
        "password",
        Arrays.asList(new SimpleGrantedAuthority(annotation.role())));
    securityContext.setAuthentication(authenticationToken);

    return securityContext;
  }
}
