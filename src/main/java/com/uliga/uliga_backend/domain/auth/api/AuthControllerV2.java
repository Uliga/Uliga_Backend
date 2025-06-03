package com.uliga.uliga_backend.domain.auth.api;

import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uliga.uliga_backend.domain.auth.application.AuthReactiveService;
import com.uliga.uliga_backend.domain.member.application.EmailCertificationService;
import com.uliga.uliga_backend.domain.member.dto.MemberDTO.CodeConfirmDto;
import com.uliga.uliga_backend.domain.member.dto.MemberDTO.ConfirmEmailDto;
import com.uliga.uliga_backend.domain.member.dto.MemberDTO.EmailConfirmCodeDto;
import com.uliga.uliga_backend.domain.member.dto.MemberDTO.EmailSentDto;
import com.uliga.uliga_backend.domain.member.dto.MemberDTO.ExistsCheckDto;
import com.uliga.uliga_backend.domain.member.dto.MemberDTO.LoginRequest;
import com.uliga.uliga_backend.domain.member.dto.MemberDTO.LoginResult;
import com.uliga.uliga_backend.domain.member.dto.MemberDTO.SignUpRequest;
import com.uliga.uliga_backend.domain.member.dto.MemberDTO.SignUpResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Tag(name = "사용자 인증 API", description = "사용자 인증 API 입니다")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v2/auth")
public class AuthControllerV2 {
  private final AuthReactiveService authService;
  private final EmailCertificationService emailCertificationService;

  @Operation(summary = "회원 가입 API")
  @PostMapping("signup")
  public ResponseEntity<Mono<SignUpResult>> signUp(@Valid @RequestBody SignUpRequest dto) {
    return ResponseEntity.ok(authService.signup(dto));
  }

  @Operation(summary = "로그인 API")
  @PostMapping("login")
  public ResponseEntity<Mono<LoginResult>> login(@Valid @RequestBody LoginRequest dto) {
    return ResponseEntity.ok(authService.login(dto));
  }

  @Operation(summary = "로그아웃 API")
  @PostMapping("logout")
  public ResponseEntity<Mono<String>> logout(ServerHttpRequest request) {
    return ResponseEntity.ok(authService.logout(request));
  }

  @Operation(summary = "이메일 인증 요청 API")
  @PostMapping("mail")
  public ResponseEntity<Mono<EmailSentDto>> mailConfirm(@Valid @RequestBody ConfirmEmailDto dto) {
    return ResponseEntity.ok(emailCertificationService.sendEmail(dto));
  }

  @Operation(summary = "이메일 인증 요청 코드 확인 API")
  @PostMapping("mail/code")
  public ResponseEntity<Mono<CodeConfirmDto>> codeConfirm(@Valid @RequestBody EmailConfirmCodeDto dto) {
    return ResponseEntity.ok(emailCertificationService.confirmCode(dto));
  }

  @Operation(summary = "닉네임 중복 확인 API")
  @GetMapping(value = "/nickname/exists/{nickname}")
  public ResponseEntity<Mono<ExistsCheckDto>> nicknameExists(
      @Parameter(name = "nickname", description = "중복 확인하려는 닉네임", in = ParameterIn.PATH) @PathVariable("nickname") String nickname) {

    return ResponseEntity.ok(authService.nicknameExists(nickname));
  }
}
