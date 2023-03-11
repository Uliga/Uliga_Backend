package com.uliga.uliga_backend.domain.Member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.Member.application.AuthService;
import com.uliga.uliga_backend.domain.Member.application.MemberService;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest({MemberController.class, MemberAuthController.class})
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith({SpringExtension.class})
class MemberAuthControllerTest {
    @MockBean
    private MemberService memberService;
    @MockBean
    private AuthService authService;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private MemberRepository memberRepository;

}