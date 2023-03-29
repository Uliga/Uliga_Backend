package com.uliga.uliga_backend.domain.JoinTable.dao;

import com.uliga.uliga_backend.domain.JoinTable.model.AccountBookMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccountBookMemberRepository extends JpaRepository<AccountBookMember, Long>{
    boolean existsAccountBookMemberByMemberIdAndAccountBookId(Long memberId, Long accountBookId);

}
