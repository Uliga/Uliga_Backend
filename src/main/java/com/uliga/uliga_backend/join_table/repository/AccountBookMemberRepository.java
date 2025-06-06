package com.uliga.uliga_backend.join_table.repository;

import java.util.Optional;

import com.uliga.uliga_backend.join_table.model.AccountBookMember;

public interface AccountBookMemberRepository {
  boolean existsAccountBookMemberByMemberIdAndAccountBookId(Long memberId, Long accountBookId);

  void deleteAllByMemberId(Long memberId);

  void deleteAccountBookMemberByAccountBookIdAndMemberId(Long accountBookId, Long memberId);

  Optional<AccountBookMember> findAccountBookMemberByMemberIdAndAccountBookId(Long memberId, Long accountBookId);

}
