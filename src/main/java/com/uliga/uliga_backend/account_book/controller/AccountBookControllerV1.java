package com.uliga.uliga_backend.account_book.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.account_book.service.AccountBookService;
import com.uliga.uliga_backend.account_book_data.service.AccountBookDataService;
import com.uliga.uliga_backend.budget.service.BudgetService;
import com.uliga.uliga_backend.category.service.CategoryService;
import com.uliga.uliga_backend.income.service.IncomeService;
import com.uliga.uliga_backend.record.service.RecordService;
import com.uliga.uliga_backend.schedule.service.ScheduleService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "가계부", description = "가계부 관련 API 입니다")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/accountBook")
public class AccountBookControllerV1 {

  private final AccountBookService accountBookService;
  private final AccountBookDataService accountBookDataService;
  private final BudgetService budgetService;
  private final IncomeService incomeService;
  private final RecordService recordService;
  private final CategoryService categoryService;
  private final ScheduleService scheduleService;
  private final ObjectMapper objectMapper;

  // TODO: expenseController, revenueController에 from, to, datePeriod로 조회할 수 있도록
  // 수정, aggregate O
  // @Operation(summary = "한달 가계부 수입/지출 조회 API", description = "한달동안의 가계부 수입/지출조회
  // API 입니다")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "조회 성공시", content =
  // @Content(schema = @Schema(implementation = AccountBookDataDailySum.class))),
  // @ApiResponse(responseCode = "401", description = "엑세스 만료시", content =
  // @Content(schema = @Schema(implementation = ErrorResponse.class))) })
  // @GetMapping(value = "/{id}/item/{year}/{month}")
  // public ResponseEntity<AccountBookDataDailySum> getAccountBookItems(
  // @Parameter(name = "id", description = "가계부 아이디", in = PATH)
  // @PathVariable("id") Long id,
  // @Parameter(name = "year", description = "년도", in = PATH)
  // @PathVariable("year") Long year,
  // @Parameter(name = "month", description = "달", in = PATH)
  // @PathVariable("month") Long month) {
  // return ResponseEntity.ok(accountBookDataService.getAccountBookItems(id, year,
  // month));
  // }

  // TODO: expenseController, revenueController 조회 쿼리필드로 date 추가
  // @Operation(summary = "하루 수입/지출 내역 상세 조회", description = "하루 가계부 수입/지출 조회 API
  // 입니다")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "조회 성공시", content =
  // @Content(schema = @Schema(implementation =
  // DailyAccountBookDataDetails.class))),
  // @ApiResponse(responseCode = "401", description = "엑세스 만료시", content =
  // @Content(schema = @Schema(implementation = ErrorResponse.class))) })
  // @GetMapping(value = "/{id}/item/{year}/{month}/{day}")
  // public ResponseEntity<DailyAccountBookDataDetails>
  // getAccountBookItemDetailsByDay(
  // @Parameter(name = "id", description = "가계부 아이디", in = PATH)
  // @PathVariable("id") Long id,
  // @Parameter(name = "year", description = "년도", in = PATH)
  // @PathVariable("year") Long year,
  // @Parameter(name = "month", description = "달", in = PATH)
  // @PathVariable("month") Long month,
  // @Parameter(name = "day", description = "하루", in = PATH) @PathVariable("day")
  // Long day) {
  // return
  // ResponseEntity.ok(accountBookDataService.getDailyAccountBookDataDetails(id,
  // year, month, day));
  // }

  // @Operation(summary = "가계부 내역 삭제", description = "가계부 내역 삭제 API 입니다")
  // @DeleteMapping(value = "/item")
  // public ResponseEntity<String> deleteAccountBookItems(@Valid @RequestBody
  // DeleteItemRequest deleteItemRequest) {

  // accountBookDataService.deleteAccountBookData(deleteItemRequest);
  // return ResponseEntity.ok("DELETED");
  // }

