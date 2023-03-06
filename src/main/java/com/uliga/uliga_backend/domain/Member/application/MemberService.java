package com.uliga.uliga_backend.domain.Member.application;

import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.UpdateApplicationPasswordDto;
import com.uliga.uliga_backend.domain.Member.dto.NativeQuery.MemberInfoNativeQ;
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
    public MemberInfoNativeQ getCurrentMemberInfo(Long id) {
        return memberRepository.findMemberInfoById(id);
    }

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

    @Transactional
    public boolean checkPassword(Long id, PasswordCheck passwordCheck) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        return passwordEncoder.matches(passwordCheck.getPassword(), member.getPassword());

    }

    @Transactional
    public void updatePassword(Long id, UpdatePasswordDto passwordDto) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        passwordDto.encrypt(passwordEncoder);
        member.updatePassword(passwordDto.getNewPassword());
    }

    @Transactional
    public void updateAvatarUrl(Long id, UpdateAvatarUrl avatarUrl) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        member.updateAvatarUrl(avatarUrl.getAvatarUrl());
    }

    @Transactional
    public boolean nicknameExists(Long id, NicknameCheckDto nicknameCheckDto) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        if (member.getNickName().equals(nicknameCheckDto.getNickname())) {
            return true;
        } else {
            return memberRepository.existsByNickName(nicknameCheckDto.getNickname());
        }
    }

    @Transactional
    public void updateNickname(Long id, UpdateNicknameDto updateNicknameDto) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        member.updateNickname(updateNicknameDto.getNewNickname());
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        memberRepository.delete(member);
    }
}
