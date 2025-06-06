package com.uliga.uliga_backend.member.repository;

public interface MemberRepository {

  // Optional<Member> findByEmailAndDeletedAndUserLoginType(String email, Boolean
  // deleted, UserLoginType loginType);

  // Optional<Member> findByEmailAndDeleted(String email, Boolean deleted);

  // boolean existsByEmailAndDeleted(String email, Boolean deleted);

  // boolean existsByNickNameAndDeleted(String nickname, Boolean deleted);

  // @Query("select new
  // com.uliga.uliga_backend.domain.Member.dto.NativeQ.MemberInfoNativeQ(" +
  // "m.id, " +
  // "m.privateAccountBook.id," +
  // "m.userName," +
  // " m.nickName, " +
  // "m.email) from Member m where m.id = :id")
  // MemberInfoNativeQ findMemberInfoById(@Param("id") Long id);

  // @Query("SELECT m " +
  // "FROM AccountBook ab " +
  // "JOIN AccountBookMember abm ON ab.id = abm.accountBook.id " +
  // "JOIN Member m ON abm.member.id = m.id " +
  // "WHERE ab.id = :id")
  // List<Member> findMemberByAccountBookId(@Param("id") Long id);
}
