package com.uliga.uliga_backend.domain.Member.application;

import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQuery.AccountBookInfoQ;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.UpdateApplicationPasswordDto;
import com.uliga.uliga_backend.domain.Member.dto.NativeQuery.MemberInfoNativeQ;
import com.uliga.uliga_backend.domain.Member.exception.InvalidApplicationPasswordException;
import com.uliga.uliga_backend.domain.Member.exception.UserNotFoundByEmail;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final AccountBookRepository accountBookRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public GetMemberInfo getCurrentMemberInfo(Long id) {

        MemberInfoNativeQ memberInfoById = memberRepository.findMemberInfoById(id);
        return GetMemberInfo.builder()
                .memberInfo(memberInfoById)
                .invitations(null).build();
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
    // JPQL로 리팩터링해야할듯? 공유 가계부가 좀 걸림, 개인 가계부는 다 지우면 되는데, 회원이 탈퇴했는데, 공유 가계부에 다른 사람들이 있으면
    // 지우면 안되고, 공유 가계부에 사람이 없으면 지워야하니까 ㅇㅇ
    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        List<AccountBookInfoQ> accountBookInfosByMemberId = accountBookRepository.findAccountBookInfosByMemberId(id);
        for (AccountBookInfoQ accountBookInfoQ : accountBookInfosByMemberId) {
            if (accountBookInfoQ.getIsPrivate()) {
                accountBookRepository.deleteById(accountBookInfoQ.getAccountBookId());
            }
        }
        memberRepository.delete(member);
    }

    @Transactional
    public SearchEmailResult findMemberByEmail(SearchMemberByEmail byEmail) {
        Member member = memberRepository.findByEmail(byEmail.getEmail()).orElseThrow(UserNotFoundByEmail::new);

        return SearchEmailResult.builder()
                .id(member.getId())
                .nickName(member.getNickName())
                .userName(member.getUserName()).build();
    }
}
