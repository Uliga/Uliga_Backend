package com.uliga.uliga_backend.domain.Income.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.exception.CategoryNotFoundException;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Income.dao.IncomeRepository;
import com.uliga.uliga_backend.domain.Income.dto.IncomeDTO;
import com.uliga.uliga_backend.domain.Income.dto.IncomeDTO.IncomeDeleteRequest;
import com.uliga.uliga_backend.domain.Income.dto.IncomeDTO.IncomeUpdateRequest;
import com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ;
import com.uliga.uliga_backend.domain.Income.exception.InvalidIncomeDeleteRequest;
import com.uliga.uliga_backend.domain.Income.model.Income;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.global.error.exception.IdNotFoundException;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void addIncomeToOtherAccountBooks(List<Income> incomeList) {
        incomeRepository.saveAll(incomeList);
    }

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
    public IncomeUpdateRequest updateIncome(Map<String, Object> updates) {
        IncomeUpdateRequest patchIncome = objectMapper.convertValue(updates, IncomeUpdateRequest.class);
        if (patchIncome.getId() == null) {
            throw new IdNotFoundException();
        }
        Income income = incomeRepository.findById(patchIncome.getId()).orElseThrow(NotFoundByIdException::new);
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
        // 날짜는 프론트에서 값 받고 변경해야할듯?
        return patchIncome;
    }

    @Transactional
    public Page<IncomeInfoQ> getMemberIncomesByAccountBook(Long id, Long accountBookId, Long year, Long month, Pageable pageable) {
        if (year == null && month == null) {

            return incomeRepository.getMemberIncomesByAccountBook(id, accountBookId, pageable);
        } else if (year != null && month == null) {
            return incomeRepository.getMemberIncomesByAccountBookAndYear(id, accountBookId, year, pageable);
        } else {
            return incomeRepository.getMemberIncomesByAccountBookAndYearAndMonth(id, accountBookId, year, month, pageable);
        }
    }

    @Transactional
    public Page<IncomeInfoQ> getMemberIncomes(Long id, Pageable pageable) {
        return incomeRepository.getMemberIncomes(id, pageable);
    }

    @Transactional
    public Page<IncomeInfoQ> getMemberIncomesByCategory(Long id, Long accountBookId, String category, Pageable pageable) {
        return incomeRepository.getMemberIncomesByCategory(id, accountBookId, category, pageable);
    }

    @Transactional
    public void deleteIncome(Long id, Long incomeId) {
        Income income = incomeRepository.findById(incomeId).orElseThrow(NotFoundByIdException::new);
        if (income.getCreator().getId().equals(id)) {
            incomeRepository.delete(income);
        } else {
            throw new InvalidIncomeDeleteRequest();
        }
    }
}
