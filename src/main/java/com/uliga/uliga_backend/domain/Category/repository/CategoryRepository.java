package com.uliga.uliga_backend.domain.category.repository;

import com.uliga.uliga_backend.domain.account_book.model.AccountBook;
import com.uliga.uliga_backend.domain.category.dto.NativeQ.AccountBookCategoryAnalyzeQ;
import com.uliga.uliga_backend.domain.category.dto.NativeQ.AccountBookCategoryInfoQ;
import com.uliga.uliga_backend.domain.category.model.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;


        
                        select ne
                        "c.id," +
                        "c.name)"
                        "FROM " +
                "AccountBook ab JOIN Category c on c.accountBook.id = ab.id WHERE ab.id = :id

        
                        select ne
                        "c.id," +
                        "c.name)"
                        "FROM " +
                "AccountBook ab JOIN Category c on c.accountBook.id = ab.id WHERE ab.id = :i

        List<AccountBookCategoryInfoQ> findAccountBookCategoryAnaly

        
                        SELECT NEW
                        "c.id, " +
                        "c.name, " +
                        "SUM(r.value)) " +
                        "FROM Category c " +
                        "JOIN Record r ON r.category.id = 
                        "WHERE c.accountBook.id = :i
                        "AND r.date.year = :year " +
                        "AND r.date.mon
                        "group by c " +
                "order by SUM(r.value) DESC LIMIT 5")
                        

        
                        alue = "SEL
                        "c.name " +
                        "FROM category c " +
                        "JOIN account_book_data r ON r.cat
                        "WHERE c.account_book_i
                        "AND r.year = :year " +
                        "AND r.month = :month " +
                        "group by r.category_id " +
                "order by SUM(r.value) DESC LIMIT 5, 100", nativeQuery = true)
                        

        

        

        
        @Query("SELECT c.name FROM Category c WHERE c.accountBook.id = :accountBookId")

        

        

        

        List<Category> findCategoriesByAccountBookId(Long accountBookId);
        @Query("SELECT c FROM Category c where c.accountBook.id in (:accountBookIds) AND c.name = '기타'")

        
                        SELECT c " +
                        "From Category c " +
                        "join AccountBookMember abm 
                        "WHERE abm.member.i
                "AND c.name=:name")
    List<Category> findCategoriesByMemberIdAndName(@Param("id") Long memberId, @Param("name") String name);
}
