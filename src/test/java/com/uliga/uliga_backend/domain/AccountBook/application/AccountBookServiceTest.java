package com.uliga.uliga_backend.domain.AccountBook.application;

import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Income.dao.IncomeRepository;
import com.uliga.uliga_backend.domain.JoinTable.dao.AccountBookMemberRepository;
import com.uliga.uliga_backend.domain.JoinTable.dao.ScheduleMemberRepository;
import com.uliga.uliga_backend.domain.Record.dao.RecordRepository;
import com.uliga.uliga_backend.domain.Schedule.dao.ScheduleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class AccountBookServiceTest {
    @Autowired
    AccountBookService accountBookService;
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

    @Test
    @DisplayName("가계부 정보 조회 성공 테스트")
    public void getSingleAccountBookInfoTestToSuccess() throws Exception{
        //given


        // when

        // then
    }
}