  // TODO: from,to datePeriod 별 지출, 수입 예산 총합 조회 API, aggregate O
  // expense, revenue, budget 각각 API 호출하는 것으로 변경
  // @Operation(summary = "한달 가계부 지출/수입/예산 총합 조회 API", description = "한달 동안의 가계부
  // 수입/지출/예산 총합 조회 API 입니다")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "조회 성공시", content =
  // @Content(schema = @Schema(implementation = GetAccountBookAssets.class))),
  // @ApiResponse(responseCode = "401", description = "엑세스 만료시", content =
  // @Content(schema = @Schema(implementation = ErrorResponse.class))) })
  // @GetMapping(value = "/{id}/asset/{year}/{month}")
  // public ResponseEntity<GetAccountBookAssets> getAccountBookAssets(
  // @Parameter(name = "id", description = "가계부 아이디", in = PATH)
  // @PathVariable("id") Long id,
  // @Parameter(name = "year", description = "년도", in = PATH)
  // @PathVariable("year") Long year,
  // @Parameter(name = "month", description = "달", in = PATH)
  // @PathVariable("month") Long month) {

  // GetAccountBookAssets accountBookAssets = GetAccountBookAssets.builder()
  // .budget(budgetService.getMonthlyBudgetSum(id, year, month))
  // .record(recordService.getMonthlyRecordSum(id, year, month))
  // .income(incomeService.getMonthlyIncomeSum(id, year, month)).build();
  // return ResponseEntity.ok(accountBookAssets);
  // }

  // @Operation(summary = "수입/지출 추가 API", description = "수입/지출 한번에 추가 API")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "추가 성공시", content =
  // @Content(schema = @Schema(implementation = CreateResult.class))),
  // @ApiResponse(responseCode = "401", description = "엑세스 만료시", content =
  // @Content(schema = @Schema(implementation = ErrorResponse.class))) })
  // @PostMapping(value = "/item")
  // public ResponseEntity<CreateResult> createItems(
  // @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description =
  // "수입/지출 생성 요청") @RequestBody CreateItems items) {

  // Long id = SecurityUtil.getCurrentMemberId();

  // return ResponseEntity.ok(accountBookDataService.createItems(id, items));
  // }

  // TODO: category 컨트롤러로 이전
  // @Operation(summary = "가계부에 카테고리 추가 API", description = "가계부에 카테고리 추가하는 API
  // 입니다")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "추가 성공시", content =
  // @Content(schema = @Schema(implementation = CategoryCreateResult.class))),
  // @ApiResponse(responseCode = "401", description = "엑세스 만료시", content =
  // @Content(schema = @Schema(implementation = ErrorResponse.class))) })
  // @PostMapping(value = "/category")
  // public ResponseEntity<CategoryCreateResult> createCategories(
  // @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description =
  // "카테고리 생성 요청") @RequestBody CategoryCreateRequest createRequest) {

  // Long currentMemberId = SecurityUtil.getCurrentMemberId();
  // return ResponseEntity.ok(categoryService.createCategories(currentMemberId,
  // createRequest));

  // }

  // TODO: expenseController
  // @Operation(summary = "가계부에 지출 추가 API", description = "가계부에 지출 추가하는 API 입니다")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "추가 성공시", content =
  // @Content(schema = @Schema(implementation = AddRecordResult.class))),
  // @ApiResponse(responseCode = "401", description = "엑세스 만료시", content =
  // @Content(schema = @Schema(implementation = ErrorResponse.class))) })
  // @PostMapping(value = "/record")
  // public ResponseEntity<AddRecordResult> addRecord(
  // @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description =
  // "지출 한개 생성 요청") @RequestBody AddRecordRequest request) {

  // Long currentMemberId = SecurityUtil.getCurrentMemberId();
  // return ResponseEntity.ok(recordService.addRecord(currentMemberId, request));
  // }

