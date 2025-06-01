package com.uliga.uliga_backend.domain.income.repository;

import com.uliga.uliga_backend.domain.account_book_data.dto.NativeQ.DailyValueQ;
import com.uliga.uliga_backend.domain.account_book_data.dto.NativeQ.MonthlySumQ;
import com.uliga.uliga_backend.domain.income.dto.NativeQ.IncomeInfoQ;
import com.uliga.uliga_backend.domain.income.model.Income;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


        
                        SELECT NEW com.u
                        "i.date.day, " +
                        "SUM(i.value)) " +
                        "FROM Income i " +
                        "WHERE i.accountBook.id = :i
                        "AND i.date.year = :year " +
                        "AND i.date.month = :month " +
                        "GROUP BY i.date.year, i.d
                "ORDER BY i.date.day ASC")
                        OfMonth(@Param("id") Long
                        @Param("year") Long year,

        
                        select new com.u
                        "SUM(i.value)" +
                        ") FROM AccountBook ab " +
                        "JOIN Income i ON i.accountBoo
                        "WHERE i.date.mont
                        "AND ab.id=:id " +
                "AND i.date.year = :year GROUP BY ab.id")
                        

        
                        select ne
                        "i.id," +
                        "i.value," +
                        "i.payment," +
                        "i.account,
                        "i.memo," +
                        "i.date.year," +
                        "i.date.month,"
                        "i.date.day," +
                        "m.userName
                        "c.name," +
                        "abm.avatarUrl) from Income i " +
                        "JOIN Member m on m.id = i.creator.id " +
                        
                        "JOIN Category c on c.id = i.category.id JOIN AccountBookMember abm ON abm.acco
                "WHERE m.id=:id ORDER BY i.date.year*365 + i.date.month*31 + i.date.

        
        @Modifying
        @Query(nativeQuery = true, value = "DELETE FROM income where

        
        @Modifying
        @Query(nativeQuery = true, value = "INSERT INTO income (account_book_data_id) value (:id)")
    void createFromAccountBookDataId(@Param("id") Long id);
}
