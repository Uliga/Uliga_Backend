package com.uliga.uliga_backend.domain.Member.api;

import com.uliga.uliga_backend.domain.Member.application.AuthService;
import com.uliga.uliga_backend.domain.Member.application.EmailCertificationService;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.LoginResult;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.SignUpRequest;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.SignUpResult;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO.AccessTokenDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberAuthController {
    private final AuthService authService;
    private final EmailCertificationService emailCertificationService;


    @PostMapping(value = "/signup")
    public ResponseEntity<SignUpResult> signUp(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(SignUpResult.builder().result(authService.signUp(signUpRequest)).build());
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResult> loginJson(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<LoginResult> loginForm(LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @GetMapping(value = "/logout-redirect")
    public ResponseEntity<String> logoutRedirect() {
        return ResponseEntity.ok("LOGOUT");
    }

    @PostMapping(value = "/reissue")
    public ResponseEntity<TokenDTO.TokenIssueDTO> reissue(@RequestBody AccessTokenDTO accessTokenDTO) {
        return ResponseEntity.ok(authService.reissue(accessTokenDTO));
    }

    // 이메일 인증 요청
    @PostMapping(value = "/mail")
    public ResponseEntity<EmailSentDto> mailConfirm(@RequestBody ConfirmEmailDto confirmEmailDto) throws Exception {

        emailCertificationService.sendSimpleMessage(confirmEmailDto.getEmail());
        return ResponseEntity.ok(EmailSentDto.builder().email(confirmEmailDto.getEmail()).success(true).build());
    }

    // 코드 인증 요청
    @PostMapping(value = "/mail/code")
    public ResponseEntity<CodeConfirmDto> codeConfirm(@RequestBody EmailConfirmCodeDto emailConfirmCodeDto) {
        return ResponseEntity.ok(emailCertificationService.confirmCode(emailConfirmCodeDto));

    }

    // 이메일 중복 확인
    @GetMapping(value = "/mail/exists/{email}")
    public ResponseEntity<ExistsCheckDto> emailExists(@PathVariable("email") String email) {
        return ResponseEntity.ok(authService.emailExists(email));
    }


}
