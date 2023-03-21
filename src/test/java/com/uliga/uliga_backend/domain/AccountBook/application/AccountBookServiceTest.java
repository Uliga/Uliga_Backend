package com.uliga.uliga_backend.domain.AccountBook.application;

import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.exception.UnauthorizedAccountBookAccessException;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Income.dao.IncomeRepository;
import com.uliga.uliga_backend.domain.JoinTable.dao.AccountBookMemberRepository;
import com.uliga.uliga_backend.domain.JoinTable.dao.ScheduleMemberRepository;
import com.uliga.uliga_backend.domain.Member.application.AuthService;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.SignUpRequest;
import com.uliga.uliga_backend.domain.Record.dao.RecordRepository;
import com.uliga.uliga_backend.domain.Schedule.dao.ScheduleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
   @Test
   @DisplayName("가계부 생성 성공 테스트")
   public void createAccountBookTestToSuccess() throws Exception{
       //given
       SignUpRequest nickname = createSignUpRequest("email@email.com", "nickname");
       Long memberId = authService.signUp(nickname);
       AccountBookCreateRequest newAccountBook = createAccountBookCreateRequest("newAccountBook");


       // when
       SimpleAccountBookInfo accountBook = accountBookService.createAccountBook(memberId, newAccountBook);

       // then
       assertTrue(accountBookMemberRepository.existsAccountBookMemberByMemberIdAndAccountBookId(memberId, accountBook.getId()));

   }

   @Test
   @DisplayName("가계부 정보 조회 성공 테스트")
   public void getAccountBookInfoTestToSuccess() throws Exception{
       //given
       SignUpRequest nickname = createSignUpRequest("email@email.com", "nickname");
       Long memberId = authService.signUp(nickname);
       AccountBookCreateRequest newAccountBook = createAccountBookCreateRequest("newAccountBook");

       // when
       SimpleAccountBookInfo accountBook = accountBookService.createAccountBook(memberId, newAccountBook);
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
       SignUpRequest nickname = createSignUpRequest("email@email.com", "nickname");
       Long memberId = authService.signUp(nickname);
       AccountBookCreateRequest newAccountBook = createAccountBookCreateRequest("newAccountBook");
       SignUpRequest signUpRequest = createSignUpRequest("newuser@email.com", "newNickname");
       Long signUp = authService.signUp(signUpRequest);


       // when
       SimpleAccountBookInfo accountBook = accountBookService.createAccountBook(memberId, newAccountBook);
       // then
       assertThrows(UnauthorizedAccountBookAccessException.class, () -> accountBookService.getSingleAccountBookInfo(accountBook.getId(), signUp));
   }


}