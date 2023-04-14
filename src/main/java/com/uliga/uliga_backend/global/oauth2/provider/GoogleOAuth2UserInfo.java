package com.uliga.uliga_backend.global.oauth2.provider;

import com.uliga.uliga_backend.domain.Member.model.UserLoginType;
import com.uliga.uliga_backend.global.oauth2.OAuth2UserInfo;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getProvider(){
        return UserLoginType.GOOGLE.toString();
    }

}
