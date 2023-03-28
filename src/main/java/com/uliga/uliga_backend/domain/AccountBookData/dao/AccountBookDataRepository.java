package com.uliga.uliga_backend.domain.AccountBookData.dao;

import com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.AccountBookDataQ;
import com.uliga.uliga_backend.domain.AccountBookData.model.AccountBookData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountBookDataRepository extends JpaRepository<AccountBookData, Long> {
    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.AccountBookDataQ(abd.id, abd.value, abd.payment, abd.account, abd.memo, abd.date.year, abd.date.month, abd.date.day, abd.type, abd.creator.userName, abd.category.name) FROM AccountBookData abd LEFT JOIN Income i on i.id = abd.id LEFT JOIN Record r ON r.id = abd.id")
    Page<AccountBookDataQ> findAccountBookDataByAccountBookId(@Param("id") Long id, Pageable pageable);
    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.AccountBookDataQ(abd.id, abd.value, abd.payment, abd.account, abd.memo, abd.date.year, abd.date.month, abd.date.day, abd.type, abd.creator.userName, abd.category.name) FROM AccountBookData abd LEFT JOIN Income i on i.id = abd.id LEFT JOIN Record r ON r.id = abd.id")
    Page<AccountBookDataQ> findAccountBookDataByAccountBookIdAndYear(@Param("id") Long id, @Param("year") Long year, Pageable pageable);
    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.AccountBookDataQ(abd.id, abd.value, abd.payment, abd.account, abd.memo, abd.date.year, abd.date.month, abd.date.day, abd.type, abd.creator.userName, abd.category.name) FROM AccountBookData abd LEFT JOIN Income i on i.id = abd.id LEFT JOIN Record r ON r.id = abd.id")
    Page<AccountBookDataQ> findAccountBookDataByAccountBookIdAndYearAndMonth(@Param("id") Long id, @Param("year") Long year, @Param("month") Long month, Pageable pageable);
}

