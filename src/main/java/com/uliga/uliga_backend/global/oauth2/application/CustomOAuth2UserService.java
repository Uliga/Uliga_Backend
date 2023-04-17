package com.uliga.uliga_backend.global.oauth2.application;

import com.uliga.uliga_backend.domain.AccountBook.application.AccountBookService;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.model.Authority;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Member.model.UserLoginType;
import com.uliga.uliga_backend.domain.Member.model.UserPrincipal;
import com.uliga.uliga_backend.global.oauth2.OAuth2UserInfo;
import com.uliga.uliga_backend.global.oauth2.OAuth2UserInfoFactory;
import com.uliga.uliga_backend.global.oauth2.exception.DuplicateUserByEmail;
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

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final AccountBookService accountBookService;
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("");
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        UserLoginType loginType =  UserLoginType.valueOfLabel(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        log.info("loginType : " + loginType);
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        log.info("userNameAttributeName : " + userNameAttributeName);
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(loginType.getType(), oAuth2User.getAttributes());
        log.info("userEmail : "+userInfo.getEmail());
        Optional<Member> byEmailAndDeleted = memberRepository.findByEmailAndDeleted(userInfo.getEmail(), false);
        Member member;
        if (byEmailAndDeleted.isPresent()) {
            member = byEmailAndDeleted.get();
        } else {
            member = createUser(userInfo, loginType);
            AccountBookDTO.CreateRequestPrivate requestPrivate = AccountBookDTO.CreateRequestPrivate.builder().name(member.getUserName() + " 님의 가계부").relationship("개인").isPrivate(true).build();
            AccountBook accountBookPrivate = accountBookService.createAccountBookPrivate(member, requestPrivate);
            member.setPrivateAccountBook(accountBookPrivate);
        }

        return UserPrincipal.create(member, oAuth2User.getAttributes());


    }

    private Member createUser(OAuth2UserInfo memberInfo, UserLoginType loginType) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (memberInfo.getEmail() == null) {
            String encode = passwordEncoder.encode("NULL");
            Member member = Member.builder().email("NULL")
                    .userName(memberInfo.getName())
                    .nickName(memberInfo.getName())
                    .password(encode)
                    .deleted(false)
                    .userLoginType(loginType)
                    .authority(Authority.ROLE_USER)
                    .build();
            return memberRepository.save(member);
        } else {
            String encode = passwordEncoder.encode(memberInfo.getEmail());
            Member member = Member.builder().email(memberInfo.getEmail())
                    .userName(memberInfo.getName())
                    .nickName(memberInfo.getName())
                    .password(encode)
                    .deleted(false)
                    .userLoginType(loginType)
                    .authority(Authority.ROLE_USER)
                    .build();
            return memberRepository.save(member);
        }


    }
}
