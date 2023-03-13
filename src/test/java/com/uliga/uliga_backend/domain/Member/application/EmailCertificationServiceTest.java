package com.uliga.uliga_backend.domain.Member.application;

import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.exception.EmailCertificationExpireException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class EmailCertificationServiceTest {
    @Autowired
    EmailCertificationService emailCertificationService;

    public EmailConfirmCodeDto createEmailConfirmCode(String email, String code) {
        return EmailConfirmCodeDto.builder()
                .email(email)
                .code(code).build();
    }
    @Test
    @DisplayName("이메일 전송 성공 테스트")
    public void sendEmailTestToSuccess() throws Exception{
        //given
        emailCertificationService.sendSimpleMessage("dongjunkim99@icloud.com");


        // when
        EmailConfirmCodeDto emailConfirmCode = createEmailConfirmCode("dongjunkim99@icloud.com", emailCertificationService.getePw());
        CodeConfirmDto codeConfirmDto = emailCertificationService.confirmCode(emailConfirmCode);
        // then
        assertTrue(codeConfirmDto.isMatches());
    }

    @Test
    @DisplayName("이메일 코드 검증 실패 테스트 - 레디스에 없는 이메일")
    public void sendEmailTestToFailByInvalidEmail() throws Exception{
        //given
        emailCertificationService.sendSimpleMessage("dongjunkim99@icloud.com");

        // when
        EmailConfirmCodeDto emailConfirmCode = createEmailConfirmCode("dongjunkim992@icloud.com", "aaaaaa");
        // then
        assertThrows(EmailCertificationExpireException.class, () -> emailCertificationService.confirmCode(emailConfirmCode));
    }

    @Test
    @DisplayName("이메일 코드 검증 실패 테스트 - 잘못된 코드")
    public void sendEmailTestToFailByInvalidCode() throws Exception{
        //given
        emailCertificationService.sendSimpleMessage("dongjunkim99@icloud.com");


        // when
        EmailConfirmCodeDto emailConfirmCode = createEmailConfirmCode("dongjunkim99@icloud.com", "aaaaaa");
        // then
        assertFalse(emailCertificationService.confirmCode(emailConfirmCode).isMatches());

    }
}