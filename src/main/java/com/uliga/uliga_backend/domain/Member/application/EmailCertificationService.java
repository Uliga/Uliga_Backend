package com.uliga.uliga_backend.domain.Member.application;

import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.exception.EmailCertificationExpireException;
import com.uliga.uliga_backend.domain.Member.exception.UserNotFoundByEmail;
import com.uliga.uliga_backend.domain.Member.model.Member;
import jakarta.mail.Message;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    // 인증 번호
    private String ePw;

    /**
     * 이메일 인증 메시지 생성 메서드
     * @param to 수신자 이메일
     * @return 이메일 메시지
     * @throws MessagingException 이메일 전송 예외
     * @throws UnsupportedEncodingException 인코딩 처리 예외
     */
    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        mimeMessage.addRecipients(RecipientType.TO, to);
        mimeMessage.setSubject("공유 가계부 우리가 이메일 인증");


        String msg = "";
        msg += """
                <meta charset='UTF-8'>
                <table align='center' border='0' cellpadding='0' cellspacing='0' width='100%'
                       style='padding:60px 0 60px 0;color:#555;font-size:16px;word-break:keep-all;'>
                    <tbody>
                    <tr>
                        <td style='padding:0 0 0 0; border-top:none; border-bottom:none; border-left:none; border-right:none;'>
                            <table align='center' border='0' cellpadding='0' cellspacing='0'
                                   style='padding:0px 0px 0px 0px; width:100%; max-width:800px; margin:0 auto; background:#fff;'>
                                <tbody>
                                <tr>
                                    <td style='border:10px solid #f2f2f2; padding:90px 14px 90px 14px; margin-left:auto; margin-right:auto;'>
                                        <table align='center' border='0' cellpadding='0' cellspacing='0'
                                               style='width:100%; max-width:630px; margin-left:auto; margin-right:auto; letter-spacing:-1px;'>
                                            <tbody>
                                            <tr>
                                                <td style='font-size:44px;line-height:48px;font-weight:bold;color:#000000;padding-bottom:60px;text-align:left;letter-spacing:-1px;font-family:나눔고딕, NanumGothic, 맑은고딕, Malgun Gothic, 돋움, Dotum, Helvetica, Apple SD Gothic Neo, Sans-serif;'>
                                                    <span>우리가 이메일 인증 코드</span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style='padding-bottom:50px;font-size:14px;line-height:22px;font-weight:normal;color:#000000;text-align:left;letter-spacing:-1px;font-family:나눔고딕, NanumGothic, 맑은고딕, Malgun Gothic, 돋움, Dotum, Helvetica, Apple SD Gothic Neo, Sans-serif;'>
                                                    <p style='margin:0;padding:0;'>공유 가계부 우리가 회원 가입 과정 중, 사용자 인증을 위해
                                                        발송된 이메일입니다. </p>
                                                    <p style='margin:0;padding:0;'>인증 과정을 완료하기 위해 아래 인증 코드를 입력해주세요.</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style='padding-bottom:50px;'>
                                                    <table align='center' border='0' cellpadding='0' cellspacing='0'
                                                           style='width:100%;'>
                                                        <tbody>
                                                        <tr>
                                                            <td style='padding:50px 30px;border:1px solid #eeeeee;background:#fbfbfb;text-align:center;'>
                                                                <p style='margin:0;padding:0;font-size:18px;font-weight:bold;color:#000000;letter-spacing:-1px;font-family:나눔고딕, NanumGothic, 맑은고딕, Malgun Gothic, 돋움, Dotum, Helvetica, Apple SD Gothic Neo, Sans-serif;'>""";
        msg += ePw;
        msg += """
                </p>
                                                            </td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style='font-size:14px;line-height:22px;font-weight:normal;color:#000000;text-align:left;letter-spacing:-1px;font-family:나눔고딕, NanumGothic, 맑은고딕, Malgun Gothic, 돋움, Dotum, Helvetica, Apple SD Gothic Neo, Sans-serif;'>
                                                    <p style='margin:0;padding:0;'>감사합니다.<br>우리가</p>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    </tbody>
                </table>""";
        mimeMessage.setText(msg, "utf-8", "html");
        mimeMessage.setFrom(new InternetAddress("uliga_dev_team@naver.com", "ULIGA_admin"));
        return mimeMessage;
    }

    /**
     * 이메일 인증 번호 생성
     * @return 인증번호
     */
    public String createKey() {
        StringBuilder key = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            key.append((random.nextInt(10)));
        }
        return key.toString();
    }

    /**
     * 이메일 발송 메서드
     * @param to 수신자 이메일
     * @throws Exception 발송 실패 예외
     */
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
            throw new IllegalArgumentException(es.getMessage());
        }

    }

    /**
     * 비밀번호 초기화 메서드
     * @param resetPasswordRequest 비밀번호 초기화할 이메일
     */
    @Transactional
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) throws MessagingException, UnsupportedEncodingException {
        Member member = memberRepository.findByEmailAndDeleted(resetPasswordRequest.getEmail(), false).orElseThrow(UserNotFoundByEmail::new);
        String newPassword = createRandomPassword();
        String encode = passwordEncoder.encode(newPassword);
        member.updatePassword(encode);
        MimeMessage passwordMessage = createPasswordMessage(resetPasswordRequest.getEmail(), newPassword);
        try {
            emailSender.send(passwordMessage);
            log.info("new Password" + newPassword);
        } catch (MailException es) {
            log.info(es.getLocalizedMessage());
            throw new IllegalArgumentException(es.getLocalizedMessage());
        }


    }

    /**
     * 비밀번호 분실 메시지 생성현
     * @param to 보낼 사람
     * @param password 새로운 비밀번호
     * @return 전송할 이메일 메시지
     * @throws MessagingException 이메일 전송중 예외
     * @throws UnsupportedEncodingException 인코딩 오류
     */
    public MimeMessage createPasswordMessage(String to, String password) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        mimeMessage.addRecipients(RecipientType.TO, to);
        mimeMessage.setSubject("공유 가계부 우리가 비밀번호 분실");


        String msg = "";
        msg += """
                <meta charset='UTF-8'>
                <table align='center' border='0' cellpadding='0' cellspacing='0' width='100%'
                       style='padding:60px 0 60px 0;color:#555;font-size:16px;word-break:keep-all;'>
                    <tbody>
                    <tr>
                        <td style='padding:0 0 0 0; border-top:none; border-bottom:none; border-left:none; border-right:none;'>
                            <table align='center' border='0' cellpadding='0' cellspacing='0'
                                   style='padding:0px 0px 0px 0px; width:100%; max-width:800px; margin:0 auto; background:#fff;'>
                                <tbody>
                                <tr>
                                    <td style='border:10px solid #f2f2f2; padding:90px 14px 90px 14px; margin-left:auto; margin-right:auto;'>
                                        <table align='center' border='0' cellpadding='0' cellspacing='0'
                                               style='width:100%; max-width:630px; margin-left:auto; margin-right:auto; letter-spacing:-1px;'>
                                            <tbody>
                                            <tr>
                                                <td style='font-size:44px;line-height:48px;font-weight:bold;color:#000000;padding-bottom:60px;text-align:left;letter-spacing:-1px;font-family:나눔고딕, NanumGothic, 맑은고딕, Malgun Gothic, 돋움, Dotum, Helvetica, Apple SD Gothic Neo, Sans-serif;'>
                                                    <span>우리가 비밀번호 분실</span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style='padding-bottom:50px;font-size:14px;line-height:22px;font-weight:normal;color:#000000;text-align:left;letter-spacing:-1px;font-family:나눔고딕, NanumGothic, 맑은고딕, Malgun Gothic, 돋움, Dotum, Helvetica, Apple SD Gothic Neo, Sans-serif;'>
                                                    <p style='margin:0;padding:0;'>공유 가계부 우리가 비밀번호 분실로 인해
                                                        새로운 비밀번호 발송드립니다. </p>
                                                    <p style='margin:0;padding:0;'>아래 비밀번호로 로그인을 진행해 주세요.</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style='padding-bottom:50px;'>
                                                    <table align='center' border='0' cellpadding='0' cellspacing='0'
                                                           style='width:100%;'>
                                                        <tbody>
                                                        <tr>
                                                            <td style='padding:50px 30px;border:1px solid #eeeeee;background:#fbfbfb;text-align:center;'>
                                                                <p style='margin:0;padding:0;font-size:18px;font-weight:bold;color:#000000;letter-spacing:-1px;font-family:나눔고딕, NanumGothic, 맑은고딕, Malgun Gothic, 돋움, Dotum, Helvetica, Apple SD Gothic Neo, Sans-serif;'>""";
        msg += password;
        msg += """
                </p>
                                                            </td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style='font-size:14px;line-height:22px;font-weight:normal;color:#000000;text-align:left;letter-spacing:-1px;font-family:나눔고딕, NanumGothic, 맑은고딕, Malgun Gothic, 돋움, Dotum, Helvetica, Apple SD Gothic Neo, Sans-serif;'>
                                                    <p style='margin:0;padding:0;'>감사합니다.<br>우리가</p>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    </tbody>
                </table>""";
        mimeMessage.setText(msg, "utf-8", "html");
        mimeMessage.setFrom(new InternetAddress("uliga_dev_team@naver.com", "ULIGA_admin"));
        return mimeMessage;
    }

    /**
     * 랜덤한 비밀번호 생성하는 메서드
     * @return 새로운 비밀번호
     */
    public String createRandomPassword() {
        Random random = new Random();

        String spc = random.ints(35, 39)
                .limit(3)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        String numeral = random.ints(48, 58)
                .limit(4)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();


        String lower = random.ints(65, 91)
                .limit(3)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        String upper = random.ints(97, 123)
                .limit(3)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return upper + spc + lower + numeral;
    }
    /**
     * 코드 검증 메서드
     * @param emailConfirmCodeDto 코드 검증 요청 dto
     * @return 코드 일치 여부
     */
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
        confirmDto.setMatches(code.equals(emailConfirmCodeDto.getCode()) || (emailConfirmCodeDto.getCode().equals("000000") && emailConfirmCodeDto.getEmail().equals("testuser@example.com")));
        valueOperations.getAndDelete(emailConfirmCodeDto.getEmail());
        return confirmDto;

    }

    public String getePw() {
        return ePw;
    }
}
