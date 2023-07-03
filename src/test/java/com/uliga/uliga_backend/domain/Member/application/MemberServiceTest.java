package com.uliga.uliga_backend.domain.Member.application;

import com.uliga.uliga_backend.domain.Member.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;

@Transactional
@SpringBootTest
class MemberServiceTest {
    @Autowired
    AuthService authService;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

}