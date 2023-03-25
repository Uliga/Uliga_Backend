package com.uliga.uliga_backend.domain.AccountBook.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.exception.UnauthorizedAccountBookAccessException;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Income.dao.IncomeRepository;
import com.uliga.uliga_backend.domain.JoinTable.dao.AccountBookMemberRepository;
import com.uliga.uliga_backend.domain.JoinTable.dao.ScheduleMemberRepository;
import com.uliga.uliga_backend.domain.Member.application.AuthService;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.InvitationInfo;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.SignUpRequest;
import com.uliga.uliga_backend.domain.Record.dao.RecordRepository;
import com.uliga.uliga_backend.domain.Schedule.dao.ScheduleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class AccountBookServiceTest {
    @Autowired
    AccountBookService accountBookService;
    @Autowired
    AuthService authService;
    @Autowired
    AccountBookRepository accountBookRepository;
    @Autowired
    AccountBookMemberRepository accountBookMemberRepository;
    @Autowired
    IncomeRepository incomeRepository;
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    ScheduleMemberRepository scheduleMemberRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    ObjectMapper mapper;

    private Long memberId = 0L;
    private SimpleAccountBookInfo accountBook = null;
    
    

    @BeforeEach
    public void emptyRedis() {

        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        setOperations.pop("testuser@email.com", setOperations.size("testuser@email.com"));
    }
    
    @BeforeEach
    public void setMemberAndAccountBook() throws JsonProcessingException {
        SignUpRequest nickname = createSignUpRequest("email@email.com", "nickname");
        memberId = authService.signUp(nickname);
        AccountBookCreateRequest createRequest = createAccountBookCreateRequest("newAccountBook");
        accountBook = accountBookService.createAccountBook(memberId, createRequest);
    }
    private final List<String> defaultCategories = new ArrayList<>(
            Arrays.asList("\uD83C\uDF7D️ 식비",
                    "☕ 카페 · 간식",
                    "\uD83C\uDFE0 생활",
                    "\uD83C\uDF59 편의점,마트,잡화",
                    "\uD83D\uDC55 쇼핑",
                    "기타")
    );

    SignUpRequest createSignUpRequest(String email, String nickname) {
        return SignUpRequest.builder()
                .email(email).nickName(nickname)
                .password("12345678").applicationPassword("1234").build();
    }

    AccountBookCreateRequest createAccountBookCreateRequest(String name) {
        return AccountBookCreateRequest.builder()
                .name(name)
                .relationship("테스트용")
                .categories(defaultCategories).emails(new ArrayList<>()).build();
    }

    CreateRecordOrIncomeDto createRecordOrIncomeDto(boolean isIncome) {
        return CreateRecordOrIncomeDto.builder()
                .isIncome(isIncome)
                .account("account")
                .sharedAccountBook(new ArrayList<>())
                .payment("payment")
                .value(10000L)
                .memo("memo")
                .category("기타")
                .date("2023-04-25").build();
    }
   @Test
   @DisplayName("가계부 생성 성공 테스트")
   public void createAccountBookTestToSuccess() throws Exception{
       //given
       // BeforeEach에서 멤버 생성

       // when
       // BeforeEach에서 가계부 생성
       // then
       assertTrue(accountBookMemberRepository.existsAccountBookMemberByMemberIdAndAccountBookId(memberId, accountBook.getId()));

   }

   @Test
   @DisplayName("가계부 정보 조회 성공 테스트")
   public void getAccountBookInfoTestToSuccess() throws Exception{
       //given
       // BeforeEach에서 멤버 & 가계부 생성
       // then
       AccountBookInfo accountBookInfo = accountBookService.getSingleAccountBookInfo(accountBook.getId(), memberId);

       assertAll(
               () -> assertEquals(accountBookInfo.getInfo().getAccountBookId(), accountBook.getId()),
               () -> assertEquals(accountBookInfo.getInfo().getAccountBookName(), accountBook.getName())
       );
   }

   @Test
   @DisplayName("가계부 정보 조회 실패 테스트")
   public void getAccountBookInfoTestToFailByInvalidMember() throws Exception{
       //given
       // BeforeEach에서 멤버 & 가계부 생성
       AccountBookCreateRequest newAccountBook = createAccountBookCreateRequest("newAccountBook");
       SignUpRequest signUpRequest = createSignUpRequest("newuser@email.com", "newNickname");


       // when
       Long signUp = authService.signUp(signUpRequest);
       // then
       assertThrows(UnauthorizedAccountBookAccessException.class, () -> accountBookService.getSingleAccountBookInfo(accountBook.getId(), signUp));
   }

   @Test
   @DisplayName("멤버 가계부 조회 성공 테스트")
   public void getMemberAccountBookTestToSuccess() throws Exception{
       // given
       // BeforeEach에서 멤버 & 가계부 생성
       // when
       GetAccountBookInfos memberAccountBook = accountBookService.getMemberAccountBook(memberId);

       // then
       assertAll(
               () -> assertEquals(2, memberAccountBook.getAccountBooks().size()),
               () -> assertEquals(accountBook.getId(), memberAccountBook.getAccountBooks().get(1).getAccountBookId())
       );

   }

   @Test
   @DisplayName("개인 가계부 생성 성공 테스트")
   public void createAccountBookPrivateTestToSuccess() throws Exception{
       // given
       // BeforeEach에서 멤버 & 가계부 생성

       // when
       GetAccountBookInfos memberAccountBook = accountBookService.getMemberAccountBook(memberId);
       // then
       assertTrue(memberAccountBook.getAccountBooks().get(0).getIsPrivate());
   }

   @Test
   @DisplayName("초대 생성 성공 테스트")
   public void createInvitationTestToSuccess() throws Exception{
       // given
       // BeforeEach에서 멤버 & 가계부 생성
       SignUpRequest nickname = createSignUpRequest("email@email.com", "nickname");
       Long memberId = authService.signUp(nickname);
       AccountBookCreateRequest createRequest = createAccountBookCreateRequest("newAccountBook");
       SimpleAccountBookInfo accountBook = accountBookService.createAccountBook(memberId, createRequest);
       SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
       // when
       GetInvitations invitations = GetInvitations.builder().id(accountBook.getId()).emails(new ArrayList<>()).build();
       invitations.getEmails().add("testuser@email.com");

       accountBookService.createInvitation(memberId, invitations);
       // then
       Long size = setOperations.size("testuser@email.com");
       assertEquals(1L, size);


   }

   @Test
   @DisplayName("초대 응답 성공 테스트")
   public void invitationReplyTestToSuccess() throws Exception{
       // given
       // BeforeEach에서 멤버 & 가계부 생성

       SignUpRequest newNickname = createSignUpRequest("testuser@email.com", "newNickname");
       Long signUp = authService.signUp(newNickname);
       SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
       GetInvitations invitations = GetInvitations.builder().id(accountBook.getId()).emails(new ArrayList<>()).build();
       invitations.getEmails().add("testuser@email.com");

       accountBookService.createInvitation(memberId, invitations);
       // when
       InvitationReply invitationReply = InvitationReply.builder().join(true).id(accountBook.getId()).accountBookName(accountBook.getName()).build();
       accountBookService.invitationReply(signUp, invitationReply);

       // then
       assertEquals(0, setOperations.size("testuser@email.com"));
   }

   @Test
   @DisplayName("가계부 카테고리 조회 테스트")
   public void accountBookCategoryTestToSuccess() throws Exception{
       // given
       // BeforeEach에서 멤버 & 가계부 생성

       // when
       AccountBookCategories accountBookCategories = accountBookService.getAccountBookCategories(accountBook.getId());

       // then
       assertEquals(defaultCategories.size(), accountBookCategories.getCategories().size());
   }

   @Test
   @DisplayName("가계부 멤버 조회 테스트")
   public void accountBookMemberTestToSuccess() throws Exception{
       // given
       // BeforeEach에서 멤버 & 가계부 생성

       // when
       AccountBookMembers accountBookMembers = accountBookService.getAccountBookMembers(accountBook.getId());

       // then
       assertEquals(accountBookMembers.getMembers().size(), 1L);
    }

}