  // TODO: revenueController
  // @Operation(summary = "가계부에 수입 추가 API", description = "가계부에 수입 추가하는 API 입니다")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "추가 성공시", content =
  // @Content(schema = @Schema(implementation = AddIncomeResult.class))),
  // @ApiResponse(responseCode = "401", description = "엑세스 만료시", content =
  // @Content(schema = @Schema(implementation = ErrorResponse.class))) })
  // @PostMapping(value = "/income")
  // public ResponseEntity<AddIncomeResult> addIncome(
  // @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description =
  // "수입 한개 생성 요청") @RequestBody AddIncomeRequest request) {

  // Long currentMemberId = SecurityUtil.getCurrentMemberId();
  // return ResponseEntity.ok(incomeService.addIncome(currentMemberId, request));
  // }

  // TODO: categoryController
  // @Operation(summary = "가계부 카테고리 조회 API", description = "가계부 카테고리 조회 API 입니다")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "조회 성공시", content =
  // @Content(schema = @Schema(implementation = AccountBookCategories.class))),
  // @ApiResponse(responseCode = "401", description = "엑세스 만료시", content =
  // @Content(schema = @Schema(implementation = ErrorResponse.class))) })
  // @GetMapping(value = "/{id}/category")
  // public ResponseEntity<AccountBookCategories> getAccountBookCategory(
  // @Parameter(name = "id", description = "가계부 아이디", in = PATH)
  // @PathVariable("id") Long id) {
  // return ResponseEntity.ok(categoryService.getAccountBookCategories(id));
  // }

  // TODO: accountBookControllerV2
  // @Operation(summary = "가계부 멤버 조회 API", description = "가계부 멤버 조회 API 입니다")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "조회 성공시", content =
  // @Content(schema = @Schema(implementation = AccountBookMembers.class))),
  // @ApiResponse(responseCode = "401", description = "엑세스 만료시", content =
  // @Content(schema = @Schema(implementation = ErrorResponse.class))) })
  // @GetMapping(value = "/{id}/member")
  // public ResponseEntity<AccountBookMembers> getAccountBookMembers(
  // @Parameter(name = "id", description = "가계부 아이디", in = PATH)
  // @PathVariable("id") Long id) {

  // return ResponseEntity.ok(accountBookService.getAccountBookMembers(id));
  // }

  // TODO:fixedController
  // @Operation(summary = "가계부에 금융 일정 추가", description = "가계부에 금융 일정 추가 API 입니다")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "추가 성공시", content =
  // @Content(schema = @Schema(implementation = AddScheduleResult.class))) })
  // @PostMapping(value = "/schedule")
  // public ResponseEntity<AddScheduleResult> addSchedule(@Valid @RequestBody
  // ScheduleDTO.AddSchedules addSchedules)
  // throws JsonProcessingException {
  // Long memberId = SecurityUtil.getCurrentMemberId();
  // return ResponseEntity.ok(scheduleService.addSchedule(memberId,
  // addSchedules));
  // }

  // TODO: fixedController
  // @Operation(summary = "가계부 금융 일정 세부 조회", description = "가계부 금융 일정 세부 조회 API
  // 입니다")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "조회 성공시", content =
  // @Content(schema = @Schema(implementation = GetAccountBookSchedules.class)))
  // })
  // @GetMapping(value = "/{id}/schedule")
  // public ResponseEntity<GetAccountBookSchedules> getAccountBookSchedules(
  // @Parameter(name = "id", description = "가계부 아이디", in = PATH)
  // @PathVariable("id") Long id) {
  // Long memberId = SecurityUtil.getCurrentMemberId();
  // return ResponseEntity.ok(scheduleService.getAccountBookSchedules(memberId,
  // id));
  // }

  // @Operation(summary = "가계부 삭제 요청", description = "가계부 삭제 요청 API 입니다")
  // @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "삭제
  // 성공시") })
  // @DeleteMapping(value = "/{id}")
  // public ResponseEntity<String> deleteAccountBook(
  // @Parameter(name = "id", description = "가계부 아이디", in = PATH)
  // @PathVariable("id") Long id) {

  // Long currentMemberId = SecurityUtil.getCurrentMemberId();
  // return ResponseEntity.ok(accountBookService.deleteAccountBook(id,
  // currentMemberId));
  // }

