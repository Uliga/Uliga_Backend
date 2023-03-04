package com.uliga.uliga_backend.domain.Member.dao;

import com.uliga.uliga_backend.domain.Member.dto.NativeQuery.MemberInfoNativeQ;
import com.uliga.uliga_backend.domain.Member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickName(String nickname);

    @Query("select new com.uliga.uliga_backend.domain.Member.dto.NativeQuery.MemberInfoNativeQ(m.id, m.avatarUrl, m.userName, m.nickName, m.email) from Member m where m.id = :id")
    MemberInfoNativeQ findMemberInfoById(@Param("id") Long id);
}
