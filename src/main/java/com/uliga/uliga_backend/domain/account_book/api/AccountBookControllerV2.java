package com.uliga.uliga_backend.domain.account_book.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uliga.uliga_backend.domain.account_book.application.AccountBookServiceV2;
import com.uliga.uliga_backend.domain.account_book.dto.req.CreateAccountBookDto;
import com.uliga.uliga_backend.domain.account_book.dto.req.CreateInvitationDto;
import com.uliga.uliga_backend.domain.account_book.dto.req.InvitationReplyDto;
import com.uliga.uliga_backend.domain.account_book.dto.req.UpdateAccountBookDto;
import com.uliga.uliga_backend.domain.account_book.dto.res.AccountBookDto;
import com.uliga.uliga_backend.domain.account_book.dto.res.AccountBookInvitationDto;
import com.uliga.uliga_backend.global.common.annotation.Serialize;
import com.uliga.uliga_backend.global.util.SecurityUtil;
import com.uliga.uliga_backend.jooq.tables.pojos.AccountBook;
import com.uliga.uliga_backend.jooq.tables.pojos.AccountBookInvitation;

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
  @PostMapping("/invite")
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
}
