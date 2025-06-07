package com.uliga.uliga_backend.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.dto.member.MemberDTO.CodeConfirmDto;
import com.uliga.uliga_backend.dto.member.MemberDTO.ConfirmEmailDto;
import com.uliga.uliga_backend.dto.member.MemberDTO.EmailConfirmCodeDto;
import com.uliga.uliga_backend.dto.member.MemberDTO.EmailSentDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailCertificationService {
  // private final JavaMailSender emailSender;
  private final RedisTemplate<String, String> redisTemplate;
  // private final MemberRepository memberRepository;
  // private final PasswordEncoder passwordEncoder;
  // 인증 번호
  private String ePw;

  public Mono<EmailSentDto> sendEmail(ConfirmEmailDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'sendEmail'");
  }

  public Mono<CodeConfirmDto> confirmCode(EmailConfirmCodeDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'confirmCode'");
  }
}