  // @Operation(summary = "가계부 내역 다수 삭제 요청", description = "가계부 내역 다수 삭제 요청 API
  // 입니다")
  // @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "삭제
  // 성공시") })
  // @DeleteMapping(value = "/data")
  // public ResponseEntity<String> deleteAccountBookData(
  // @Valid @RequestBody AccountBookDataDeleteRequest dataDeleteRequest) {
  // // TODO: 프론트랑 확인 후 중복된 API이니 제거 예정

  // accountBookDataService
  // .deleteAccountBookData(DeleteItemRequest.builder().deleteIds(dataDeleteRequest.getIds())
  // .build());
  // return ResponseEntity.ok("DELETED");
  // }

  // TODO: accountBookController, from, to, aggregate X
  // @Operation(summary = "가계부 전체 내역 전체/년도별/월별 조회", description = "가계부 전체 내역 조회
  // API 입니다")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "조회 성공시", content =
  // @Content(schema = @Schema(implementation = AccountBookDataQ.class))) })
  // @GetMapping(value = "/{id}/history")
  // public ResponseEntity<Page<AccountBookDataQ>> getAccountBookHistory(
  // @Parameter(name = "id", description = "가계부 아이디", in = PATH)
  // @PathVariable("id") Long id,
  // @RequestParam(value = "categoryId", required = false) Long categoryId,
  // @RequestParam(value = "year", required = false) Long year,
  // @RequestParam(value = "month", required = false) Long month, Pageable
  // pageable) {
  // return ResponseEntity.ok(
  // accountBookDataService.getAccountBookHistory(id, categoryId, year, month,
  // pageable));
  // }

  // TODO: expenseController,from,to datePeriod 사용하도록, aggregate O
  // @Operation(summary = "가계부 분석용 - 날짜별 지출 조회 API", description = "가계부 분석용 날짜별 지출
  // 조회 API 입니다")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "조회 성공시", content =
  // @Content(schema = @Schema(implementation =
  // AccountBookDailyRecordSumAndMonthlySum.class))) })
  // @GetMapping(value = "/{id}/analyze/{year}/{month}")
  // public ResponseEntity<AccountBookDailyRecordSumAndMonthlySum>
  // getAccountBookDailyValues(
  // @PathVariable("id") Long id,
  // @PathVariable("year") Long year, @PathVariable("month") Long month) {
  // return ResponseEntity.ok(recordService.getDailyRecordSumAndMonthlySum(id,
  // year, month));
  // }

  // TODO: expenseController, 위와 같은 메소드로 작업 가능할듯? 쿼리에 categoryId 추가
  // @Operation(summary = "가계부 분석용 한달 카테고리 별 지출 조회 API", description = "가계부 분석용 한달
  // 카테고리 별 지출 조회 API 입니다")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "조회 성공시", content =
  // @Content(schema = @Schema(implementation =
  // MonthlyRecordSumPerCategories.class))) })
  // @GetMapping(value = "/{id}/analyze/category/{year}/{month}")
  // public ResponseEntity<MonthlyRecordSumPerCategories>
  // getAccountBookCategoryAnalyze(@PathVariable("id") Long id,
  // @PathVariable("year") Long year, @PathVariable("month") Long month) {
  // return ResponseEntity.ok(recordService.getMonthlyRecordSumPerCategories(id,
  // year, month));
  // }

  // TODO: fixedExpenseController, aggregate O
  // @Operation(summary = "가계부 분석용 - 한달 고정지출 조회용 API", description = "가계부 분석용 한달
  // 고정 지출 조회용 API 입니다")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "조회 성공시", content =
  // @Content(schema = @Schema(implementation =
  // ScheduleDTO.AccountBookScheduleAnalyze.class))) })
  // @GetMapping(value = "/{id}/analyze/schedule")
  // public ResponseEntity<ScheduleDTO.AccountBookScheduleAnalyze>
  // getAccountBookScheduleAnalyze(
  // @PathVariable("id") Long id) {
  // Long currentMemberId = SecurityUtil.getCurrentMemberId();
  // return ResponseEntity.ok(scheduleService.getScheduleAnalyze(id,
  // currentMemberId));
  // }

