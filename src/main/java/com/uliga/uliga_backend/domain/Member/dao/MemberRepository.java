package com.uliga.uliga_backend.domain.Member.dao;

import com.uliga.uliga_backend.domain.Member.dto.NativeQ.MemberInfoNativeQ;
import com.uliga.uliga_backend.domain.Member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickName(String nickname);

    @Query("select new com.uliga.uliga_backend.domain.Member.dto.NativeQ.MemberInfoNativeQ(" +
            "m.id, " +
            "m.privateAccountBook.id," +
            "m.avatarUrl, " +
            "m.userName," +
            " m.nickName, " +
            "m.email) from Member m where m.id = :id")
    MemberInfoNativeQ findMemberInfoById(@Param("id") Long id);

    @Query("SELECT Member " +
            "FROM AccountBook ab " +
            "JOIN AccountBookMember abm ON ab.id = abm.accountBook.id " +
            "JOIN Member m ON abm.member.id = m.id " +
            "WHERE ab.id = :id")
    List<Member> findMemberByAccountBookId(@Param("id") Long id);
}
