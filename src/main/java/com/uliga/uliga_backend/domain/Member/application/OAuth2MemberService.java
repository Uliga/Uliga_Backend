package com.uliga.uliga_backend.domain.Member.application;

import com.uliga.uliga_backend.domain.Member.Component.GoogleAuth;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.LoginResult;
import com.uliga.uliga_backend.domain.Member.dto.OAuthDTO;
import com.uliga.uliga_backend.domain.Member.exception.UnknownLoginException;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Member.model.UserLoginType;
import com.uliga.uliga_backend.global.common.constants.OAuthConstants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2MemberService {

    private final GoogleAuth googleAuth;

    private final AuthService authService;

    private final MemberRepository memberRepository;
    @Transactional
    public LoginResult oAuthLogin(String loginType, String token, String password) throws IOException {

        switch (loginType) {
            case OAuthConstants.GOOGLE: {
//                log.info("구글 로그인 코드" + code);
//                // 구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답 객체를 받아옴
//                ResponseEntity<String> accessTokenResponse = googleAuth.requestAccessToken(code);
                // 응답객체가 JSON 형식으로 되어 있으니, 이를 역직렬화해서 자바 객체에 담음
//                GoogleOAuthToken oAuthToken = googleAuth.getAccessToken(token);
                // 액세스 토큰을 다시 구글로 보내 사용자 정보가 담긴 응답 객체를 받아옴
                ResponseEntity<String> userInfoResponse = googleAuth.requestUserInfo(token);
                // 다시 Json 형식의 응답 객체를 자바 객체로 역 직렬화
                OAuthDTO.GoogleUser googleUser = googleAuth.getUserInfo(userInfoResponse);
                Optional<Member> byEmail = memberRepository.findByEmail(googleUser.getEmail());
                // 이메일로 디비에 유저 존재
                if (byEmail.isPresent()) {
                    Member member = byEmail.get();
                    // 구글 로그인으로 생성된 유저
                    if (member.getUserLoginType() != UserLoginType.GOOGLE) {

                        throw new IllegalArgumentException("디비 이메일 중복");
                    }

                } else {
                    MemberDTO.SocialSignUpRequest signUpRequest = MemberDTO.SocialSignUpRequest.builder()
                            .email(googleUser.getEmail())
                            .userLoginType(UserLoginType.GOOGLE)
                            .password(password)
                            .build();

                    authService.socialSignUp(signUpRequest);

                }
                return authService.login(MemberDTO.LoginRequest.builder().email(googleUser.getEmail()).password(password).build());

            }
            default: {
                throw new UnknownLoginException();
            }
        }

    }
}