  // // 최근 3달 지출 총합 조회 API, aggregate O, 위 API 활용 가능
  // @Operation(summary = "가계부 분석용 - 지난달과 분석용 API", description = "가계부 분석용 지난달과
  // 분석용 API입니다")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "조회 성공시", content =
  // @Content(schema = @Schema(implementation = MonthlyCompare.class))) })
  // @GetMapping(value = "/{id}/analyze/compare/{year}/{month}")
  // public ResponseEntity<MonthlyCompare>
  // getAccountBookMonthlyCompare(@PathVariable("id") Long id,
  // @PathVariable("year") Long year, @PathVariable("month") Long month) {
  // return
  // ResponseEntity.ok(accountBookDataService.getAccountBookDataMonthlyCompare(id,
  // year, month));
  // }

  // 위 가계부 내역 조회 API로 대체 가능
  // @Operation(summary = "가계부 분석용 - 한달 가계부 내역 조회 API", description = "가계부 분석용 한달
  // 내역 조회 API 입니다")
  // @GetMapping(value = "/{id}/analyze/month/{year}/{month}")
  // public ResponseEntity<Page<AccountBookDataQ>>
  // getAccountBookMonthlyDetail(@PathVariable("id") Long id,
  // @PathVariable("year") Long year, @PathVariable("month") Long month,
  // @RequestParam(value = "category", required = false, defaultValue = "") String
  // category,
  // Pageable pageable) {
  // return ResponseEntity
  // .ok(accountBookDataService.getMonthlyAccountBookDetail(id, year, month,
  // pageable,
  // category));
  // }

  // TODO: expenseController, 이번달 지출 총합 조회,
  // @Operation(summary = "가계부 분석용 - 예산과 비교용 API", description = "가계부 분석용 예산과 비교용
  // API 입니다")
  // @GetMapping(value = "/{id}/analyze/budget/{year}/{month}")
  // public ResponseEntity<BudgetDTO.BudgetCompare>
  // getAccountBookBudgetCompare(@PathVariable("id") Long id,
  // @PathVariable("year") Long year, @PathVariable("month") Long month) {
  // return ResponseEntity.ok(budgetService.compareWithBudget(id, year, month));
  // }

  // TODO: expenseController, from,to, datePeriod, sum 필드 반환, aggregate O, 위에 정의한
  // API로 대체 가능
  // @Operation(summary = "가계부 분석용 - 주차별 지출 금액 조회", description = "가계부 분석용 주차별 지출
  // 금액 조회 API 입니다")
  // @GetMapping(value = "/{id}/analyze/weekly/{year}/{month}/{startDay}")
  // public ResponseEntity<AccountBookWeeklyRecord>
  // getAccountBookWeeklyCompare(@PathVariable("id") Long id,
  // @PathVariable("year") Long year, @PathVariable("month") Long month,
  // @PathVariable("startDay") Long startDay) {
  // return ResponseEntity.ok(recordService.getWeeklyRecordSum(id, year, month,
  // startDay));
  // }

  // TODO: accountBookController, from, to, aggregate X
  // @Operation(summary = "기간 별 내역 조회", description = "기간 별 내역 조회 API 입니다")
  // @GetMapping("/{id}/analyze/custom/{year}/{month}/{startDay}/{endDay}")
  // public ResponseEntity<Page<AccountBookDataQ>>
  // getCustomAccountBookData(@PathVariable("id") Long id,
  // @PathVariable("year") Long year, @PathVariable("month") Long month,
  // @PathVariable("startDay") Long startDay,
  // @PathVariable("endDay") Long endDay,
  // @RequestParam(value = "category", required = false, defaultValue = "") String
  // category,
  // Pageable pageable) {
  // return ResponseEntity.ok(
  // accountBookDataService.getCustomAccountBookData(id, year, month, startDay,
  // endDay,
  // category, pageable));
  // }

}
