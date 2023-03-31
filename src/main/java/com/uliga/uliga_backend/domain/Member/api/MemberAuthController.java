package com.uliga.uliga_backend.domain.Member.api;

import com.uliga.uliga_backend.domain.Member.application.AuthService;
import com.uliga.uliga_backend.domain.Member.application.EmailCertificationService;
import com.uliga.uliga_backend.domain.Member.application.OAuth2MemberService;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;
import com.uliga.uliga_backend.domain.Member.dto.NativeQ.MemberInfoNativeQ;
import com.uliga.uliga_backend.domain.Member.dto.OAuthDTO;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO.TokenIssueDTO;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;


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
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = SignUpResult.class))),
            @ApiResponse(responseCode = "409", description = "잘못된 값 형식이 넘어왔을때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/signup")
    public ResponseEntity<SignUpResult> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {

        log.info("회원 가입 요청 API 호출");
        authService.signUp(signUpRequest);
        return ResponseEntity.ok(SignUpResult.builder().result("CREATED").build());
    }

    @Operation(summary = "로그인 API", description = "로그인 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = LoginResult.class))),
            @ApiResponse(responseCode = "409", description = "로그인 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResult> loginJson(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {

        log.info("로그인 요청 API 호출 - json");
        return ResponseEntity.ok(authService.login(loginRequest, response, request));
    }


    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<LoginResult> loginForm
            (LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {

        log.info("로그인 요청 API 호출 - form");
        return ResponseEntity.ok(authService.login(loginRequest, response, request));
    }

    @Operation(summary = "소셜 로그인 API - 미구현", description = "소셜 로그인 API 입니다")
    @PostMapping(value = "/social_login/{loginType}")
    public ResponseEntity<LoginResult> socialLogin(@Parameter(name = "loginType", description = "로그인 타입", in = PATH) @Param("loginType") String loginType,
                                                   @RequestBody OAuthDTO.SocialLoginDto loginDto,
                                                   @Value("${oAuth.password}") String password,
                                                   HttpServletResponse response,
                                                   HttpServletRequest request) throws IOException {

        return ResponseEntity.ok(oAuth2MemberService.oAuthLogin(loginType.toUpperCase(), loginDto.getToken(), password, response, request));
    }

    @Operation(summary = "로그아웃시 리다이렉트 API", description = "로그아웃시 호출되는 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
    @GetMapping(value = "/logout-redirect")
    public ResponseEntity<String> logoutRedirect() {
        return ResponseEntity.ok("LOGOUT");
    }


    @Operation(summary = "토큰 재발급 요청 API", description = "토큰 재발급시 호출하는 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공", content = @Content(schema = @Schema(implementation = TokenIssueDTO.class))),
            @ApiResponse(responseCode = "401", description = "토큰 재발급 실패, 만료된 리프레쉬", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/reissue")
    public ResponseEntity<TokenIssueDTO> reissue(HttpServletResponse response, HttpServletRequest request) {

        log.info("토큰 재발급 요청 API 호출");
        return ResponseEntity.ok(authService.reissue(response, request));
    }

    @Operation(summary = "이메일 인증 요청 API", description = "이메일 인증 요청 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 성공", content = @Content(schema = @Schema(implementation = EmailSentDto.class))),
            @ApiResponse(responseCode = "409", description = "잘못된 값 형식이 넘어왔을때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/mail", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailSentDto> mailConfirmJson(@Valid @RequestBody ConfirmEmailDto confirmEmailDto) throws Exception {

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

    @Operation(summary = "이메일 인증 코드 확인 API", description = "이메일 인증 코드 확인 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코드 일치 여부 확인", content = @Content(schema = @Schema(implementation = CodeConfirmDto.class), examples = {
                    @ExampleObject(name = "일치시", summary = "일치시", value = "'matches':true", description = "코드 일치시"),
                    @ExampleObject(name = "불일치시", summary = "불일치시", value = "'matches':false", description = "코드 불일치시")
            })),
            @ApiResponse(responseCode = "409", description = "잘못된 값 형식이 넘어왔을때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/mail/code", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CodeConfirmDto> codeConfirmJson(@Valid @RequestBody EmailConfirmCodeDto emailConfirmCodeDto) {

        log.info("코드 인증 요청 API 호출");
        return ResponseEntity.ok(emailCertificationService.confirmCode(emailConfirmCodeDto));

    }

    @PostMapping(value = "/mail/code", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<CodeConfirmDto> codeConfirmForm(@RequestBody EmailConfirmCodeDto emailConfirmCodeDto) {

        log.info("코드 인증 요청 API 호출");
        return ResponseEntity.ok(emailCertificationService.confirmCode(emailConfirmCodeDto));

    }


    @Operation(summary = "이메일 중복 확인 API", description = "이메일 중복 확인 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "중복여부 확인", content = @Content(schema = @Schema(implementation = ExistsCheckDto.class), examples = {
                    @ExampleObject(name = "중복시", summary = "중복시", value = "'exists':true", description = "중복하는 이메일 존재할때"),
                    @ExampleObject(name = "중복 아닐시", summary = "중복 아닐시", value = "'exists':false", description = "중복하는 이메일 존재하지 않을때")
            })),
            @ApiResponse(responseCode = "409", description = "잘못된 값 형식시 넘어왔을때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/mail/exists/{email}")
    public ResponseEntity<ExistsCheckDto> emailExists(@Parameter(name = "email", description = "중복 확인 하려는 이메일", in = ParameterIn.PATH)
                                                      @PathVariable("email") String email) {

        log.info("이메일 중복 확인 API 호출");
        return ResponseEntity.ok(authService.emailExists(email));
    }


    @Operation(summary = "닉네임 중복 확인 API", description = "닉네임 중복 확인 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "중복여부 확인", content = @Content(schema = @Schema(implementation = ExistsCheckDto.class), examples = {
                    @ExampleObject(name = "중복시", summary = "중복시", value = "'exists':true", description = "중복하는 닉네임 존재할때"),
                    @ExampleObject(name = "중복 아닐시", summary = "중복 아닐시", value = "'exists':false", description = "중복하는 닉네임 존재하지 않을때")
            }))
    })
    @GetMapping(value = "/nickname/exists/{nickname}")
    public ResponseEntity<ExistsCheckDto> nicknameExists(@Parameter(name = "nickname", description = "중복 확인하려는 닉네임", in = ParameterIn.PATH)
                                                         @PathVariable("nickname") String nickname) {
        log.info("닉네임 중복 확인 API 호출");
        return ResponseEntity.ok(authService.nicknameExists(nickname));
    }

    @GetMapping(value = "/mybatis/test")
    public ResponseEntity<List<MemberInfoNativeQ>> mybatisTest(@RequestParam(name = "id", required = false) Long id, @RequestParam(name = "name", required = false) String name) {
        HashMap<String, Object> requirements = new HashMap<>();
        requirements.put("id", id);
        requirements.put("name", name);
        return ResponseEntity.ok(authService.mybatisTest(requirements));
    }
}
