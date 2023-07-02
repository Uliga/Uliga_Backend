package com.uliga.uliga_backend.domain.AccountBook.dao;

import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.*;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface AccountBookRepository extends JpaRepository<AccountBook, Long>{

    @Query("select new com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookInfoQ(" +
            "abm.accountBook.id, " +
            "ab.isPrivate, " +
            "ab.name, " +
            "abm.accountBookAuthority, " +
            "abm.getNotification," +
            "ab.relationShip, " +
            "abm.avatarUrl) from " +
            "AccountBook ab join AccountBookMember abm on ab.id = abm.accountBook.id " +
            "WHERE abm.member.id=:id ORDER BY ab.isPrivate DESC")
    List<AccountBookInfoQ> findAccountBookInfosByMemberId(@Param("id") Long id);
    @Query("select new com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookInfoQ(" +
            "abm.accountBook.id, " +
            "ab.isPrivate, " +
            "ab.name, " +
            "abm.accountBookAuthority, " +
            "abm.getNotification," +
            "ab.relationShip, " +
            "abm.avatarUrl) from " +
            "AccountBook ab join AccountBookMember abm on ab.id = abm.accountBook.id WHERE ab.id=:id and abm.member.id = :memberId")
    AccountBookInfoQ findAccountBookInfoById(@Param("id") Long id, @Param("memberId") Long memberId);
    @Query("select new com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookMemberInfoQ(" +
            "m.id, " +
            "m.userName," +
            "abm.accountBookAuthority, " +
            "abm.avatarUrl, " +
            "m.email) " +
            "FROM " +
            "AccountBook ab join AccountBookMember abm on ab.id = abm.accountBook.id " +
            "join Member m on abm.member.id = m.id " +
            "WHERE abm.accountBook.id=:id AND m.deleted=false ")
    List<AccountBookMemberInfoQ> findAccountBookMemberInfoById(@Param("id") Long id);



    @Query("SELECT ab " +
            "FROM AccountBook ab " +
            "JOIN AccountBookMember abm ON abm.accountBook.id = ab.id " +
            "WHERE abm.member.id=:id")
    List<AccountBook> findAccountBooksByMemberId(@Param("id") Long id);
    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MembersQ(" +
            "COUNT(abm.member.id)" +
            ") FROM AccountBook ab " +
            "JOIN AccountBookMember abm ON abm.accountBook.id = ab.id " +
            "JOIN Member m ON abm.member.id = m.id " +
            "WHERE abm.accountBook.id=:id AND m.deleted = false GROUP BY ab.id")
    MembersQ getMemberNumberByAccountBookId(@Param("id") Long id);

    void deleteById(Long id);

    @Query("SELECT ab FROM AccountBook ab where ab.id in (:accountBookIds)")
    List<AccountBook> findAccountBookByAccountBookIds(@Param("accountBookIds") List<Long> accountBookIds);


}
