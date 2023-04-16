package com.uliga.uliga_backend.domain.Record.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.exception.CategoryNotFoundException;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Income.model.Income;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Record.dao.RecordMapper;
import com.uliga.uliga_backend.domain.Record.dao.RecordRepository;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ;
import com.uliga.uliga_backend.domain.Record.dto.RecordDTO;
import com.uliga.uliga_backend.domain.Record.dto.RecordDTO.RecordDeleteRequest;
import com.uliga.uliga_backend.domain.Record.dto.RecordDTO.RecordInfoDetail;
import com.uliga.uliga_backend.domain.Record.dto.RecordDTO.RecordUpdateRequest;
import com.uliga.uliga_backend.domain.Record.exception.InvalidRecordDelete;
import com.uliga.uliga_backend.domain.Record.model.Record;
import com.uliga.uliga_backend.domain.RecordComment.dto.NativeQ.RecordCommentInfoQ;
import com.uliga.uliga_backend.domain.RecordComment.dto.RecordCommentDto.RecordCommentCreateDto;
import com.uliga.uliga_backend.global.error.exception.IdNotFoundException;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import jakarta.transaction.Transactional;
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

import static com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final AccountBookRepository accountBookRepository;
    private final RecordMapper mapper;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void addRecordToOtherAccountBooks(List<Record> records) {
        recordRepository.saveAll(records);
    }
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
        List<Long> sharedAccountBookIds = request.getSharedAccountBook();
        List<AccountBook> sharedAccountBooks = accountBookRepository.findAccountBookByAccountBookIds(sharedAccountBookIds);
        List<Category> categories = categoryRepository.findCategoriesByAccountBookIds(sharedAccountBookIds);

        Map<Long, Category> categoryDict = new HashMap<>();
        Map<Long, AccountBook> accountBookDict = new HashMap<>();
        List<Record> toSave = new ArrayList<>();
        for (Category c : categories) {
            categoryDict.put(c.getAccountBook().getId(), c);
        }
        for (AccountBook ab : sharedAccountBooks) {
            accountBookDict.put(ab.getId(), ab);
        }
        for (Long accountBookId : sharedAccountBookIds) {
            Record temp_record = Record.builder()
                    .category(categoryDict.get(accountBookId))
                    .date(date)
                    .accountBook(accountBookDict.get(accountBookId))
                    .memo(request.getMemo())
                    .payment(request.getPayment())
                    .creator(member)
                    .spend(request.getValue())
                    .account(request.getAccount())
                    .build();
            toSave.add(temp_record);
        }
        recordRepository.saveAll(toSave);
        return AddRecordResult.builder()
                .accountBookId(accountBook.getId())
                .recordInfo(record.toInfoQ()).build();
    }

    @Transactional
    public RecordUpdateRequest updateRecord(Map<String, Object> updates) {
        RecordUpdateRequest patchRecord = objectMapper.convertValue(updates, RecordUpdateRequest.class);
        if (patchRecord.getId() == null) {
            throw new IdNotFoundException("지출 아이디가 null 입니다.");
        }
        Record record = recordRepository.findById(patchRecord.getId()).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 지출이 없습니다"));

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
    public Page<RecordInfoQ> getMemberRecordsByAccountBook(Long accountBookId, Long categoryId, Long year, Long month, Pageable pageable) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("accountBookId", accountBookId);
        map.put("categoryId", categoryId);
        map.put("year", year);
        map.put("month", month);
        map.put("offset", pageable.getOffset());
        map.put("pageSize", pageable.getPageSize());;
        List<RecordInfoQ> accountBookMemberRecords = mapper.findAccountBookMemberRecords(map);
        List<Long> counted = mapper.countQueryForRecordHistory(map);
        return new PageImpl<>(accountBookMemberRecords, pageable, counted.size());
    }

    @Transactional
    public Page<RecordInfoQ> getMemberRecords(Long id, Pageable pageable) {

        return recordRepository.getMemberRecords(id, pageable);
    }

    @Transactional
    public void deleteRecord(Long id, Long recordId) {
        Record record = recordRepository.findById(recordId).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 지출이 없습니다"));
        if (record.getCreator().getId().equals(id)) {
            recordRepository.delete(record);
        } else {
            throw new InvalidRecordDelete();
        }

    }


    @Transactional
    public RecordCommentInfoQ addCommentToRecord(Long id, RecordCommentCreateDto createDto) {
        return null;
    }

    @Transactional
    public RecordInfoDetail getRecordInfoDetail(Long id) {
        return null;
    }
}
