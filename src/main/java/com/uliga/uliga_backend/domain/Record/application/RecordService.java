package com.uliga.uliga_backend.domain.Record.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.exception.CategoryNotFoundException;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.AccountBookData.model.AccountBookDataType;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Income.dao.IncomeMapper;
import com.uliga.uliga_backend.domain.Income.dao.IncomeRepository;
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
    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    /**
     * 지출 리스트 다른 가계부에 추가하는 메서드
     * @param records 지출 리스트
     */
    @Transactional
    public void addRecordToOtherAccountBooks(List<Record> records) {
        recordRepository.saveAll(records);
    }

    /**
     * 수입/지출 한개 가계부에 추가
     * @param dto 지출 dto
     * @param accountBook 추가할 가계부
     * @param member 생성한 멤버
     * @param date 지출 날짜
     * @param category 지출 카테고리
     * @return 추가 결과
     */
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

    /**
     * 지출 한개 가계부에 추가
     * @param request 지출 dto
     * @param category 지출 카테고리
     * @param date 지출 날짜
     * @param accountBook 추가할 가계부
     * @param member 생성한 멤버
     * @return 지출 추가 결과
     */
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

    /**
     * 지출 정보 업데이트
     * @param updates 업데이트 정보 map
     * @return 업데이트 결과
     */
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
        if (patchRecord.getType() != null && patchRecord.getType().equals(AccountBookDataType.INCOME.toString())) {
            record.updateType();
            incomeRepository.createFromAccountBookDataId(record.getId());
            recordRepository.deleteByAccountBookDataIdNativeQ(record.getId());

        }
        return patchRecord;
    }

    /**
     * 가계부 지출 페이징으로 조회
     * @param accountBookId 가계부 아이디
     * @param categoryId 카테고리 아이디
     * @param year 년도
     * @param month 달
     * @param pageable 페이징 정보
     * @return 조회 결과
     */
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

    /**
     * 멤버 전체 지출 조회
     * @param id 멤버 아이디
     * @param pageable 페이징 정보
     * @return 조회 결과
     */
    @Transactional
    public Page<RecordInfoQ> getMemberRecords(Long id, Pageable pageable) {

        return recordRepository.getMemberRecords(id, pageable);
    }

    /**
     * 지출 삭제
     * @param id 멤버 아이디
     * @param recordId 지출 아이디
     */
    @Transactional
    public void deleteRecord(Long id, Long recordId) {
        Record record = recordRepository.findById(recordId).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 지출이 없습니다"));
        if (record.getCreator().getId().equals(id)) {
            recordRepository.delete(record);
        } else {
            throw new InvalidRecordDelete();
        }

    }

    /**
     * 지출에 댓글 추가
     * @param id 지출 아이디
     * @param createDto 댓글 정보
     * @return 댓글 추가 결과
     */
    @Transactional
    public RecordCommentInfoQ addCommentToRecord(Long id, RecordCommentCreateDto createDto) {
        return null;
    }

    /**
     * 지출 상세 정보 조회
     * @param id 지출 아이디
     * @return 지출 상세 정보
     */
    @Transactional
    public RecordInfoDetail getRecordInfoDetail(Long id) {
        return null;
    }
}
