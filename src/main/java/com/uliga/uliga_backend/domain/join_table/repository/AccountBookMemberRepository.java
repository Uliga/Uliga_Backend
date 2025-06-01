package com.uliga.uliga_backend.domain.join_table.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uliga.uliga_backend.domain.join_table.model.AccountBookMember;

public interface AccountBookMemberRepository extends JpaRepository<AccountBookMember, Long> {
    boolean existsAccountBookMemberByMemberIdAndAccountBookId(Long memberId, Long accountBookId);

    void deleteAllByMemberId(Long memberId);

    void deleteAccountBookMemberByAccountBookIdAndMemberId(Long accountBookId, Long memberId);

    Optional<AccountBookMember> findAccountBookMemberByMemberIdAndAccountBookId(Long memberId, Long accountBookId);

}
