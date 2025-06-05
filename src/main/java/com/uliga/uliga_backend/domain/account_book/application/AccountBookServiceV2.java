package com.uliga.uliga_backend.domain.account_book.application;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.domain.account_book.dto.req.CreateAccountBookDto;
import com.uliga.uliga_backend.domain.account_book.dto.req.CreateInvitationDto;
import com.uliga.uliga_backend.domain.account_book.dto.req.InvitationReplyDto;
import com.uliga.uliga_backend.domain.account_book.dto.req.UpdateAccountBookDto;
import com.uliga.uliga_backend.domain.account_book.repository.AccountBookRepository;
import com.uliga.uliga_backend.jooq.tables.pojos.AccountBook;
import com.uliga.uliga_backend.jooq.tables.pojos.AccountBookInvitation;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountBookServiceV2 {
  private final AccountBookRepository accountBookRepository;

  public Mono<AccountBook> getAccountBook(Long accountBookId) {
    return accountBookRepository.findById(accountBookId);
  }

  public Flux<AccountBook> getMemberAccountBooks(Long memberId) {
    return accountBookRepository.findAllByMemberId(memberId);
  }

  public Mono<AccountBook> getAccountBookDetail(Long memberId, Long accountBookId) {

  }

  public Mono<AccountBook> createAccountBook(CreateAccountBookDto dto) {

  }

  public Mono<AccountBook> updateAccountBook(UpdateAccountBookDto dto) {

  }

  public Mono<AccountBookInvitation> createInvitation(CreateInvitationDto dto) {

  }

  public Mono<AccountBookInvitation> replyToInvitation(InvitationReplyDto dto) {

  }
}
