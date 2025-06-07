package com.uliga.uliga_backend.util;

// public class WithMockCustomUserSecurityContextFactory implements
// WithSecurityContextFactory<WithMockCustomUser> {
// @Override
// public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
// final SecurityContext securityContext =
// SecurityContextHolder.createEmptyContext();
// final UsernamePasswordAuthenticationToken authenticationToken = new
// UsernamePasswordAuthenticationToken(
// annotation.userName(),
// "password",
// Arrays.asList(new SimpleGrantedAuthority(annotation.role())));
// securityContext.setAuthentication(authenticationToken);

// return securityContext;
// }
// }
