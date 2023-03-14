package com.uliga.uliga_backend.domain.AccountBook.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uliga.uliga_backend.domain.AccountBook.application.AccountBookService;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.*;
import com.uliga.uliga_backend.domain.Budget.application.BudgetService;
import com.uliga.uliga_backend.domain.Budget.dto.BudgetDTO.CreateBudgetDto;
import com.uliga.uliga_backend.domain.Budget.dto.NativeQ.BudgetInfoQ;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import com.uliga.uliga_backend.global.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
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
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountBookInfo> getSingleAccountBookInfo(@PathVariable("id") Long id) {
        log.info("가계부 아이디로 조회 API 호출");
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.getSingleAccountBookInfo(id, memberId));
    }

    @Operation(summary = "가계부 생성 API", description = "가계부 생성하는 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가계부 생성시", content = @Content(schema = @Schema(implementation = SimpleAccountBookInfo.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })

    @PostMapping(value = "")
    public ResponseEntity<SimpleAccountBookInfo> createAccountBook(@RequestBody CreateRequest createRequest) throws JsonProcessingException {
        log.info("가계부 생성 API 호출");
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.createAccountBook(id, createRequest));
    }

    @PostMapping(value = "/private")
    public ResponseEntity<SimpleAccountBookInfo> createAccountBookPrivate(@RequestBody CreateRequestPrivate createRequest) {
        log.info("개인 가계부 생성 API 호출");
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.createAccountBookPrivate(id, createRequest));
    }

    @Operation(summary = "멤버 초대 API", description = "멤버 초대 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "초대 성공시", content = @Content(schema = @Schema(implementation = Invited.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/invitation")
    public ResponseEntity<Invited> createInvitation(@RequestBody GetInvitations invitations) throws JsonProcessingException {
        log.info("멤버 초대 API 호출");
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.createInvitation(id, invitations));
    }

    @Operation(summary = "초대 응답 API", description = "초대 응답 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "초대 응답시", content = @Content(schema = @Schema(implementation = InvitationReply.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/invitation/reply")
    public ResponseEntity<InvitationReplyResult> invitationReply(@RequestBody InvitationReply invitationReply) throws JsonProcessingException {
        log.info("초대 응답 API 호출");
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.invitationReply(id, invitationReply));
    }

    @Operation(summary = "한달 가계부 수입/지출/금융일정 조회 API", description = "한달동안의 가계부 수입/지출/금융일정 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = AccountBookItems.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}/item/{year}/{month}")
    public ResponseEntity<AccountBookItems> getAccountBookItems(
            @PathVariable("id") Long id,
            @PathVariable("year") Long year,
            @PathVariable("month") Long month,
            @RequestParam(value = "startDay", required = false, defaultValue = "1") Long startDay,
            @RequestParam(value = "endDay", required = false, defaultValue = "31") Long endDay
    ) {
        log.info("한달 가계부 수입/지출/금융 일정 조회 API 호출");
        return ResponseEntity.ok(accountBookService.getAccountBookItems(id, year, month, startDay, endDay));
    }

    @Operation(summary = "한달 가계부 지출/수입/예산 총합 조회 API", description = "한달 동안의 가계부 수입/지출/예산 총합 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = GetAccountBookAssets.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}/asset/{year}/{month}")
    public ResponseEntity<GetAccountBookAssets> getAccountBookAssets(
            @PathVariable("id") Long id,
            @PathVariable("year") Long year,
            @PathVariable("month") Long month
    ) {
        log.info("한달 가계부 지출/수입/예산 총합 조회 API 호출");
        return ResponseEntity.ok(accountBookService.getAccountBookAssets(id, year, month));
    }

    @Operation(summary = "수입/지출 추가 API", description = "수입/지출 한번에 추가 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = CreateResult.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/item")
    public ResponseEntity<CreateResult> createItems(@RequestBody CreateItems items) {

        log.info("지출 혹은 수입 생성 API 호출");
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.createItems(id, items));
    }

    @Operation(summary = "가계부에 카테고리 추가 API", description = "가계부에 카테고리 추가하는 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = CategoryCreateResult.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/category")
    public ResponseEntity<CategoryCreateResult> createCategories(@RequestBody CategoryCreateRequest createRequest) {
        log.info("가계부에 카테고리 추가 API 호출");
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.createCategory(currentMemberId, createRequest));
    }

    @Operation(summary = "가계부에 지출 추가 API", description = "가계부에 카테고리 추가하는 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = AddRecordResult.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/record")
    public ResponseEntity<AddRecordResult> addRecord(@RequestBody AddRecordRequest request) {
        log.info("가계부에 지출 한개 추가 api 호출");
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.addRecord(currentMemberId, request));
    }

    @Operation(summary = "가계부에 수입 추가 API", description = "가계부에 카테고리 추가하는 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = AddIncomeResult.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/income")
    public ResponseEntity<AddIncomeResult> addIncome(@RequestBody AddIncomeRequest request) {
        log.info("가계부에 수입 한개 추가 api 호출");
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.addIncome(currentMemberId, request));
    }

    @Operation(summary = "가계부 카테고리 조회 API", description = "가계부 카테고리 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = AccountBookCategories.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}/category")
    public ResponseEntity<AccountBookCategories> getAccountBookCategory(@PathVariable("id") Long id) {
        log.info("가계부 카테고리 조회 API 호출");
        return ResponseEntity.ok(accountBookService.getAccountBookCategories(id));
    }

    @Operation(summary = "가계부 멤버 조회 API", description = "가계부 멤버 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = AccountBookMembers.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}/member")
    public ResponseEntity<AccountBookMembers> getAccountBookMembers(@PathVariable("id") Long id) {
        log.info("가계부 멤버 조회 API 호출");
        return ResponseEntity.ok(accountBookService.getAccountBookMembers(id));
    }

    @Operation(summary = "가계부 예산 추가")
    @PostMapping(value = "/budget")
    public ResponseEntity<BudgetInfoQ> addBudget(@RequestBody Map<String, Object> createBudgetDto) {
        log.info("가계부 예산 추가 API 호출");
        return ResponseEntity.ok(accountBookService.addBudget(createBudgetDto));
    }
    // 구현 해야될 기능 틀

    @Operation(summary = "가계부에 금융 일정 추가 - 미구현")
    @PostMapping(value = "/schedule")
    public void addSchedule() {

    }
}
