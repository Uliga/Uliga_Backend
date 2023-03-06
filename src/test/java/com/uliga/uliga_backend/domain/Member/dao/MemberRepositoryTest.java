package com.uliga.uliga_backend.domain.Member.dao;

import com.uliga.uliga_backend.domain.Member.dto.NativeQuery.MemberInfoNativeQ;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;


    @Test
    @DisplayName("findById - Success")
    void findById() {
        // given
        Long id = 103L;
        // then
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);

        //then
        assertEquals(member.getId(), id);

    }

    @Test
    @DisplayName("findById - Fail")
    public void findByIdFail() throws Exception{
        //given
        assertThrows(NotFoundByIdException.class, () -> memberRepository.findById(102L).orElseThrow(NotFoundByIdException::new));


        // when

        // then
    }

    @Test
    @DisplayName("findMemberInfoById - Success")
    void findMemberInfoById() {
        // given
        Long id = 103L;

        // when
        MemberInfoNativeQ memberInfoById = memberRepository.findMemberInfoById(id);
        // then
        assertEquals(memberInfoById.getId(), id);
    }

}