package com.uliga.uliga_backend.domain.Member.application;

import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.UpdateApplicationPasswordDto;
import com.uliga.uliga_backend.domain.Member.exception.InvalidApplicationPasswordException;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public boolean checkApplicationPassword(Long id, ApplicationPasswordCheck passwordCheck) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        return passwordEncoder.matches(passwordCheck.getApplicationPassword(), member.getApplicationPassword());
    }

    @Transactional
    public void updateApplicationPassword(Long id, UpdateApplicationPasswordDto passwordDto) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        passwordDto.encrypt(passwordEncoder);
        member.updateApplicationPassword(passwordDto.getNewPassword());

    }
}
