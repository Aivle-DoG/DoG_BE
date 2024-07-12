package aivle.dog.global.service;

import aivle.dog.security.util.RedisUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Log4j2
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;


    public EmailService(JavaMailSender javaMailSender, RedisUtil redisUtil) {
        this.javaMailSender = javaMailSender;
        this.redisUtil = redisUtil;
    }

    private String createCode(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        // length 길이만큼 랜덤 문자열 생성
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            stringBuilder.append(chars.charAt(index));
        }

        return stringBuilder.toString();
    }


    // 인증코드 이메일 발송
    @Async
    public void sendEmail(String toEmail) {
        if (redisUtil.existData(toEmail)) {
            redisUtil.deleteData(toEmail);
        }
        String authCode = createCode(6);
        log.info("EmailService/sendEmail : " + authCode);
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("DoG에서 보낸 인증코드입니다");
        message.setText(String.format(
                """
                        안녕하세요. 이메일 인증을 위한 인증코드입니다.
                        5분내에 입력해주시기 바랍니다.
                        %s
                        """, authCode
        ));
        // Redis 에 해당 인증코드 인증 시간 설정(초 단위)
        redisUtil.setDataExpire(toEmail, authCode, 60 * 5L);

        // 이메일 발송
        javaMailSender.send(message);
    }

    // 코드 검증
    public Boolean verifyEmailCode(String email, String code) {
        String codeFoundByEmail = redisUtil.getData(email);
        log.info("EmailService/verifyEmailCode : " + codeFoundByEmail);
        if (codeFoundByEmail == null) {
            return false;
        }
        boolean isVerify = codeFoundByEmail.equals(code);
        if (isVerify) {
            redisUtil.setData(email, "true");
        }
        return isVerify;
    }
}
