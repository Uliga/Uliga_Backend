package com.uliga.uliga_backend.domain.AccountBook.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.repository.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.exception.InvalidAccountBookDeleteRequest;
import com.uliga.uliga_backend.domain.AccountBook.exception.InvitationSaveError;
import com.uliga.uliga_backend.domain.AccountBook.exception.InvitationSaveErrorWithCreation;
import com.uliga.uliga_backend.domain.AccountBook.exception.UnauthorizedAccountBookAccessException;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBookAuthority;
import com.uliga.uliga_backend.domain.Category.repository.CategoryRepository;
import com.uliga.uliga_backend.domain.JoinTable.repository.AccountBookMemberRepository;
import com.uliga.uliga_backend.domain.JoinTable.model.AccountBookMember;
import com.uliga.uliga_backend.domain.Member.repository.MemberRepository;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.InvitationInfo;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountBookService {

    private final AccountBookRepository accountBookRepository;
    private final AccountBookMemberRepository accountBookMemberRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;


    /**
     * 가계부 정보 조회
     *
     * @param id       가계부 아이디
     * @param memberId 멤버 아이디
     * @return 가계부 정보
     */
    @Transactional(readOnly = true)
    public AccountBookInfo getSingleAccountBookInfo(Long id, Long memberId) {
        AccountBookInfoQ bookInfoById = accountBookRepository.findAccountBookInfoById(id, memberId);
        if (bookInfoById == null) {
            throw new UnauthorizedAccountBookAccessException();
        }
        return AccountBookInfo.builder()
                .info(bookInfoById)
                .numberOfMember(accountBookRepository.getMemberNumberByAccountBookId(id))
                .members(accountBookRepository.findAccountBookMemberInfoById(id))
                .categories(categoryRepository.findAccountBookCategoryInfoById(id)).build();
    }

    /**
     * 멤버 가계부 조회
     *
     * @param id 멤버 아이디
     * @return 멤버 가계부 정보 리스트
     */
    @Transactional(readOnly = true)
    public GetAccountBookInfos getMemberAccountBook(Long id) {
        List<AccountBookInfo> result = new ArrayList<>();
        List<AccountBookInfoQ> accountBookInfosByMemberId = accountBookRepository.findAccountBookInfosByMemberId(id);
        for (AccountBookInfoQ accountBookInfoQ : accountBookInfosByMemberId) {
            AccountBookInfo build = AccountBookInfo.builder().info(accountBookInfoQ)
                    .members(accountBookRepository.findAccountBookMemberInfoById(accountBookInfoQ.getAccountBookId()))
                    .numberOfMember(accountBookRepository.getMemberNumberByAccountBookId(accountBookInfoQ.getAccountBookId()))
                    .categories(categoryRepository.findAccountBookCategoryInfoById(accountBookInfoQ.getAccountBookId())).build();
            result.add(build);
        }

        return new GetAccountBookInfos(result);
    }


    /**
     * 개인 가계부 생성
     *
     * @param member        멤버
     * @param createRequest 개인 가계부 생성 요청
     * @return 생성된 가계부
     */
    @Transactional
    public AccountBook createAccountBookPrivate(Member member, CreateRequestPrivate createRequest) {
        AccountBook accountBook = createRequest.toEntity();
        accountBookRepository.save(accountBook);
        member.setPrivateAccountBook(accountBook);
        AccountBookMember bookMember = AccountBookMember.builder()
                .accountBook(accountBook)
                .member(member)
                .accountBookAuthority(AccountBookAuthority.ADMIN)
                .avatarUrl("default")
                .getNotification(true).build();

        accountBookMemberRepository.save(bookMember);
        return accountBook;
    }

    /**
     * 첫 소셜 로그인시 가계부 생성
     *
     * @param memberId      멤버 아이디
     * @param createRequest 가계부 생성 요청
     * @return 생성된 가계류
     */
    @Transactional
    public AccountBook createAccountBookPrivateSocialLogin(Long memberId, CreateRequestPrivate createRequest) {
        AccountBook accountBook = createRequest.toEntity();
        accountBookRepository.save(accountBook);
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        member.setPrivateAccountBook(accountBook);
        AccountBookMember bookMember = AccountBookMember.builder()
                .accountBook(accountBook)
                .member(member)
                .accountBookAuthority(AccountBookAuthority.ADMIN)
                .avatarUrl("default")
                .getNotification(true).build();

        accountBookMemberRepository.save(bookMember);
        return accountBook;
    }

    /**
     * 가계부 생성 요청
     *
     * @param id                       멤버 아이디
     * @param accountBookCreateRequest 가계부 생성 요청
     * @return 생성한 가계부 정보
     * @throws JsonProcessingException Json 처리 예외
     */
    @Transactional
    public AccountBook createAccountBook(Long id, AccountBookCreateRequest accountBookCreateRequest) throws JsonProcessingException {

        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));
        AccountBook accountBook = accountBookCreateRequest.toEntity();
        accountBookRepository.save(accountBook);
        AccountBookMember bookMember = AccountBookMember.builder()
                .accountBook(accountBook)
                .member(member)
                .accountBookAuthority(AccountBookAuthority.ADMIN)
                .avatarUrl("default")
                .getNotification(true).build();
        accountBookMemberRepository.save(bookMember);


        for (String email : accountBookCreateRequest.getEmails()) {
            InvitationInfo info = new InvitationInfo(accountBook.getId(), member.getUserName(), accountBook.getName());
            SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
            try {

                setOperations.add(email, objectMapper.writeValueAsString(info));
            } catch (RedisSystemException e) {
                throw new InvitationSaveErrorWithCreation(accountBook.toInfoDto());
            }
        }
        return accountBook;
    }

    /**
     * 멤버 초대
     *
     * @param id          초대자 ID
     * @param invitations 초대하려는 사람들
     * @return 초대된 사람들 수
     * @throws JsonProcessingException Json 처리 예외
     */
    @Transactional
    public Invited createInvitation(Long id, GetInvitations invitations) throws JsonProcessingException {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));
        AccountBook accountBook = accountBookRepository.findById(invitations.getId()).orElseThrow(() -> new NotFoundByIdException("가계부 초대에 아이디 값이 없습니다"));
        long result = 0L;
        for (String email : invitations.getEmails()) {
            InvitationInfo info = new InvitationInfo(accountBook.getId(), member.getUserName(), accountBook.getName());
            SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
            try {

                setOperations.add(email, objectMapper.writeValueAsString(info));
            } catch (RedisSystemException e) {
                throw new InvitationSaveError();
            }
            result += 1;
        }
        return new Invited(result);
    }

    /**
     * 초대에 대한 응답
     *
     * @param id              응답자 아이디
     * @param invitationReply 초대에 대한 응답
     * @return 응답 결과
     * @throws JsonProcessingException Json 처리 예외
     */
    @Transactional
    public InvitationReplyResult invitationReply(Long id, InvitationReply invitationReply) throws JsonProcessingException {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));
        log.info(invitationReply.toString());
        if (invitationReply.getJoin()) {
            if (!accountBookMemberRepository.existsAccountBookMemberByMemberIdAndAccountBookId(member.getId(), invitationReply.getId())) {
                AccountBook accountBook = accountBookRepository.findById(invitationReply.getId()).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 가계부가 없습니다"));
                AccountBookMember bookMember = AccountBookMember.builder()
                        .accountBook(accountBook)
                        .member(member)
                        .accountBookAuthority(AccountBookAuthority.USER)
                        .avatarUrl("default")
                        .getNotification(true).build();
                accountBookMemberRepository.save(bookMember);
            }
        }
        InvitationInfo build = InvitationInfo.builder()
                .accountBookName(invitationReply.getAccountBookName())
                .memberName(invitationReply.getMemberName())
                .id(invitationReply.getId())
                .build();
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        setOperations.remove(member.getEmail(), objectMapper.writeValueAsString(build));
        return new InvitationReplyResult(invitationReply.getId(), invitationReply.getJoin());

    }


    /**
     * 가계부 멤버 조회
     *
     * @param id 가계부 아이디
     * @return 가계부 멤버 정보
     */
    @Transactional(readOnly = true)
    public AccountBookMembers getAccountBookMembers(Long id) {
        return new AccountBookMembers(accountBookRepository.findAccountBookMemberInfoById(id));
    }

    /**
     * 가계부 삭제
     *
     * @param id       가계부 아이디
     * @param memberId 멤버 아이디
     * @return DELETED
     */
    @Transactional
    public String deleteAccountBook(Long id, Long memberId) {
        AccountBook accountBook = accountBookRepository.findById(id).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 가계부가 없습니다"));
        if (accountBookMemberRepository.existsAccountBookMemberByMemberIdAndAccountBookId(memberId, id)) {
            accountBookRepository.delete(accountBook);
        } else {
            throw new InvalidAccountBookDeleteRequest();
        }
        return "DELETED";
    }


    /**
     * 가계부 정보 업데이트 요청
     *
     * @param memberId      멤버 아이디
     * @param accountBookId 가계부 아이디
     * @param request           업데이트 요청
     * @return 업데이트 결과
     * @throws JsonProcessingException Json 처리 예외
     */
    @Transactional
    public AccountBookUpdateRequest updateAccountBookInfo(Long memberId, Long accountBookId, AccountBookUpdateRequest request) throws JsonProcessingException {
        AccountBook accountBook = accountBookRepository.findById(accountBookId).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 가계부가 없습니다"));
        AccountBookMember accountBookMember = accountBookMemberRepository.findAccountBookMemberByMemberIdAndAccountBookId(memberId, accountBookId).orElseThrow(UnauthorizedAccountBookAccessException::new);
        if (request.getAvatarUrl() != null) {
            accountBookMember.setAvatarUrl(request.getAvatarUrl());
        }

        if (request.getMembers() != null) {
            createInvitation(memberId, GetInvitations.builder().id(accountBookId).emails(request.getMembers()).build());
        }

        if (request.getName() != null) {
            accountBook.setName(request.getName());
        }

        if (request.getRelationship() != null) {
            accountBook.setRelationShip(request.getRelationship());
        }
        return request;
    }

}
