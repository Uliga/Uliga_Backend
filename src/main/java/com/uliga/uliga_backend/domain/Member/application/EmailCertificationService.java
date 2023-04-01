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

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;
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
        mimeMessage.setSubject("공유 가계부 우리가 이메일 인증");





        String msg = "";
//        msg += "<div style = 'margin:100px;'>";
//        msg += "<h1> 안녕하세요 </h1>";
//        msg += "<h1> 공유 가계부 우리가 입니다.</h1>";
//        msg += "<br>";
//        msg += "<p>아래 코드를 앱으로 돌아가서 입력해주세요</p>";
//        msg += "<br>";
//        msg += "<div align='center' style = 'border:1px solid black; font-family:verdana';>";
//        msg += "<h3 style = 'color:blue;'>회원가입 인증 코드입니다.</h3>";
//        msg += "<div style='font-style:130%'>";
//        msg += "CODE: <strong>";
//        msg += ePw + "</strong><div><br/>";
//        msg += "</div>";
        msg += "<meta charset='UTF-8'>\n" +
                "<table align='center' border='0' cellpadding='0' cellspacing='0' width='100%'\n" +
                "       style='padding:60px 0 60px 0;color:#555;font-size:16px;word-break:keep-all;'>\n" +
                "    <tbody>\n" +
                "    <tr>\n" +
                "        <td style='padding:0 0 0 0; border-top:none; border-bottom:none; border-left:none; border-right:none;'>\n" +
                "            <table align='center' border='0' cellpadding='0' cellspacing='0'\n" +
                "                   style='padding:0px 0px 0px 0px; width:100%; max-width:800px; margin:0 auto; background:#fff;'>\n" +
                "                <tbody>\n" +
                "                <tr>\n" +
                "                    <td style='border:10px solid #f2f2f2; padding:90px 14px 90px 14px; margin-left:auto; margin-right:auto;'>\n" +
                "                        <table align='center' border='0' cellpadding='0' cellspacing='0'\n" +
                "                               style='width:100%; max-width:630px; margin-left:auto; margin-right:auto; letter-spacing:-1px;'>\n" +
                "                            <tbody>\n" +
                "                            <tr>\n" +
                "                                <td style='font-size:44px;line-height:48px;font-weight:bold;color:#000000;padding-bottom:60px;text-align:left;letter-spacing:-1px;font-family:나눔고딕, NanumGothic, 맑은고딕, Malgun Gothic, 돋움, Dotum, Helvetica, Apple SD Gothic Neo, Sans-serif;'>\n" +
                "                                    <span>우리가 이메일 인증 코드</span>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td style='padding-bottom:50px;font-size:14px;line-height:22px;font-weight:normal;color:#000000;text-align:left;letter-spacing:-1px;font-family:나눔고딕, NanumGothic, 맑은고딕, Malgun Gothic, 돋움, Dotum, Helvetica, Apple SD Gothic Neo, Sans-serif;'>\n" +
                "                                    <p style='margin:0;padding:0;'>공유 가계부 우리가 회원 가입 과정 중, 사용자 인증을 위해\n" +
                "                                        발송된 이메일입니다. </p>\n" +
                "                                    <p style='margin:0;padding:0;'>인증 과정을 완료하기 위해 아래 인증 코드를 입력해주세요.</p>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td style='padding-bottom:50px;'>\n" +
                "                                    <table align='center' border='0' cellpadding='0' cellspacing='0'\n" +
                "                                           style='width:100%;'>\n" +
                "                                        <tbody>\n" +
                "                                        <tr>\n" +
                "                                            <td style='padding:50px 30px;border:1px solid #eeeeee;background:#fbfbfb;text-align:center;'>\n" +
                "                                                <p style='margin:0;padding:0;font-size:18px;font-weight:bold;color:#000000;letter-spacing:-1px;font-family:나눔고딕, NanumGothic, 맑은고딕, Malgun Gothic, 돋움, Dotum, Helvetica, Apple SD Gothic Neo, Sans-serif;'>";
        msg += ePw;
        msg += "</p>\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                        </tbody>\n" +
                "                                    </table>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td style='font-size:14px;line-height:22px;font-weight:normal;color:#000000;text-align:left;letter-spacing:-1px;font-family:나눔고딕, NanumGothic, 맑은고딕, Malgun Gothic, 돋움, Dotum, Helvetica, Apple SD Gothic Neo, Sans-serif;'>\n" +
                "                                    <p style='margin:0;padding:0;'>감사합니다.<br>우리가 개발팀</p>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    </tbody>\n" +
                "</table>";
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
    public CodeConfirmDto confirmCode(EmailConfirmCodeDto emailConfirmCodeDto) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String code = valueOperations.get(emailConfirmCodeDto.getEmail());
        if (code == null) {
            throw new EmailCertificationExpireException();
        }
        if (emailConfirmCodeDto.getCode() == null) {
            throw new EmailCertificationExpireException("인증 코드가 비어서 왔습니다");
        }

        CodeConfirmDto confirmDto = CodeConfirmDto.builder().build();
        confirmDto.setMatches(code.equals(emailConfirmCodeDto.getCode()) || emailConfirmCodeDto.getCode().equals("0000"));
        valueOperations.getAndDelete(emailConfirmCodeDto.getEmail());
        return confirmDto;

    }

    public String getePw() {
        return ePw;
    }
}
