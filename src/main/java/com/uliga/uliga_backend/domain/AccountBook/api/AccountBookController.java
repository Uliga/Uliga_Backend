package com.uliga.uliga_backend.domain.AccountBook.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uliga.uliga_backend.domain.AccountBook.application.AccountBookService;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.*;
import com.uliga.uliga_backend.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/accountBook")
public class AccountBookController {

    private final AccountBookService accountBookService;

    @GetMapping(value = "")
    public ResponseEntity<GetAccountBookInfos> getMemberAccountBook() {
        log.info("멤버 가계부 조회 API 호출");
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.getMemberAccountBook(id));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountBookInfo> getSingleAccountBookInfo(@PathVariable("id") Long id) {
        log.info("가계부 아이디로 조회 API 호출");
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.getSingleAccountBookInfo(id, memberId));
    }

    // 이메일로 유저 초대장 여기서 보내야함
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

    @PostMapping(value = "/invitation")
    public ResponseEntity<Invited> createInvitation(@RequestBody GetInvitations invitations) throws JsonProcessingException {
        log.info("멤버 초대 API 호출");
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.createInvitation(id, invitations));
    }

    @PostMapping(value = "/invitation/reply")
    public ResponseEntity<InvitationReplyResult> invitationReply(@RequestBody InvitationReply invitationReply) throws JsonProcessingException {
        log.info("초대 응답 API 호출");
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.invitationReply(id, invitationReply));
    }

    @GetMapping(value = "/{id}/item")
    public ResponseEntity<AccountBookItems> getAccountBookItems(@PathVariable("id") Long id) {
        return ResponseEntity.ok(accountBookService.getAccountBookItems(id));
    }

    @PostMapping(value = "/item")
    public ResponseEntity<CreateResult> createItems(@RequestBody CreateItems items) {

        log.info("지출 혹은 수입 생성 API 호출");
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.createItems(id, items));
    }


    @PostMapping(value = "/category")
    public ResponseEntity<CategoryCreateResult> createCategories(@RequestBody CategoryCreateRequest createRequest) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.createCategory(currentMemberId, createRequest));
    }

    @PatchMapping(value = "/record/category")
    public ResponseEntity<UpdateCategoryResult> updateCategoryToRecord(@RequestBody UpdateRecordCategory recordCategory) {
        return ResponseEntity.ok(accountBookService.updateRecordCategory(recordCategory));
    }

    @PatchMapping(value = "/income/category")
    public ResponseEntity<UpdateCategoryResult> updateCategoryToIncome(@RequestBody UpdateIncomeCategory incomeCategory) {
        return ResponseEntity.ok(accountBookService.updateIncomeCategory(incomeCategory));
    }

    @PostMapping(value = "/record")
    public ResponseEntity<AddRecordResult> addRecord(@RequestBody AddRecordRequest request) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.addRecord(currentMemberId, request));
    }

    @PostMapping(value = "/income")
    public ResponseEntity<AddIncomeResult> addIncome(@RequestBody AddIncomeRequest request) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.addIncome(currentMemberId, request));
    }

    @GetMapping(value = "/{id}/category")
    public ResponseEntity<AccountBookCategories> getAccountBookCategory(@PathVariable("id") Long id) {

        return ResponseEntity.ok(accountBookService.getAccountBookCategories(id));
    }

    @GetMapping(value = "/{id}/member")
    public ResponseEntity<AccountBookMembers> getAccountBookMembers(@PathVariable("id") Long id) {
        return ResponseEntity.ok(accountBookService.getAccountBookMembers(id));
    }
}
