package com.uliga.uliga_backend.account_book.service;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.account_book.dto.req.AccountBookRecordQueryDto;
import com.uliga.uliga_backend.account_book.dto.req.CreateAccountBookDto;
import com.uliga.uliga_backend.account_book.dto.req.CreateAccountBookRecordDto;
import com.uliga.uliga_backend.account_book.dto.req.CreateInvitationDto;
import com.uliga.uliga_backend.account_book.dto.req.DeleteAccountBookRecordDto;
import com.uliga.uliga_backend.account_book.dto.req.InvitationReplyDto;
import com.uliga.uliga_backend.account_book.dto.req.UpdateAccountBookDto;
import com.uliga.uliga_backend.account_book.dto.res.AccountBookRecordDto;
import com.uliga.uliga_backend.account_book.repository.AccountBookRepository;
import com.uliga.uliga_backend.account_book_user.dto.req.DeleteAccountBookUserDto;
import com.uliga.uliga_backend.common.dto.req.OrderByQuery;
import com.uliga.uliga_backend.common.dto.req.PaginateQuery;
import com.uliga.uliga_backend.common.dto.res.CountDto;
import com.uliga.uliga_backend.common.dto.res.PaginatedDto;
import com.uliga.uliga_backend.jooq.tables.pojos.AccountBook;
import com.uliga.uliga_backend.jooq.tables.pojos.AccountBookInvitation;
import com.uliga.uliga_backend.jooq.tables.pojos.AccountBookUser;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountBookServiceV2 {
  private final AccountBookRepository accountBookRepository;

  public Mono<AccountBook> getAccountBook(Long accountBookId) {
    // return accountBookRepository.findById(accountBookId);
    throw new UnsupportedOperationException();
  }

  public Flux<AccountBook> getMemberAccountBooks(Long memberId) {
    // return accountBookRepository.findAllByMemberId(memberId);
    throw new UnsupportedOperationException();
  }

  public Mono<AccountBook> getAccountBookDetail(Long memberId, Long accountBookId) {
    throw new UnsupportedOperationException();
  }

  public Mono<AccountBook> createAccountBook(CreateAccountBookDto dto) {
    throw new UnsupportedOperationException();
  }

  public Mono<AccountBook> updateAccountBook(UpdateAccountBookDto dto) {
    throw new UnsupportedOperationException();
  }

  public Mono<AccountBookInvitation> createInvitation(CreateInvitationDto dto) {
    throw new UnsupportedOperationException();
  }

  public Mono<AccountBookInvitation> replyToInvitation(InvitationReplyDto dto) {
    throw new UnsupportedOperationException();
  }

  public Mono<CountDto> deleteAccountBookItems(DeleteAccountBookRecordDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteAccountBookItems'");
  }

  public Mono<CountDto> createAccountBookItems(CreateAccountBookRecordDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createAccountBookItems'");
  }

  public Flux<AccountBookUser> getAccountBookUsers(Long accountBookId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAccountBookUsers'");
  }

  public Mono<AccountBook> deleteAccountBook(Long accountBookId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteAccountBook'");
  }

  public Mono<PaginatedDto<AccountBookRecordDto>> getAccountBookRecords(AccountBookRecordQueryDto query,
      PaginateQuery paginate, OrderByQuery orderBy) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAccountBookRecords'");

    /*
     * // jOOQ로 Expense, Revenue 조회
     * List<Expense> expenseList = dsl
     * .selectFrom(EXPENSE)
     * .fetchInto(Expense.class);
     * 
     * List<Revenue> revenueList = dsl
     * .selectFrom(REVENUE)
     * .fetchInto(Revenue.class);
     * 
     * // Expense → DTO
     * List<AccountBookRecordDto> expenseDtos = expenseList.stream()
     * .map(e -> new AccountBookRecordDto(
     * e.getId(),
     * e.getCreatedAt(),
     * e.getAmount(),
     * AccountBookRecordDto.RecordType.EXPENSE
     * ))
     * .collect(Collectors.toList());
     * 
     * // Revenue → DTO
     * List<AccountBookRecordDto> revenueDtos = revenueList.stream()
     * .map(r -> new AccountBookRecordDto(
     * r.getId(),
     * r.getCreatedAt(),
     * r.getAmount(),
     * AccountBookRecordDto.RecordType.REVENUE
     * ))
     * .collect(Collectors.toList());
     * 
     * // 합친 뒤 생성일 기준 정렬
     * List<AccountBookRecordDto> combined = Stream
     * .concat(expenseDtos.stream(), revenueDtos.stream())
     * .sorted(Comparator.comparing(AccountBookRecordDto::getCreatedAt))
     * .collect(Collectors.toList());
     * 
     * return combined;
     */
  }

  public Mono<AccountBookUser> deleteAccountBookUser(DeleteAccountBookUserDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteAccountBookUser'");
  }
}
