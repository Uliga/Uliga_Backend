package com.uliga.uliga_backend.security.oauth2.provider;

import java.util.Map;

import com.uliga.uliga_backend.member.model.UserLoginType;
import com.uliga.uliga_backend.security.oauth2.OAuth2UserInfo;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
  public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  public String getId() {
    return attributes.get("id").toString();
  }

  @Override
  public String getName() {
    Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

    if (properties == null) {
      return null;
    }

    return (String) properties.get("nickname");
  }

  @Override
  public String getEmail() {
    Map<String, Object> properties = (Map<String, Object>) attributes.get("kakao_account");

    if (properties == null) {
      return null;
    }
    return (String) properties.get("email");
  }

  @Override
  public String getProvider() {
    return UserLoginType.KAKAO.toString();
  }

}
