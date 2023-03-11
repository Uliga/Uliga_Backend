package com.uliga.uliga_backend.domain.Member.api;

import com.uliga.uliga_backend.domain.Member.application.AuthService;
import com.uliga.uliga_backend.domain.Member.application.EmailCertificationService;
import com.uliga.uliga_backend.domain.Member.application.OAuth2MemberService;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;
import com.uliga.uliga_backend.domain.Member.dto.OAuthDTO;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;


@Tag(name = "사용자 인증", description = "사용자 인증 관련 API 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberAuthController {
    private final AuthService authService;
    private final EmailCertificationService emailCertificationService;

    private final OAuth2MemberService oAuth2MemberService;

    @Operation(summary = "회원가입 API", description = "회원가입 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = SignUpResult.class)))
    })
    @PostMapping(value = "/signup")
    public ResponseEntity<SignUpResult> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.info("회원 가입 요청 API 호출");
        return ResponseEntity.ok(SignUpResult.builder().result(authService.signUp(signUpRequest)).build());
    }

    @Operation(summary = "로그인 API", description = "로그인 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = LoginResult.class))),
            @ApiResponse(responseCode = "409", description = "로그인 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class )))
    })
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResult> loginJson(@RequestBody LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {

        log.info("로그인 요청 API 호출 - json");
        return ResponseEntity.ok(authService.login(loginRequest, response, request));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<LoginResult> loginForm(LoginRequest loginRequest, HttpServletResponse response,HttpServletRequest request) {
        log.info("로그인 요청 API 호출 - form");
        return ResponseEntity.ok(authService.login(loginRequest, response, request));
    }

    @PostMapping(value = "/social_login/{loginType}")
    public ResponseEntity<LoginResult> socialLogin(@Param("loginType") String loginType, @RequestBody OAuthDTO.SocialLoginDto loginDto, @Value("${oAuth.password}") String password, HttpServletResponse response,HttpServletRequest request) throws IOException {
        return ResponseEntity.ok(oAuth2MemberService.oAuthLogin(loginType.toUpperCase(), loginDto.getToken(), password, response, request));
    }

    @GetMapping(value = "/logout-redirect")
    public ResponseEntity<String> logoutRedirect() {
        return ResponseEntity.ok("LOGOUT");
    }

    @GetMapping(value = "/reissue")
    public ResponseEntity<TokenDTO.TokenIssueDTO> reissue(HttpServletResponse response,HttpServletRequest request) {
        log.info("토큰 재발급 요청 API 호출");
        return ResponseEntity.ok(authService.reissue(response, request));
    }

    // 이메일 인증 요청
    @PostMapping(value = "/mail", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailSentDto> mailConfirmJson(@RequestBody ConfirmEmailDto confirmEmailDto) throws Exception {
        log.info("이메일 인증 요청 API 호출 - json");
        emailCertificationService.sendSimpleMessage(confirmEmailDto.getEmail());
        return ResponseEntity.ok(EmailSentDto.builder().email(confirmEmailDto.getEmail()).success(true).build());
    }

    @PostMapping(value = "/mail", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<EmailSentDto> mailConfirmForm(ConfirmEmailDto confirmEmailDto) throws Exception {
        log.info("이메일 인증 요청 API 호출 - form");
        emailCertificationService.sendSimpleMessage(confirmEmailDto.getEmail());
        return ResponseEntity.ok(EmailSentDto.builder().email(confirmEmailDto.getEmail()).success(true).build());
    }


    // 코드 인증 요청
    @PostMapping(value = "/mail/code", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CodeConfirmDto> codeConfirmJson(@RequestBody EmailConfirmCodeDto emailConfirmCodeDto) {

        log.info("코드 인증 요청 API 호출");
        return ResponseEntity.ok(emailCertificationService.confirmCode(emailConfirmCodeDto));

    }

    @PostMapping(value = "/mail/code", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<CodeConfirmDto> codeConfirmForm(@RequestBody EmailConfirmCodeDto emailConfirmCodeDto) {

        log.info("코드 인증 요청 API 호출");
        return ResponseEntity.ok(emailCertificationService.confirmCode(emailConfirmCodeDto));

    }



    // 이메일 중복 확인
    @GetMapping(value = "/mail/exists/{email}")
    public ResponseEntity<ExistsCheckDto> emailExists(@PathVariable("email") String email) {
        log.info("이메일 중복 확인 API 호출");
        return ResponseEntity.ok(authService.emailExists(email));
    }

    // 닉네임 중복 확인
    @GetMapping(value = "/nickname/exists/{nickname}")
    public ResponseEntity<ExistsCheckDto> nicknameExists(@PathVariable("nickname") String nickname) {
        log.info("닉네임 중복 확인 API 호출");
        return ResponseEntity.ok(authService.nicknameExists(nickname));
    }
}
