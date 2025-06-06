package com.uliga.uliga_backend.account_book.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uliga.uliga_backend.account_book.dto.req.AccountBookRecordQueryDto;
import com.uliga.uliga_backend.account_book.dto.req.CreateAccountBookDto;
import com.uliga.uliga_backend.account_book.dto.req.CreateAccountBookRecordDto;
import com.uliga.uliga_backend.account_book.dto.req.CreateInvitationDto;
import com.uliga.uliga_backend.account_book.dto.req.DeleteAccountBookRecordDto;
import com.uliga.uliga_backend.account_book.dto.req.InvitationReplyDto;
import com.uliga.uliga_backend.account_book.dto.req.UpdateAccountBookDto;
import com.uliga.uliga_backend.account_book.dto.res.AccountBookDto;
import com.uliga.uliga_backend.account_book.dto.res.AccountBookInvitationDto;
import com.uliga.uliga_backend.account_book.dto.res.AccountBookRecordDto;
import com.uliga.uliga_backend.account_book.service.AccountBookServiceV2;
import com.uliga.uliga_backend.account_book_user.dto.req.DeleteAccountBookUserDto;
import com.uliga.uliga_backend.account_book_user.dto.res.AccountBookUserDto;
import com.uliga.uliga_backend.common.annotation.Serialize;
import com.uliga.uliga_backend.common.dto.req.OrderByQuery;
import com.uliga.uliga_backend.common.dto.req.PaginateQuery;
import com.uliga.uliga_backend.common.dto.res.CountDto;
import com.uliga.uliga_backend.common.dto.res.PaginatedDto;
import com.uliga.uliga_backend.jooq.tables.pojos.AccountBook;
import com.uliga.uliga_backend.jooq.tables.pojos.AccountBookInvitation;
import com.uliga.uliga_backend.jooq.tables.pojos.AccountBookUser;
import com.uliga.uliga_backend.util.SecurityUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "가계부", description = "가계부 관련 API 입니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("v2/accountBook")
public class AccountBookControllerV2 {
  private final AccountBookServiceV2 accountBookService;

  @Operation(summary = "가계부 생성 API")
  @PostMapping()
  @Serialize(dto = AccountBookDto.class)
  public ResponseEntity<Mono<AccountBook>> createAccountBook(
      @RequestBody CreateAccountBookDto dto) {
    return ResponseEntity.ok(accountBookService.createAccountBook(dto));
  }

  @Operation(summary = "가계부 조회 API")
  @GetMapping()
  @Serialize(dto = AccountBookDto.class)
  public ResponseEntity<Flux<AccountBook>> getAccountBooks() {
    Long memberId = SecurityUtil.getCurrentMemberId();
    return ResponseEntity.ok(accountBookService.getMemberAccountBooks(memberId));
  }

  @Operation(summary = "가계부 상세 조회 API")
  @GetMapping("/{accountBookId}")
  @Serialize(dto = AccountBookDto.class)
  public ResponseEntity<Mono<AccountBook>> getAccountBook(@PathVariable("accountBookId") Long accountBookId) {
    Long memberId = SecurityUtil.getCurrentMemberId();
    return ResponseEntity.ok(accountBookService.getAccountBookDetail(memberId, accountBookId));
  }

  @Operation(summary = "가계부 업데이트 API")
  @PatchMapping()
  @Serialize(dto = AccountBookDto.class)
  public ResponseEntity<Mono<AccountBook>> updateAccountBook(
      @RequestBody UpdateAccountBookDto dto) {
    return ResponseEntity.ok(accountBookService.updateAccountBook(dto));
  }

  @Operation(summary = "가계부 멤버 초대 API")
  @PostMapping("/invitation")
  @Serialize(dto = AccountBookInvitationDto.class)
  public ResponseEntity<Mono<AccountBookInvitation>> inviteUser(@RequestBody CreateInvitationDto dto) {
    return ResponseEntity.ok(accountBookService.createInvitation(dto));
  }

  @Operation(summary = "가계부 초대 응답 API")
  @PostMapping("/invitation/reply")
  @Serialize(dto = AccountBookInvitationDto.class)
  public ResponseEntity<Mono<AccountBookInvitation>> replyToInvitation(@RequestBody InvitationReplyDto dto) {
    return ResponseEntity.ok(accountBookService.replyToInvitation(dto));
  }

  @Operation(summary = "가계부 내역 조회 API")
  @GetMapping("/record")
  public ResponseEntity<Mono<PaginatedDto<AccountBookRecordDto>>> getAccountBookRecords(
      @ModelAttribute @Validated AccountBookRecordQueryDto query, @ModelAttribute PaginateQuery paginate,
      @ModelAttribute OrderByQuery orderBy) {
    return ResponseEntity.ok(accountBookService.getAccountBookRecords(query, paginate, orderBy));
  }

  @Operation(summary = "가계부 내역 일괄 삭제 API")
  @DeleteMapping("/record")
  public ResponseEntity<Mono<CountDto>> deleteAccountBookRecords(@RequestBody DeleteAccountBookRecordDto dto) {
    return ResponseEntity.ok(accountBookService.deleteAccountBookItems(dto));
  }

  @Operation(summary = "가계부 내역 일괄 추가 API")
  @PostMapping("/record")
  public ResponseEntity<Mono<CountDto>> createAccountBookRecords(@RequestBody CreateAccountBookRecordDto dto) {
    return ResponseEntity.ok(accountBookService.createAccountBookItems(dto));
  }

  @Operation(summary = "가계부 유저 조회 API")
  @GetMapping("/{accountBookId}/user")
  @Serialize(dto = AccountBookUserDto.class)
  public ResponseEntity<Flux<AccountBookUser>> getAccountBookUsers(
      @PathVariable("accountBookId") Long accountBookId) {
    return ResponseEntity.ok(accountBookService.getAccountBookUsers(accountBookId));
  }

  @Operation(summary = "가계부 유저 삭제 API")
  @DeleteMapping("/{accountBookId}/user")
  @Serialize(dto = AccountBookUserDto.class)
  public ResponseEntity<Mono<AccountBookUser>> deleteAccountBookUser(@RequestBody DeleteAccountBookUserDto dto) {
    return ResponseEntity.ok(accountBookService.deleteAccountBookUser(dto));
  }

  @Operation(summary = "가계부 삭제 API")
  @DeleteMapping("/{accountBookId}")
  @Serialize(dto = AccountBookDto.class)
  public ResponseEntity<Mono<AccountBook>> deleteAccountBook(@PathVariable("accountBookId") Long accountBookId) {
    return ResponseEntity.ok(accountBookService.deleteAccountBook(accountBookId));
  }

}
