package com.uliga.uliga_backend.domain.Member.api;

import com.uliga.uliga_backend.domain.Member.application.AuthService;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.LoginResult;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.SignUpRequest;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.SignUpResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberAuthController {
    private final AuthService authService;


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
}
