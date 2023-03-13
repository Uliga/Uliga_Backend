package com.uliga.uliga_backend.domain.Record.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.AddRecordResult;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.UpdateCategoryResult;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.UpdateRecordCategory;
import com.uliga.uliga_backend.domain.AccountBook.exception.CategoryNotFoundException;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Record.dao.RecordRepository;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ;
import com.uliga.uliga_backend.domain.Record.dto.RecordDTO;
import com.uliga.uliga_backend.domain.Record.dto.RecordDTO.RecordUpdateRequest;
import com.uliga.uliga_backend.global.error.exception.IdNotFoundException;
import com.uliga.uliga_backend.domain.Record.model.Record;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;
    @Transactional
    public CreateItemResult addItemToAccountBook(CreateRecordOrIncomeDto dto, AccountBook accountBook, Member member, Date date, Category category) {
        Record build = Record.builder()
                .payment(dto.getPayment())
                .account(dto.getAccount())
                .creator(member)
                .accountBook(accountBook)
                .spend(dto.getValue())
                .memo(dto.getMemo())
                .date(date)
                .category(category).build();
        recordRepository.save(build);
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
         Record sharedRecord = Record.builder()
                .payment(dto.getPayment())
                .account(dto.getAccount())
                .creator(member)
                .accountBook(accountBook)
                .spend(dto.getValue())
                .memo(dto.getMemo())
                .date(date)
                .category(category).build();
        recordRepository.save(sharedRecord);
    }

    @Transactional
    public UpdateCategoryResult updateRecordCategory(UpdateRecordCategory recordCategory) {
        Record record = recordRepository.findById(recordCategory.getRecordId()).orElseThrow(NotFoundByIdException::new);
        Category category = categoryRepository.findByAccountBookAndName(record.getAccountBook(), recordCategory.getCategory()).orElseThrow(CategoryNotFoundException::new);
        String updateCategory = record.updateCategory(category);

        return UpdateCategoryResult.builder()
                .updateItemId(record.getId())
                .category(updateCategory)
                .build();
    }

    @Transactional
    public AddRecordResult addSingleItemToAccountBook(AddRecordRequest request, Category category, Date date, AccountBook accountBook, Member member) {
        Record record = Record.builder()
                .accountBook(accountBook)
                .spend(request.getValue())
                .payment(request.getPayment())
                .category(category)
                .creator(member)
                .memo(request.getMemo())
                .date(date)
                .account(request.getAccount())
                .build();
        recordRepository.save(record);
        return AddRecordResult.builder()
                .accountBookId(accountBook.getId())
                .recordInfo(record.toInfoQ()).build();
    }

    @Transactional
    public RecordUpdateRequest updateRecord(Map<String, Object> updates) {
        RecordUpdateRequest patchRecord = objectMapper.convertValue(updates, RecordUpdateRequest.class);
        if (patchRecord.getId() == null) {
            throw new IdNotFoundException();
        }
        Record record = recordRepository.findById(patchRecord.getId()).orElseThrow(NotFoundByIdException::new);

        if (patchRecord.getAccount() != null) {
            record.updateAccount(patchRecord.getAccount());
        }
        if (patchRecord.getCategory() != null) {
            Category category = categoryRepository.findByAccountBookAndName(record.getAccountBook(), patchRecord.getCategory()).orElseThrow(CategoryNotFoundException::new);
            record.updateCategory(category);
        }
        if (patchRecord.getMemo() != null) {
            record.updateMemo(patchRecord.getMemo());
        }
        if (patchRecord.getValue() != null) {
            record.updateSpend(patchRecord.getValue());
        }
        if (patchRecord.getPayment() != null) {
            record.updatePayment(patchRecord.getPayment());
        }
        if (patchRecord.getDate() != null) {
            record.updateDate(patchRecord.getDate());
        }
        // 날짜는 프론트에서 값 받고 변경해야할듯?

        return patchRecord;
    }

    @Transactional
    public Page<RecordInfoQ> getMemberRecords(Long id, Long accountBookId, Pageable pageable) {
        return recordRepository.getMemberRecords(id, accountBookId, pageable);
    }
}
