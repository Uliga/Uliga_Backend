package com.uliga.uliga_backend.domain.AccountBook.dao;

import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQuery.AccountBookInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountBookRepository extends JpaRepository<AccountBook, Long> {

    @Query("select new com.uliga.uliga_backend.domain.AccountBook.dto.NativeQuery.AccountBookInfoQ(abm.accountBook.id, ab.isPrivate, ab.name, abm.accountBookAuthority, abm.getNotification) from " +
            "AccountBook ab join AccountBookMember abm on ab.id = abm.accountBook.id WHERE abm.member.id=:id")
    List<AccountBookInfoQ> findAccountBookInfosByMemberId(@Param("id") Long id);
}
