package com.uliga.uliga_backend.domain.Member.dao;

import com.uliga.uliga_backend.domain.Member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
