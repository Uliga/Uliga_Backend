package com.uliga.uliga_backend.domain.Member.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookInfoQ;
import com.uliga.uliga_backend.domain.JoinTable.dao.AccountBookMemberRepository;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.dto.NativeQ.MemberInfoNativeQ;
import com.uliga.uliga_backend.domain.Member.exception.UserExistsInAccountBook;
import com.uliga.uliga_backend.domain.Member.exception.UserNotFoundByEmail;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AccountBookRepository accountBookRepository;
    private final AccountBookMemberRepository accountBookMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, Object> objectRedisTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public GetMemberInfo getCurrentMemberInfo(Long id, Pageable pageable) throws JsonProcessingException {

        MemberInfoNativeQ memberInfoById = memberRepository.findMemberInfoById(id);
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        String email = memberInfoById.getEmail();
        Set<String> strings = setOperations.members(email);
        List<InvitationInfo> result = new ArrayList<>();
        List<NotificationInfo> notificationInfos = new ArrayList<>();
        Set<String> stringSet = setOperations.members(memberInfoById.getNickName());
        if (strings != null) {
            for (String o : strings) {
                result.add(objectMapper.readValue(o, InvitationInfo.class));
            }
        }
//        Collections.sort(result, Collections.reverseOrder());
        if (stringSet != null) {
            for (String o : stringSet) {
                notificationInfos.add(objectMapper.readValue(o, NotificationInfo.class));
            }
        }
        Collections.sort(notificationInfos, Collections.reverseOrder());
        // 나중에 페이징 도입하면 여기 고치면된다
//        List<InvitationInfo> invitationInfos = result.subList((int) pageable.getOffset(), pageable.getPageSize());
//        new PageImpl<>(invitationInfos, pageable, result.size());
        return GetMemberInfo.builder()
                .memberInfo(memberInfoById)
                .invitations(result)
                .notifications(notificationInfos).build();
    }

    @Transactional
    public boolean checkApplicationPassword(Long id, ApplicationPasswordCheck passwordCheck) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        return passwordEncoder.matches(passwordCheck.getApplicationPassword(), member.getApplicationPassword());
    }

    @Transactional
    public boolean checkPassword(Long id, PasswordCheck passwordCheck) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        return passwordEncoder.matches(passwordCheck.getPassword(), member.getPassword());

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
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        List<AccountBookInfoQ> accountBookInfosByMemberId = accountBookRepository.findAccountBookInfosByMemberId(id);
        for (AccountBookInfoQ accountBookInfoQ : accountBookInfosByMemberId) {
            if (accountBookInfoQ.getIsPrivate()) {
                accountBookRepository.deleteById(accountBookInfoQ.getAccountBookId());
            }
        }
        member.updateNickname("탈퇴한 유저");
        member.updateUserName("탈퇴한 유저");

    }

    @Transactional
    public SearchEmailResult findMemberByEmail(Long accountBookId, SearchMemberByEmail byEmail) {
        Member member = memberRepository.findByEmail(byEmail.getEmail()).orElseThrow(UserNotFoundByEmail::new);
        if (accountBookId != null) {
            if (accountBookMemberRepository.existsAccountBookMemberByMemberIdAndAccountBookId(member.getId(), accountBookId)) {
                throw new UserExistsInAccountBook(member.getUserName());
            }
        }

        return SearchEmailResult.builder()
                .id(member.getId())
                .nickName(member.getNickName())
                .userName(member.getUserName()).build();
    }

    @Transactional
    public MemberInfoUpdateRequest updateMemberInfo(Long id, Map<String, Object> updates) {
        MemberInfoUpdateRequest memberInfoUpdateRequest = objectMapper.convertValue(updates, MemberInfoUpdateRequest.class);
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        if (memberInfoUpdateRequest.getApplicationPassword() != null) {
            String encode = passwordEncoder.encode(memberInfoUpdateRequest.getApplicationPassword());
            member.updateApplicationPassword(encode);
        }
        if (memberInfoUpdateRequest.getAvatarUrl() != null) {
            member.updateAvatarUrl(memberInfoUpdateRequest.getAvatarUrl());
        }
        if (memberInfoUpdateRequest.getPassword() != null) {
            String encode = passwordEncoder.encode(memberInfoUpdateRequest.getPassword());
            member.updatePassword(encode);
        }
        if (memberInfoUpdateRequest.getNickName() != null) {
            member.updateNickname(memberInfoUpdateRequest.getNickName());
        }
        return memberInfoUpdateRequest;
    }

    @Transactional
    public void deleteMemberNotification(Long id) {
        SetOperations<String, Object> setOperations = objectRedisTemplate.opsForSet();
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        Long size = setOperations.size(member.getNickName());
        if (size != null) {

            setOperations.pop(member.getNickName(), size);
        }
    }
}
