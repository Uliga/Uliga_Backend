package com.uliga.uliga_backend.domain.Income.application;

import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.CreateItemResult;
import com.uliga.uliga_backend.domain.AccountBook.exception.CategoryNotFoundException;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Income.dao.IncomeRepository;
import com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ;
import com.uliga.uliga_backend.domain.Income.model.Income;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public CreateItemResult addItemToAccountBook(CreateRecordOrIncomeDto dto, AccountBook accountBook, Member member, Date date, Category category) {
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
        return CreateItemResult.builder()
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

    @Transactional
    public void addItemToSharedAccountBook(CreateRecordOrIncomeDto dto, AccountBook accountBook, Member member, Date date, Category category) {
        Income sharedIncome = Income.builder()
                .payment(dto.getPayment())
                .account(dto.getAccount())
                .creator(member)
                .accountBook(accountBook)
                .value(dto.getValue())
                .memo(dto.getMemo())
                .date(date)
                .category(category).build();
        incomeRepository.save(sharedIncome);
    }

    @Transactional
    public UpdateCategoryResult updateIncomeCategory(UpdateIncomeCategory incomeCategory) {
        Income income = incomeRepository.findById(incomeCategory.getIncomeId()).orElseThrow(NotFoundByIdException::new);
        Category category = categoryRepository.findByAccountBookAndName(income.getAccountBook(), incomeCategory.getCategory()).orElseThrow(CategoryNotFoundException::new);
        String updateCategory = income.updateCategory(category);

        return UpdateCategoryResult.builder()
                .category(updateCategory)
                .updateItemId(income.getId())
                .build();
    }

    @Transactional
    public AddIncomeResult addSingleIncomeToAccountBook(AddIncomeRequest request, Category category, Date date, AccountBook accountBook, Member member) {
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
        return AddIncomeResult.builder()
                .accountBookId(accountBook.getId())
                .incomeInfo(income.toInfoQ()).build();
    }
    @Transactional
    public IncomeInfoQ updateIncome(Long id, Map<String, Object> updates) {
        return null;
    }
}
