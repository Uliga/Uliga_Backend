package com.uliga.uliga_backend.service;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.common.dto.req.OrderByQuery;
import com.uliga.uliga_backend.common.dto.req.PaginateQuery;
import com.uliga.uliga_backend.common.dto.res.CountDto;
import com.uliga.uliga_backend.common.dto.res.PaginatedDto;
import com.uliga.uliga_backend.dto.account_book.req.AccountBookRecordQueryDto;
import com.uliga.uliga_backend.dto.account_book.req.CreateAccountBookDto;
import com.uliga.uliga_backend.dto.account_book.req.CreateAccountBookRecordDto;
import com.uliga.uliga_backend.dto.account_book.req.CreateInvitationDto;
import com.uliga.uliga_backend.dto.account_book.req.DeleteAccountBookRecordDto;
import com.uliga.uliga_backend.dto.account_book.req.InvitationReplyDto;
import com.uliga.uliga_backend.dto.account_book.req.UpdateAccountBookDto;
import com.uliga.uliga_backend.dto.account_book.res.AccountBookRecordDto;
import com.uliga.uliga_backend.dto.account_book_user.req.DeleteAccountBookUserDto;
import com.uliga.uliga_backend.entity.AccountBookEntity;
import com.uliga.uliga_backend.entity.AccountBookInvitationEntity;
import com.uliga.uliga_backend.entity.AccountBookUserEntity;
import com.uliga.uliga_backend.repository.AccountBookRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountBookServiceV2 {
  private final AccountBookRepository accountBookRepository;

  public Mono<AccountBookEntity> getAccountBook(Long accountBookId) {
    // return accountBookRepository.findById(accountBookId);
    throw new UnsupportedOperationException();
  }

  public Flux<AccountBookEntity> getMemberAccountBooks(Long memberId) {
    // return accountBookRepository.findAllByMemberId(memberId);
    throw new UnsupportedOperationException();
  }

  public Mono<AccountBookEntity> getAccountBookDetail(Long memberId, Long accountBookId) {
    throw new UnsupportedOperationException();
  }

  public Mono<AccountBookEntity> createAccountBook(CreateAccountBookDto dto) {
    throw new UnsupportedOperationException();
  }

  public Mono<AccountBookEntity> updateAccountBook(UpdateAccountBookDto dto) {
    throw new UnsupportedOperationException();
  }

  public Mono<AccountBookInvitationEntity> createInvitation(CreateInvitationDto dto) {
    throw new UnsupportedOperationException();
  }

  public Mono<AccountBookInvitationEntity> replyToInvitation(InvitationReplyDto dto) {
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

  public Flux<AccountBookUserEntity> getAccountBookUsers(Long accountBookId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAccountBookUsers'");
  }

  public Mono<AccountBookEntity> deleteAccountBook(Long accountBookId) {
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

  public Mono<AccountBookUserEntity> deleteAccountBookUser(DeleteAccountBookUserDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteAccountBookUser'");
  }
}
