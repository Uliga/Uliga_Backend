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
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
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

    /**
     * 멤버 개인 가계부 아이디 조회
     * @param id 멤버 아이디
     * @return 멤버 개인 가계부 아이디
     */
    @Transactional(readOnly = true)
    public Long getMemberPrivateAccountBookId(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));
        return member.getPrivateAccountBook().getId();
    }

    /**
     * 로그인한 멤버 정보 조회 메서드
     * @param id 멤버 아이디
     * @param pageable 페이징 정보
     * @return 로그인한 멤버 정보
     * @throws JsonProcessingException 레디스 관련 예외
     */
    @Transactional(readOnly = true)
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

    /**
     * 애플리케이션 비밀번호 확인 메서드
     * @param id 멤버 아이디
     * @param passwordCheck 유저가 입력한 애플리케이션 비밀번호
     * @return 비밀번호 일치 여부
     */
    @Transactional(readOnly = true)
    public boolean checkApplicationPassword(Long id, ApplicationPasswordCheck passwordCheck) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));
        return passwordEncoder.matches(passwordCheck.getApplicationPassword(), member.getApplicationPassword());
    }

    /**
     * 비밀번호 확인 메서드
     * @param id 멤버 아이디
     * @param passwordCheck 확인할 비밀번호
     * @return 비밀번호 일치여부
     */
    @Transactional(readOnly = true)
    public boolean checkPassword(Long id, PasswordCheck passwordCheck) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));
        return passwordEncoder.matches(passwordCheck.getPassword(), member.getPassword());

    }

    /**
     * 닉네임 중복 여부 확인 메서드
     * @param id 멤버 아이디
     * @param nicknameCheckDto 중복 확인할 닉네임
     * @return 중복 여부
     */
    @Transactional(readOnly = true)
    public boolean nicknameExists(Long id, NicknameCheckDto nicknameCheckDto) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));
        if (member.getNickName().equals(nicknameCheckDto.getNickname())) {
            return true;
        } else {
            return memberRepository.existsByNickNameAndDeleted(nicknameCheckDto.getNickname(), false);
        }
    }

    /**
     * 멤버 탈퇴 메서드
     * @param id 멤버 아이디
     */
    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));

        accountBookRepository.deleteById(member.getPrivateAccountBook().getId());
        member.delete();



        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.getAndDelete(Long.toString(id));

    }

    /**
     * 이메일로 존재하는 멤버 검색 메서드
     * @param accountBookId 가계부 아이디
     * @param byEmail 찾을 이메일
     * @return 이메일 검색 결과
     */
    @Transactional(readOnly = true)
    public SearchEmailResult findMemberByEmail(Long accountBookId, SearchMemberByEmail byEmail) {
        Member member = memberRepository.findByEmailAndDeleted(byEmail.getEmail(), false).orElseThrow(UserNotFoundByEmail::new);
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

    /**
     * 멤버 정보 업데이트
     * @param id 멤버 아이디
     * @param updates 업데이트할 정보 map
     * @return 업데이트 결과
     */
    @Transactional
    public MemberInfoUpdateRequest updateMemberInfo(Long id, Map<String, Object> updates) {
        MemberInfoUpdateRequest memberInfoUpdateRequest = objectMapper.convertValue(updates, MemberInfoUpdateRequest.class);
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));
        if (memberInfoUpdateRequest.getApplicationPassword() != null) {
            String encode = passwordEncoder.encode(memberInfoUpdateRequest.getApplicationPassword());
            member.updateApplicationPassword(encode);
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

    /**
     * 멤버 알림 삭제 메서드
     * @param id 멤버 아이디
     */
    @Transactional
    public void deleteMemberNotification(Long id) {
        SetOperations<String, Object> setOperations = objectRedisTemplate.opsForSet();
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));
        Long size = setOperations.size(member.getNickName());
        if (size != null) {

            setOperations.pop(member.getNickName(), size);
        }
    }

    /**
     * 가계부 멤버 삭제 메서드
     * @param accountBookId 가계부 아이디
     * @param memberId 삭제하려는 멤버 아이디
     */
    @Transactional
    public void deleteAccountBookMember(Long accountBookId, Long memberId) {
        accountBookMemberRepository.deleteAccountBookMemberByAccountBookIdAndMemberId(accountBookId, memberId);
    }
}
