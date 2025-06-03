package com.uliga.uliga_backend.global.util;

import java.util.Base64;
import java.util.Optional;

import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.SerializationUtils;
import org.springframework.web.server.ServerWebExchange;

/**
 * Utility for handling cookies in reactive ServerWebExchange.
 */
public class ReactiveCookieUtil {
    /**
     * Retrieve a cookie by name from the request.
     */
    public static Optional<HttpCookie> getCookie(ServerWebExchange exchange, String name) {
        return Optional.ofNullable(exchange.getRequest().getCookies().getFirst(name));
    }

    /**
     * Add a cookie to the response.
     */
    public static void addCookie(ServerHttpResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .httpOnly(true)
                .maxAge(maxAge)
                .build();
        response.addCookie(cookie);
    }

    /**
     * Delete a cookie by setting its maxAge to 0.
     */
    public static void deleteCookie(ServerHttpResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .path("/")
                .maxAge(0)
                .build();
        response.addCookie(cookie);
    }

    /**
     * Deserialize a Base64-encoded cookie value to an object.
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(String cookieValue, Class<T> cls) {
        byte[] decoded = Base64.getUrlDecoder().decode(cookieValue);
        return cls.cast(SerializationUtils.deserialize(decoded));
    }
}