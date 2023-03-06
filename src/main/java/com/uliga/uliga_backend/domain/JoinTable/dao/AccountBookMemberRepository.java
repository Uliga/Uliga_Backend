package com.uliga.uliga_backend.domain.JoinTable.dao;

import com.uliga.uliga_backend.domain.JoinTable.model.AccountBookMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBookMemberRepository extends JpaRepository<AccountBookMember, Long> {
}
