package com.uliga.uliga_backend.domain.Income.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.exception.CategoryNotFoundException;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.AccountBookData.dto.AccountBookDataDTO;
import com.uliga.uliga_backend.domain.AccountBookData.model.AccountBookDataType;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Income.dao.IncomeMapper;
import com.uliga.uliga_backend.domain.Income.dao.IncomeRepository;
import com.uliga.uliga_backend.domain.Income.dto.IncomeDTO.IncomeUpdateRequest;
import com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ;
import com.uliga.uliga_backend.domain.Income.exception.InvalidIncomeDeleteRequest;
import com.uliga.uliga_backend.domain.Income.model.Income;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Record.dao.RecordRepository;
import com.uliga.uliga_backend.global.error.exception.IdNotFoundException;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncomeService {
    private final AccountBookRepository accountBookRepository;
    private final IncomeRepository incomeRepository;
    private final IncomeMapper incomeMapper;
    private final RecordRepository recordRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    /**
     * 다른 가계부에 수입 추가할때 쓰이는 메서드
     * @param incomeList 수입 리스트
     */
    @Transactional
    public void addIncomeToOtherAccountBooks(List<Income> incomeList) {
        incomeRepository.saveAll(incomeList);
    }

    /**
     * 다른 가계부에 추가할 수입 생성하는 메서드
     * @param dto 수입 정보 DTO
     * @param accountBook 추가할 가계부
     * @param member 생성한 멤버
     * @param date 수입의 날짜
     * @param category 수입의 카테고리
     * @return 생성된 수입
     */
    @Transactional
    public AccountBookDataDTO.CreateItemResult addItemToAccountBook(AccountBookDataDTO.CreateRecordOrIncomeDto dto, AccountBook accountBook, Member member, Date date, Category category) {
        Income build = Income.builder()
                .payment(dto.getPayment())
                .account(dto.getAccount())
                .creator(member)
                .accountBook(accountBook)
                .value(dto.getValue())
                .memo(dto.getMemo())
                .date(date)
                .category(category).build();
        incomeRepository.save(build);
        return AccountBookDataDTO.CreateItemResult.builder()
                .id(build.getId())
                .account(dto.getAccount())
                .isIncome(true)
                .category(category.getName())
                .memo(dto.getMemo())
                .payment(dto.getPayment())
                .value(dto.getValue())
                .year(date.getYear())
                .month(date.getMonth())
                .day(date.getDay())
                .build();
    }


    /**
     * 가계부에 수입 한개 추가
     * @param request 수입 생성 요청
     * @param category 수입 카테고리
     * @param date 수입의 날짜
     * @param accountBook 추가할 가계부
     * @param member 수입 생성하려는 멤버
     * @return 수입 추가 결과
     */
    @Transactional
    public AccountBookDataDTO.AddIncomeResult addSingleIncomeToAccountBook(AccountBookDataDTO.AddIncomeRequest request, Category category, Date date, AccountBook accountBook, Member member) {
        Income income = Income.builder()
                .category(category)
                .date(date)
                .accountBook(accountBook)
                .memo(request.getMemo())
                .payment(request.getPayment())
                .creator(member)
                .value(request.getValue())
                .account(request.getAccount())
                .build();
        incomeRepository.save(income);
        List<Long> sharedAccountBookIds = request.getSharedAccountBook();
        List<AccountBook> sharedAccountBooks = accountBookRepository.findAccountBookByAccountBookIds(sharedAccountBookIds);
        List<Category> categories = categoryRepository.findCategoriesByAccountBookIds(sharedAccountBookIds);

        Map<Long, Category> categoryDict = new HashMap<>();
        Map<Long, AccountBook> accountBookDict = new HashMap<>();
        List<Income> toSave = new ArrayList<>();
        for (Category c : categories) {
            categoryDict.put(c.getAccountBook().getId(), c);
        }
        for (AccountBook ab : sharedAccountBooks) {
            accountBookDict.put(ab.getId(), ab);
        }
        for (Long accountBookId : sharedAccountBookIds) {
            Income temp_income = Income.builder()
                    .category(categoryDict.get(accountBookId))
                    .date(date)
                    .accountBook(accountBookDict.get(accountBookId))
                    .memo(request.getMemo())
                    .payment(request.getPayment())
                    .creator(member)
                    .value(request.getValue())
                    .account(request.getAccount())
                    .build();
            toSave.add(temp_income);
        }
        incomeRepository.saveAll(toSave);
        return AccountBookDataDTO.AddIncomeResult.builder()
                .accountBookId(accountBook.getId())
                .incomeInfo(income.toInfoQ()).build();
    }

    /**
     * 수입 정보 업데이트
     * @param updates 업데이트할 정보 map
     * @return 업데이트 결과
     */
    @Transactional
    public IncomeUpdateRequest updateIncome(Map<String, Object> updates) {
        IncomeUpdateRequest patchIncome = objectMapper.convertValue(updates, IncomeUpdateRequest.class);
        if (patchIncome.getId() == null) {
            throw new IdNotFoundException("수입 아이디가 null 입니다.");
        }
        Income income = incomeRepository.findById(patchIncome.getId()).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 수입이 없습니다"));
        if (patchIncome.getAccount() != null) {
            income.updateAccount(patchIncome.getAccount());
        }
        if (patchIncome.getCategory() != null) {
            Category category = categoryRepository.findByAccountBookAndName(income.getAccountBook(), patchIncome.getCategory()).orElseThrow(CategoryNotFoundException::new);
            income.updateCategory(category);
        }
        if (patchIncome.getMemo() != null) {
            income.updateMemo(patchIncome.getMemo());
        }
        if (patchIncome.getValue() != null) {
            income.updateValue(patchIncome.getValue());
        }
        if (patchIncome.getPayment() != null) {
            income.updatePayment(patchIncome.getPayment());
        }
        if (patchIncome.getDate() != null) {
            income.updateDate(patchIncome.getDate());
        }
        if (patchIncome.getType() != null && patchIncome.getType().equals(AccountBookDataType.RECORD.toString())) {
            income.updateType();
            recordRepository.createFromAccountBookDataId(income.getId());
            incomeRepository.deleteByAccountBookDataIdNativeQ(income.getId());
        }
        return patchIncome;
    }

    /**
     * 해당 가계부 멤버 수입 조회
     * @param accountBookId 가계부 아이디
     * @param categoryId 카테고리 아이디
     * @param year 조회할 년도
     * @param month 조회할 달
     * @param pageable 페이징 정보
     * @return 조회 결과
     */
    @Transactional(readOnly = true)
    public Page<IncomeInfoQ> getMemberIncomesByAccountBook(Long accountBookId, Long categoryId, Long year, Long month, Pageable pageable) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("accountBookId", accountBookId);
        map.put("categoryId", categoryId);
        map.put("year", year);
        map.put("month", month);
        map.put("offset", pageable.getOffset());
        map.put("pageSize", pageable.getPageSize());

        List<IncomeInfoQ> accountBookMemberIncomes = incomeMapper.findAccountBookMemberIncomes(map);
        List<Long> counted = incomeMapper.countQueryForIncomeHistory(map);
        return new PageImpl<>(accountBookMemberIncomes, pageable, counted.size());

    }

    /**
     * 멤버 수입 전체 조회
     * @param id 멤버 아이디
     * @param pageable 페이징 정보
     * @return 수입 조회 결과
     */
    @Transactional(readOnly = true)
    public Page<IncomeInfoQ> getMemberIncomes(Long id, Pageable pageable) {
        return incomeRepository.getMemberIncomes(id, pageable);
    }

    /**
     * 수입 삭제
     * @param id 멤버 아이디
     * @param incomeId 삭제할 수입 아이디
     */
    @Transactional
    public void deleteIncome(Long id, Long incomeId) {
        Income income = incomeRepository.findById(incomeId).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 수입이 없습니다"));
        if (income.getCreator().getId().equals(id)) {
            incomeRepository.delete(income);
        } else {
            throw new InvalidIncomeDeleteRequest();
        }
    }
}
