package com.uliga.uliga_backend.domain.AccountBook.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.application.AccountBookService;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.AccountBookData.application.AccountBookDataService;
import com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.AccountBookDataQ;
import com.uliga.uliga_backend.domain.Budget.application.BudgetService;
import com.uliga.uliga_backend.domain.Budget.dto.BudgetDTO;
import com.uliga.uliga_backend.domain.Budget.dto.BudgetDTO.CreateBudgetDto;
import com.uliga.uliga_backend.domain.Budget.dto.BudgetDTO.GetAccountBookAssets;
import com.uliga.uliga_backend.domain.Budget.dto.NativeQ.BudgetInfoQ;
import com.uliga.uliga_backend.domain.Category.application.CategoryService;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO.AccountBookCategories;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO.MonthlyRecordSumPerCategories;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO.CategoryCreateRequest;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO.CategoryCreateResult;
import com.uliga.uliga_backend.domain.Income.application.IncomeService;
import com.uliga.uliga_backend.domain.Record.application.RecordService;
import com.uliga.uliga_backend.domain.Schedule.application.ScheduleService;
import com.uliga.uliga_backend.domain.Schedule.dto.ScheduleDTO;
import com.uliga.uliga_backend.domain.Schedule.dto.ScheduleDTO.AddScheduleResult;
import com.uliga.uliga_backend.domain.Schedule.dto.ScheduleDTO.GetAccountBookSchedules;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import com.uliga.uliga_backend.global.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.*;
import static com.uliga.uliga_backend.domain.AccountBookData.dto.AccountBookDataDTO.*;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

@Tag(name = "가계부", description = "가계부 관련 API 입니다")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/accountBook")
public class AccountBookController {

