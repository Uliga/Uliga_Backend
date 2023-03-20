package com.uliga.uliga_backend.domain.AccountBook.application;

import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Income.dao.IncomeRepository;
import com.uliga.uliga_backend.domain.JoinTable.dao.AccountBookMemberRepository;
import com.uliga.uliga_backend.domain.JoinTable.dao.ScheduleMemberRepository;
import com.uliga.uliga_backend.domain.Record.dao.RecordRepository;
import com.uliga.uliga_backend.domain.Schedule.dao.ScheduleRepository;
import jakarta.transaction.Transactional;
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


}