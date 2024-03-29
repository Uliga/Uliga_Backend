package com.uliga.uliga_backend.domain.JoinTable.repository;

import com.uliga.uliga_backend.domain.JoinTable.model.AccountBookMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AccountBookMemberRepository extends JpaRepository<AccountBookMember, Long>{
    boolean existsAccountBookMemberByMemberIdAndAccountBookId(Long memberId, Long accountBookId);

    void deleteAllByMemberId(Long memberId);

    void deleteAccountBookMemberByAccountBookIdAndMemberId(Long accountBookId, Long memberId);

    Optional<AccountBookMember> findAccountBookMemberByMemberIdAndAccountBookId(Long memberId, Long accountBookId);

}
