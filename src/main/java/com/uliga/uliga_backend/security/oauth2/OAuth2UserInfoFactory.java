package com.uliga.uliga_backend.security.oauth2;

import java.util.Map;

import com.uliga.uliga_backend.security.oauth2.provider.GoogleOAuth2UserInfo;
import com.uliga.uliga_backend.security.oauth2.provider.KakaoOAuth2UserInfo;

public class OAuth2UserInfoFactory {
  public static OAuth2UserInfo getOAuth2UserInfo(String loginType, Map<String, Object> attributes) {
    return switch (loginType) {
      case "GOOGLE" -> new GoogleOAuth2UserInfo(attributes);
      case "KAKAO" -> new KakaoOAuth2UserInfo(attributes);
      default -> throw new IllegalArgumentException("Invalid Provider Type.");
    };
  }
}
