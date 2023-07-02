package com.uliga.uliga_backend.global.oauth2.application;

import com.uliga.uliga_backend.domain.Member.repository.MemberRepository;
import com.uliga.uliga_backend.domain.Member.model.Authority;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Member.model.UserLoginType;
import com.uliga.uliga_backend.domain.Member.model.UserPrincipal;
import com.uliga.uliga_backend.global.oauth2.OAuth2UserInfo;
import com.uliga.uliga_backend.global.oauth2.OAuth2UserInfoFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        UserLoginType loginType =  UserLoginType.valueOfLabel(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(loginType.getType(), oAuth2User.getAttributes());
//        Optional<Member> byEmailAndDeleted = memberRepository.findByEmailAndDeleted(userInfo.getEmail(), false);
//        Member member = byEmailAndDeleted.orElseGet(() -> createUser(userInfo, loginType));
        Member member = memberRepository.findByEmailAndDeleted(userInfo.getEmail(), false).orElseGet(() -> createUser(userInfo, loginType));
        return UserPrincipal.create(member, oAuth2User.getAttributes());


    }

    private Member createUser(OAuth2UserInfo memberInfo, UserLoginType loginType) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(memberInfo.getEmail());
        return Member.builder().email(memberInfo.getEmail())
                .userName(memberInfo.getName())
                .password(encode)
                .deleted(false)
                .userLoginType(loginType)
                .authority(Authority.ROLE_USER)
                .build();
    }
}
