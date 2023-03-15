package com.uliga.uliga_backend.domain.AccountBook.dao;

import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookCategoryInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookMemberInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MembersQ;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountBookRepository extends JpaRepository<AccountBook, Long> {

    @Query("select new com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookInfoQ(" +
            "abm.accountBook.id, " +
            "ab.isPrivate, " +
            "ab.name, " +
            "abm.accountBookAuthority, " +
            "abm.getNotification," +
            "ab.relationShip) from " +
            "AccountBook ab join AccountBookMember abm on ab.id = abm.accountBook.id " +
            "WHERE abm.member.id=:id ORDER BY ab.isPrivate DESC")
    List<AccountBookInfoQ> findAccountBookInfosByMemberId(@Param("id") Long id);
    @Query("select new com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookInfoQ(" +
            "abm.accountBook.id, " +
            "ab.isPrivate, " +
            "ab.name, " +
            "abm.accountBookAuthority, " +
            "abm.getNotification," +
            "ab.relationShip) from " +
            "AccountBook ab join AccountBookMember abm on ab.id = abm.accountBook.id WHERE ab.id=:id and abm.member.id = :memberId")
    AccountBookInfoQ findAccountBookInfoById(@Param("id") Long id, @Param("memberId") Long memberId);
    @Query("select new com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookMemberInfoQ(" +
            "m.id, " +
            "m.nickName," +
            "abm.accountBookAuthority) " +
            "FROM " +
            "AccountBook ab join AccountBookMember abm on ab.id = abm.accountBook.id " +
            "join Member m on abm.member.id = m.id " +
            "WHERE abm.accountBook.id=:id")
    List<AccountBookMemberInfoQ> findAccountBookMemberInfoById(@Param("id") Long id);
    @Query("select new com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookCategoryInfoQ(" +
            "c.id," +
            "c.name)" +
            "FROM " +
            "AccountBook ab JOIN Category c on c.accountBook.id = ab.id WHERE ab.id = :id")
    List<AccountBookCategoryInfoQ> findAccountBookCategoryInfoById(@Param("id") Long id);
    @Query("SELECT ab " +
            "FROM AccountBook ab " +
            "JOIN AccountBookMember abm ON abm.accountBook.id = ab.id " +
            "WHERE abm.member.id=:id")
    List<AccountBook> findAccountBooksByMemberId(@Param("id") Long id);
    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MembersQ(" +
            "COUNT(abm.member.id)" +
            ") FROM AccountBook ab " +
            "JOIN AccountBookMember abm ON abm.accountBook.id = ab.id " +
            "WHERE ab.id=:id GROUP BY ab.id")
    MembersQ getMemberNumberByAccountBookId(@Param("id") Long id);

    void deleteById(Long id);


}