    private final AccountBookService accountBookService;
    private final AccountBookDataService accountBookDataService;
    private final BudgetService budgetService;
    private final IncomeService incomeService;
    private final RecordService recordService;
    private final CategoryService categoryService;
    private final ScheduleService scheduleService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "멤버 가계부 조회 API", description = "멤버 가계부 조회 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "멤버 가계부 조회 성공시", content = @Content(schema = @Schema(implementation = GetAccountBookInfos.class))), @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping(value = "")
    public ResponseEntity<GetAccountBookInfos> getMemberAccountBook() {

        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.getMemberAccountBook(id));
    }

    @Operation(summary = "가계부 정보 조회 API", description = "가계부 정보 조회 API입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "정보 조회 성공시", content = @Content(schema = @Schema(implementation = AccountBookInfo.class))), @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountBookInfo> getSingleAccountBookInfo(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.getSingleAccountBookInfo(id, memberId));
    }

    @Operation(summary = "가계부 정보 업데이트 API", description = "가계부 정보 업데이트 API입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "업데이트 성공시", content = @Content(schema = @Schema(implementation = AccountBookUpdateRequest.class)))})
    @PatchMapping(value = "/{id}")
    public ResponseEntity<AccountBookUpdateRequest> updateAccountBookInfo(@PathVariable("id") Long id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "가계부 업데이트 요청", content = @Content(schema = @Schema(implementation = AccountBookUpdateRequest.class))) @RequestBody Map<String, Object> updateRequest) throws JsonProcessingException {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        AccountBookUpdateRequest updateDto = objectMapper.convertValue(updateRequest, AccountBookUpdateRequest.class);
        AccountBookUpdateRequest accountBookUpdateRequest = accountBookService.updateAccountBookInfo(currentMemberId, id, updateDto);
        if (updateDto.getCategories() != null) {
            categoryService.updateAccountBookCategory(id, updateDto.getCategories());
        }
        return ResponseEntity.ok(accountBookUpdateRequest);
    }

    @Operation(summary = "가계부 생성 API", description = "가계부 생성하는 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "가계부 생성시", content = @Content(schema = @Schema(implementation = SimpleAccountBookInfo.class))), @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping(value = "")
    public ResponseEntity<SimpleAccountBookInfo> createAccountBook(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "가계부 생성 요청") @RequestBody AccountBookCreateRequest accountBookCreateRequest) throws JsonProcessingException {

        Long id = SecurityUtil.getCurrentMemberId();
        AccountBook accountBook = accountBookService.createAccountBook(id, accountBookCreateRequest);
        categoryService.createCategoriesForNewAccountBook(accountBook, accountBookCreateRequest.getCategories());
        return ResponseEntity.ok(accountBook.toInfoDto());
    }


    @Operation(summary = "멤버 초대 API", description = "멤버 초대 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "초대 성공시", content = @Content(schema = @Schema(implementation = Invited.class))), @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping(value = "/invitation")
    public ResponseEntity<Invited> createInvitation(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "초대 생성 요청") @RequestBody GetInvitations invitations) throws JsonProcessingException {

        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.createInvitation(id, invitations));
    }

    @Operation(summary = "초대 응답 API", description = "초대 응답 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "초대 응답시", content = @Content(schema = @Schema(implementation = InvitationReply.class))), @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping(value = "/invitation/reply")
    public ResponseEntity<InvitationReplyResult> invitationReply(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "초대 응답 요청") @RequestBody InvitationReply invitationReply) throws JsonProcessingException {

        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.invitationReply(id, invitationReply));
    }

    @Operation(summary = "한달 가계부 수입/지출 조회 API", description = "한달동안의 가계부 수입/지출조회 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = AccountBookDataDailySum.class))), @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping(value = "/{id}/item/{year}/{month}")
    public ResponseEntity<AccountBookDataDailySum> getAccountBookItems(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id, @Parameter(name = "year", description = "년도", in = PATH) @PathVariable("year") Long year, @Parameter(name = "month", description = "달", in = PATH) @PathVariable("month") Long month) {
        return ResponseEntity.ok(accountBookDataService.getAccountBookItems(id, year, month));
    }


    @Operation(summary = "하루 수입/지출 내역 상세 조회", description = "하루 가계부 수입/지출 조회 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = DailyAccountBookDataDetails.class))), @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping(value = "/{id}/item/{year}/{month}/{day}")
    public ResponseEntity<DailyAccountBookDataDetails> getAccountBookItemDetailsByDay(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id, @Parameter(name = "year", description = "년도", in = PATH) @PathVariable("year") Long year, @Parameter(name = "month", description = "달", in = PATH) @PathVariable("month") Long month, @Parameter(name = "day", description = "하루", in = PATH) @PathVariable("day") Long day) {
        return ResponseEntity.ok(accountBookDataService.getDailyAccountBookDataDetails(id, year, month, day));
    }

    @Operation(summary = "가계부 내역 삭제", description = "가계부 내역 삭제 API 입니다")
    @DeleteMapping(value = "/item")
    public ResponseEntity<String> deleteAccountBookItems(@Valid @RequestBody DeleteItemRequest deleteItemRequest) {

        accountBookDataService.deleteAccountBookData(deleteItemRequest);
        return ResponseEntity.ok("DELETED");
    }

    @Operation(summary = "한달 가계부 지출/수입/예산 총합 조회 API", description = "한달 동안의 가계부 수입/지출/예산 총합 조회 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = GetAccountBookAssets.class))), @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping(value = "/{id}/asset/{year}/{month}")
    public ResponseEntity<GetAccountBookAssets> getAccountBookAssets(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id, @Parameter(name = "year", description = "년도", in = PATH) @PathVariable("year") Long year, @Parameter(name = "month", description = "달", in = PATH) @PathVariable("month") Long month) {

        GetAccountBookAssets accountBookAssets = GetAccountBookAssets.builder().budget(budgetService.getMonthlyBudgetSum(id, year, month)).record(recordService.getMonthlyRecordSum(id, year, month)).income(incomeService.getMonthlyIncomeSum(id, year, month)).build();
        return ResponseEntity.ok(accountBookAssets);
    }

    @Operation(summary = "수입/지출 추가 API", description = "수입/지출 한번에 추가 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = CreateResult.class))), @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping(value = "/item")
    public ResponseEntity<CreateResult> createItems(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수입/지출 생성 요청") @RequestBody CreateItems items) {

        Long id = SecurityUtil.getCurrentMemberId();

        return ResponseEntity.ok(accountBookDataService.createItems(id, items));
    }

    @Operation(summary = "가계부에 카테고리 추가 API", description = "가계부에 카테고리 추가하는 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = CategoryCreateResult.class))), @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping(value = "/category")
    public ResponseEntity<CategoryCreateResult> createCategories(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "카테고리 생성 요청") @RequestBody CategoryCreateRequest createRequest) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(categoryService.createCategories(currentMemberId, createRequest));

    }

    @Operation(summary = "가계부에 지출 추가 API", description = "가계부에 지출 추가하는 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = AddRecordResult.class))), @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping(value = "/record")
    public ResponseEntity<AddRecordResult> addRecord(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "지출 한개 생성 요청") @RequestBody AddRecordRequest request) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(recordService.addRecord(currentMemberId, request));
    }

    @Operation(summary = "가계부에 수입 추가 API", description = "가계부에 수입 추가하는 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = AddIncomeResult.class))), @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping(value = "/income")
    public ResponseEntity<AddIncomeResult> addIncome(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수입 한개 생성 요청") @RequestBody AddIncomeRequest request) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(incomeService.addIncome(currentMemberId, request));
    }

    @Operation(summary = "가계부 카테고리 조회 API", description = "가계부 카테고리 조회 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = AccountBookCategories.class))), @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping(value = "/{id}/category")
    public ResponseEntity<AccountBookCategories> getAccountBookCategory(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryService.getAccountBookCategories(id));
    }

    @Operation(summary = "가계부 멤버 조회 API", description = "가계부 멤버 조회 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = AccountBookMembers.class))), @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping(value = "/{id}/member")
    public ResponseEntity<AccountBookMembers> getAccountBookMembers(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id) {

        return ResponseEntity.ok(accountBookService.getAccountBookMembers(id));
    }

    @Operation(summary = "가계부 예산 추가", description = "가계부 예산 추가 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = BudgetInfoQ.class))), @ApiResponse(responseCode = "409", description = "해당 년도/달에 예산 이미 존재시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping(value = "/budget")
    public ResponseEntity<BudgetInfoQ> addBudget(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "예산 생성 요청", content = @Content(schema = @Schema(implementation = CreateBudgetDto.class))) @RequestBody Map<String, Object> createBudgetDto) {
        return ResponseEntity.ok(budgetService.addBudget(createBudgetDto));
    }

    @Operation(summary = "가계부에 금융 일정 추가", description = "가계부에 금융 일정 추가 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = AddScheduleResult.class)))})
    @PostMapping(value = "/schedule")
    public ResponseEntity<AddScheduleResult> addSchedule(@Valid @RequestBody ScheduleDTO.AddSchedules addSchedules) throws JsonProcessingException {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(scheduleService.addSchedule(memberId, addSchedules));
    }

    @Operation(summary = "가계부 금융 일정 세부 조회", description = "가계부 금융 일정 세부 조회 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = GetAccountBookSchedules.class)))})
    @GetMapping(value = "/{id}/schedule")
    public ResponseEntity<GetAccountBookSchedules> getAccountBookSchedules(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(scheduleService.getAccountBookSchedules(memberId, id));
    }

    @Operation(summary = "가계부 삭제 요청", description = "가계부 삭제 요청 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "삭제 성공시")})
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteAccountBook(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.deleteAccountBook(id, currentMemberId));
    }

    @Operation(summary = "가계부 내역 다수 삭제 요청", description = "가계부 내역 다수 삭제 요청 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "삭제 성공시")})
    @DeleteMapping(value = "/data")
    public ResponseEntity<String> deleteAccountBookData(@Valid @RequestBody AccountBookDataDeleteRequest dataDeleteRequest) {
        // TODO: 프론트랑 확인 후 중복된 API이니 제거 예정


        accountBookDataService.deleteAccountBookData(DeleteItemRequest.builder().deleteIds(dataDeleteRequest.getIds()).build());
        return ResponseEntity.ok("DELETED");
    }

    @Operation(summary = "가계부 전체 내역 전체/년도별/월별 조회", description = "가계부 전체 내역 조회 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = AccountBookDataQ.class)))})
    @GetMapping(value = "/{id}/history")
    public ResponseEntity<Page<AccountBookDataQ>> getAccountBookHistory(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id, @RequestParam(value = "categoryId", required = false) Long categoryId, @RequestParam(value = "year", required = false) Long year, @RequestParam(value = "month", required = false) Long month, Pageable pageable) {
        return ResponseEntity.ok(accountBookDataService.getAccountBookHistory(id, categoryId, year, month, pageable));
    }


    @Operation(summary = "가계부 분석용 - 날짜별 지출 조회 API", description = "가계부 분석용 날짜별 지출 조회 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = AccountBookDailyRecordSumAndMonthlySum.class)))})
    @GetMapping(value = "/{id}/analyze/{year}/{month}")
    public ResponseEntity<AccountBookDailyRecordSumAndMonthlySum> getAccountBookDailyValues(@PathVariable("id") Long id, @PathVariable("year") Long year, @PathVariable("month") Long month) {
        return ResponseEntity.ok(recordService.getDailyRecordSumAndMonthlySum(id, year, month));
    }

    @Operation(summary = "가계부 분석용 한달 카테고리 별 지출 조회 API", description = "가계부 분석용 한달 카테고리 별 지출 조회 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = MonthlyRecordSumPerCategories.class)))})
    @GetMapping(value = "/{id}/analyze/category/{year}/{month}")
    public ResponseEntity<MonthlyRecordSumPerCategories> getAccountBookCategoryAnalyze(@PathVariable("id") Long id, @PathVariable("year") Long year, @PathVariable("month") Long month) {
        return ResponseEntity.ok(recordService.getMonthlyRecordSumPerCategories(id, year, month));
    }

    @Operation(summary = "가계부 분석용 - 한달 고정지출 조회용 API", description = "가계부 분석용 한달 고정 지출 조회용 API 입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = ScheduleDTO.AccountBookScheduleAnalyze.class)))})
    @GetMapping(value = "/{id}/analyze/schedule")
    public ResponseEntity<ScheduleDTO.AccountBookScheduleAnalyze> getAccountBookScheduleAnalyze(@PathVariable("id") Long id) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(scheduleService.getScheduleAnalyze(id, currentMemberId));
    }

    @Operation(summary = "가계부 분석용 - 지난달과 분석용 API", description = "가계부 분석용 지난달과 분석용 API입니다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = MonthlyCompare.class)))})
    @GetMapping(value = "/{id}/analyze/compare/{year}/{month}")
    public ResponseEntity<MonthlyCompare> getAccountBookMonthlyCompare(@PathVariable("id") Long id, @PathVariable("year") Long year, @PathVariable("month") Long month) {
        return ResponseEntity.ok(accountBookDataService.getAccountBookDataMonthlyCompare(id, year, month));
    }

    @Operation(summary = "가계부 분석용 - 한달 가계부 내역 조회 API", description = "가계부 분석용 한달 내역 조회 API 입니다")
    @GetMapping(value = "/{id}/analyze/month/{year}/{month}")
    public ResponseEntity<Page<AccountBookDataQ>> getAccountBookMonthlyDetail(@PathVariable("id") Long id, @PathVariable("year") Long year, @PathVariable("month") Long month, @RequestParam(value = "category", required = false, defaultValue = "") String category, Pageable pageable) {
        return ResponseEntity.ok(accountBookDataService.getMonthlyAccountBookDetail(id, year, month, pageable, category));
    }

    @Operation(summary = "가계부 분석용 - 예산과 비교용 API", description = "가계부 분석용 예산과 비교용 API 입니다")
    @GetMapping(value = "/{id}/analyze/budget/{year}/{month}")
    public ResponseEntity<BudgetDTO.BudgetCompare> getAccountBookBudgetCompare(@PathVariable("id") Long id, @PathVariable("year") Long year, @PathVariable("month") Long month) {
        return ResponseEntity.ok(budgetService.compareWithBudget(id, year, month));
    }

    @Operation(summary = "가계부 분석용 - 주차별 지출 금액 조회", description = "가계부 분석용 주차별 지출 금액 조회 API 입니다")
    @GetMapping(value = "/{id}/analyze/weekly/{year}/{month}/{startDay}")
    public ResponseEntity<AccountBookWeeklyRecord> getAccountBookWeeklyCompare(@PathVariable("id") Long id, @PathVariable("year") Long year, @PathVariable("month") Long month, @PathVariable("startDay") Long startDay) {
        return ResponseEntity.ok(recordService.getWeeklyRecordSum(id, year, month, startDay));
    }

    @Operation(summary = "기간 별 내역 조회", description = "기간 별 내역 조회 API 입니다")
    @GetMapping("/{id}/analyze/custom/{year}/{month}/{startDay}/{endDay}")
    public ResponseEntity<Page<AccountBookDataQ>> getCustomAccountBookData(@PathVariable("id") Long id, @PathVariable("year") Long year, @PathVariable("month") Long month, @PathVariable("startDay") Long startDay, @PathVariable("endDay") Long endDay, @RequestParam(value = "category", required = false, defaultValue = "") String category, Pageable pageable) {
        return ResponseEntity.ok(accountBookDataService.getCustomAccountBookData(id, year, month, startDay, endDay, category, pageable));
    }

}
