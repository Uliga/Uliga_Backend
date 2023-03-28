package com.uliga.uliga_backend.domain.AccountBook.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uliga.uliga_backend.domain.AccountBook.application.AccountBookService;
import com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.AccountBookDataQ;
import com.uliga.uliga_backend.domain.Budget.dto.BudgetDTO.CreateBudgetDto;
import com.uliga.uliga_backend.domain.Budget.dto.NativeQ.BudgetInfoQ;
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
import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

@Tag(name = "가계부", description = "가계부 관련 API 입니다")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/accountBook")
public class AccountBookController {

    private final AccountBookService accountBookService;


    @Operation(summary = "멤버 가계부 조회 API", description = "멤버 가계부 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 가계부 조회 성공시", content = @Content(schema = @Schema(implementation = GetAccountBookInfos.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "")
    public ResponseEntity<GetAccountBookInfos> getMemberAccountBook() {

        log.info("멤버 가계부 조회 API 호출");
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.getMemberAccountBook(id));
    }

    @Operation(summary = "가계부 정보 조회 API", description = "가계부 정보 조회 API입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 조회 성공시", content = @Content(schema = @Schema(implementation = AccountBookInfo.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountBookInfo> getSingleAccountBookInfo(@Parameter(name = "id", description = "가계부 아이디", in = PATH)
                                                                    @PathVariable("id") Long id) {
        log.info("가계부 아이디로 조회 API 호출");
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.getSingleAccountBookInfo(id, memberId));
    }

    @Operation(summary = "가계부 생성 API", description = "가계부 생성하는 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가계부 생성시", content = @Content(schema = @Schema(implementation = SimpleAccountBookInfo.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "")
    public ResponseEntity<SimpleAccountBookInfo> createAccountBook(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "가계부 생성 요청") @RequestBody AccountBookCreateRequest accountBookCreateRequest) throws JsonProcessingException {

        log.info("가계부 생성 API 호출");
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.createAccountBook(id, accountBookCreateRequest));
    }


    @Operation(summary = "멤버 초대 API", description = "멤버 초대 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "초대 성공시", content = @Content(schema = @Schema(implementation = Invited.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/invitation")
    public ResponseEntity<Invited> createInvitation(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "초대 생성 요청") @RequestBody GetInvitations invitations) throws JsonProcessingException {

        log.info("멤버 초대 API 호출");
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.createInvitation(id, invitations));
    }

    @Operation(summary = "초대 응답 API", description = "초대 응답 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "초대 응답시", content = @Content(schema = @Schema(implementation = InvitationReply.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/invitation/reply")
    public ResponseEntity<InvitationReplyResult> invitationReply(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "초대 응답 요청") @RequestBody InvitationReply invitationReply) throws JsonProcessingException {

        log.info("초대 응답 API 호출");
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.invitationReply(id, invitationReply));
    }

    @Operation(summary = "한달 가계부 수입/지출 조회 API", description = "한달동안의 가계부 수입/지출조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = AccountBookIncomesAndRecords.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}/item/{year}/{month}")
    public ResponseEntity<AccountBookIncomesAndRecords> getAccountBookItems(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id,
                                                                            @Parameter(name = "year", description = "년도", in = PATH) @PathVariable("year") Long year,
                                                                            @Parameter(name = "month", description = "달", in = PATH) @PathVariable("month") Long month) {

        log.info("한달 가계부 수입/지출 조회 API 호출");
        return ResponseEntity.ok(accountBookService.getAccountBookItems(id, year, month));
    }


    @Operation(summary = "하루 수입/지출 내역 상세 조회", description = "하루 가계부 수입/지출 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = RecordAndIncomeDetails.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}/item/{year}/{month}/{day}")
    public ResponseEntity<RecordAndIncomeDetails> getAccountBookItemDetailsByDay(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id,
                                                                                 @Parameter(name = "year", description = "년도", in = PATH) @PathVariable("year") Long year,
                                                                                 @Parameter(name = "month", description = "달", in = PATH) @PathVariable("month") Long month,
                                                                                 @Parameter(name = "day", description = "하루", in = PATH) @PathVariable("day") Long day) {

        log.info("하루 수입/지출 내역 상세 조회");
        return ResponseEntity.ok(accountBookService.getAccountBookItemDetails(id, year, month, day));
    }

    @Operation(summary = "한달 가계부 지출/수입/예산 총합 조회 API", description = "한달 동안의 가계부 수입/지출/예산 총합 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = GetAccountBookAssets.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}/asset/{year}/{month}")
    public ResponseEntity<GetAccountBookAssets> getAccountBookAssets(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id,
                                                                     @Parameter(name = "year", description = "년도", in = PATH) @PathVariable("year") Long year,
                                                                     @Parameter(name = "month", description = "달", in = PATH) @PathVariable("month") Long month) {

        log.info("한달 가계부 지출/수입/예산 총합 조회 API 호출");
        return ResponseEntity.ok(accountBookService.getAccountBookAssets(id, year, month));
    }

    @Operation(summary = "수입/지출 추가 API", description = "수입/지출 한번에 추가 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = CreateResult.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/item")
    public ResponseEntity<CreateResult> createItems(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수입/지출 생성 요청") @RequestBody CreateItems items) {

        log.info("지출 혹은 수입 생성 API 호출");
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.createItems(id, items));
    }

    @Operation(summary = "가계부에 카테고리 추가 API", description = "가계부에 카테고리 추가하는 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = CategoryCreateResult.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/category")
    public ResponseEntity<CategoryCreateResult> createCategories(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "카테고리 생성 요청") @RequestBody CategoryCreateRequest createRequest) {

        log.info("가계부에 카테고리 추가 API 호출");
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.createCategory(currentMemberId, createRequest));
    }

    @Operation(summary = "가계부에 지출 추가 API", description = "가계부에 지출 추가하는 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = AddRecordResult.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/record")
    public ResponseEntity<AddRecordResult> addRecord(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "지출 한개 생성 요청") @RequestBody AddRecordRequest request) {

        log.info("가계부에 지출 한개 추가 api 호출");
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.addRecord(currentMemberId, request));
    }

    @Operation(summary = "가계부에 수입 추가 API", description = "가계부에 수입 추가하는 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = AddIncomeResult.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/income")
    public ResponseEntity<AddIncomeResult> addIncome(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수입 한개 생성 요청") @RequestBody AddIncomeRequest request) {

        log.info("가계부에 수입 한개 추가 api 호출");
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.addIncome(currentMemberId, request));
    }

    @Operation(summary = "가계부 카테고리 조회 API", description = "가계부 카테고리 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = AccountBookCategories.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}/category")
    public ResponseEntity<AccountBookCategories> getAccountBookCategory(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id) {

        log.info("가계부 카테고리 조회 API 호출");
        return ResponseEntity.ok(accountBookService.getAccountBookCategories(id));
    }

    @Operation(summary = "가계부 멤버 조회 API", description = "가계부 멤버 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = AccountBookMembers.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}/member")
    public ResponseEntity<AccountBookMembers> getAccountBookMembers(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id) {

        log.info("가계부 멤버 조회 API 호출");
        return ResponseEntity.ok(accountBookService.getAccountBookMembers(id));
    }

    @Operation(summary = "가계부 예산 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = BudgetInfoQ.class))),
            @ApiResponse(responseCode = "409", description = "해당 년도/달에 예산 이미 존재시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/budget")
    public ResponseEntity<BudgetInfoQ> addBudget(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "예산 생성 요청",
            content = @Content(schema = @Schema(implementation = CreateBudgetDto.class)))
                                                 @RequestBody Map<String, Object> createBudgetDto) {

        log.info("가계부 예산 추가 API 호출");
        return ResponseEntity.ok(accountBookService.addBudget(createBudgetDto));
    }

    @Operation(summary = "가계부에 금융 일정 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = AddScheduleResult.class)))
    })
    @PostMapping(value = "/schedule")
    public ResponseEntity<AddScheduleResult> addSchedule(@Valid @RequestBody AddSchedules addSchedules) throws JsonProcessingException {

        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.addSchedule(memberId, addSchedules));
    }

    @Operation(summary = "가계부 금융 일정 세부 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = GetAccountBookSchedules.class)))
    })
    @GetMapping(value = "/{id}/schedule")
    public ResponseEntity<GetAccountBookSchedules> getAccountBookSchedules(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.getAccountBookSchedules(memberId, id));
    }

    @Operation(summary = "가계부 삭제 요청")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공시")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteAccountBook(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.deleteAccountBook(id, currentMemberId));
    }

    @Operation(summary = "내역 조회에 쓸 API, 전체/년도별/월별 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = AccountBookDataQ.class)))
    })
    @GetMapping(value = "/{id}/history")
    public ResponseEntity<Page<AccountBookDataQ>> getAccountBookHistory(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id,
                                                                        @RequestParam(value = "year", required = false, defaultValue = "") Long year,
                                                                        @RequestParam(value = "month", required = false, defaultValue = "") Long month,
                                                                        Pageable pageable) {

        log.info("가계부 내역 조회 API 호출");
        return ResponseEntity.ok(accountBookService.getAccountBookHistory(id, year, month, pageable));
    }

}
