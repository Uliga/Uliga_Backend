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

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void updateApplicationPassword(Long id, UpdateApplicationPasswordDto updateApplicationPasswordDto) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        log.info(Long.toString(id));

        log.info(member.getApplicationPassword());
        log.info(updateApplicationPasswordDto.getOldPassword());
        if (!passwordEncoder.matches(updateApplicationPasswordDto.getOldPassword(), member.getApplicationPassword())) {
            throw new InvalidApplicationPasswordException("잘못된 현재 애플리케이션 비밀번호로 업데이트 실패");
        }
        updateApplicationPasswordDto.encrypt(passwordEncoder);
        member.updateApplicationPassword(updateApplicationPasswordDto.getNewPassword());

    }
}
