package com.uliga.uliga_backend.domain.Member.application;

import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.exception.EmailCertificationExpireException;
import jakarta.mail.Message;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.uliga.uliga_backend.global.common.constants.EmailConstants.EMAIL_CERTIFICATION_TIME;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailCertificationService {
    private final JavaMailSender emailSender;
    private final RedisTemplate<String, String> redisTemplate;
    // 인증 번호
    private String ePw;

    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        mimeMessage.addRecipients(RecipientType.TO, to);
        mimeMessage.setSubject("Uliga 이메일 인증");

        String msg = "";
        msg += "<div style = 'margin:100px;'>";
        msg += "<h1> 안녕하세요 </h1>";
        msg += "<h1> 공유 가계부 우리가 입니다.</h1>";
        msg += "<br>";
        msg += "<p>아래 코드를 앱으로 돌아가서 입력해주세요</p>";
        msg += "<br>";
        msg += "<div align='center' style = 'border:1px solid black; font-family:verdana';>";
        msg += "<h3 style = 'color:blue;'>회원가입 인증 코드입니다.</h3>";
        msg += "<div style='font-style:130%'>";
        msg += "CODE: <strong>";
        msg += ePw + "</strong><div><br/>";
        msg += "</div>";
        mimeMessage.setText(msg, "utf-8", "html");
        mimeMessage.setFrom(new InternetAddress("uliga_dev_team@naver.com", "ULIGA_admin"));
        return mimeMessage;
    }

    public String createKey() {
        StringBuilder key = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            key.append((random.nextInt(10)));
        }
        return key.toString();
    }

    // 메일 발송
    public void sendSimpleMessage(String to) throws Exception {
        ePw = createKey();
        MimeMessage message = createMessage(to);
        try {
            emailSender.send(message);
            log.info("secret code = " + ePw);
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(to, ePw);
            redisTemplate.expire(to, EMAIL_CERTIFICATION_TIME, TimeUnit.MILLISECONDS);
        } catch (MailException es) {
            log.info(es.getLocalizedMessage());
            throw new IllegalArgumentException(es.getMessage());
        }

    }

    // 코드 검증
    public MemberDTO.CodeConfirmDto confirmCode(MemberDTO.EmailConfirmCodeDto emailConfirmCodeDto) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String code = valueOperations.get(emailConfirmCodeDto.getEmail());
        if (code == null) {
            throw new EmailCertificationExpireException();
        }
        if (!code.equals(emailConfirmCodeDto.getCode())) {
            return MemberDTO.CodeConfirmDto.builder().matches(false).build();
        }
        valueOperations.getAndDelete(emailConfirmCodeDto.getEmail());
        return MemberDTO.CodeConfirmDto.builder().matches(true).build();

    }
}
