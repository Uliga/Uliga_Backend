package com.uliga.uliga_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uliga.uliga_backend.common.annotation.Serialize;
import com.uliga.uliga_backend.dto.account_book.req.CreateInvitationDto;
import com.uliga.uliga_backend.dto.account_book.req.InvitationReplyDto;
import com.uliga.uliga_backend.dto.account_book.res.AccountBookInvitationDto;
import com.uliga.uliga_backend.entity.AccountBookInvitationEntity;
import com.uliga.uliga_backend.service.AccountBookInvitationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Tag(name = "가계부 초대 API")
@RestController
@RequestMapping("v2/account-book-invitation")
@RequiredArgsConstructor
public class AccountBookInvitationController {
  private final AccountBookInvitationService accountBookInvitationService;

  @Operation(summary = "가계부 멤버 초대 API")
  @PostMapping()
  @Serialize(dto = AccountBookInvitationDto.class)
  public ResponseEntity<Mono<AccountBookInvitationEntity>> inviteUser(@RequestBody CreateInvitationDto dto) {
    return ResponseEntity.ok(accountBookInvitationService.createInvitation(dto));
  }

  @Operation(summary = "가계부 초대 응답 API")
  @PostMapping("/reply")
  @Serialize(dto = AccountBookInvitationDto.class)
  public ResponseEntity<Mono<AccountBookInvitationEntity>> replyToInvitation(@RequestBody InvitationReplyDto dto) {
    return ResponseEntity.ok(accountBookInvitationService.replyToInvitation(dto));
  }
}
