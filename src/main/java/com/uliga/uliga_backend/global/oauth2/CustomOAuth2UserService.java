package com.uliga.uliga_backend.global.oauth2;

import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2UserService delegate = new DefaultOAuth2UserService();
//        OAuth2User oAuth2User = delegate.loadUser(userRequest);
//        Map<String, Object> attributes = oAuth2User.getAttributes();
//
//        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
//        String email = (String) kakao_account.get("email");
//        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
//        String nickname = (String) properties.get("nickname");
//        log.info("email : " + email + " nickname : " + nickname);
//
//        if (memberRepository.existsByEmailAndDeleted(email, false)) {
//            log.info("가입한 적 있음");
//
//        } else {
//            Member member = Member.builder().userName(nickname).userLoginType(UserLoginType.KAKAO).email(email).deleted(false).userName(nickname).applicationPassword("1234").authority(Authority.ROLE_USER).build();
//            log.info("가입한 적 없음");
//            memberRepository.save(member);
//        }
//
//        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), attributes, "id");
        // 성공 정보를 바탕으로 객체를 만든다
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        // 생성된 Service 객체로부터 User를 받는다.
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 받은 User로부터 user 정보를 가져온다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        log.info("registrationId : " + registrationId);
        log.info("userNameAttributeName : " + userNameAttributeName);

        // SuccessHandler가 사용할 수있도록 등록해준다
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        var stringObjectMap = oAuth2Attribute.convertToMap();
        log.info("registrationId : " + registrationId);
        if (memberRepository.existsByEmailAndDeleted(oAuth2Attribute.getEmail(), false)) {
            log.info("email : " + oAuth2Attribute.getEmail() + " exists True");
        } else {
            log.info("email : "+oAuth2Attribute.getEmail()+" exists False");
        }
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), stringObjectMap, "email");
    }
}
