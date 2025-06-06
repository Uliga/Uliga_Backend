package com.uliga.uliga_backend.member.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.uliga.uliga_backend.member.model.Member;

@Repository
public interface MemberReactiveRepository extends ReactiveCrudRepository<Member, Long>, MemberCustomRepository {

}
