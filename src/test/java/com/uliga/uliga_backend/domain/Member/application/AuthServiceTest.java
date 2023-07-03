package com.uliga.uliga_backend.domain.Member.application;

import com.uliga.uliga_backend.domain.AccountBook.repository.AccountBookRepository;
import com.uliga.uliga_backend.domain.Member.repository.MemberRepository;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.LoginRequest;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.SignUpRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Transactional
@SpringBootTest
class AuthServiceTest {
    @Autowired
    AuthService authService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AccountBookRepository accountBookRepository;



}