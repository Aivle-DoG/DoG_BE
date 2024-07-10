package aivle.dog.global.controller;

import aivle.dog.domain.user.dto.EmailDto;
import aivle.dog.global.Message;
import aivle.dog.global.StateEnum;
import aivle.dog.global.service.EmailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("api/v1/email")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    // 인증코드 메일 발송
    @PostMapping("/send")
    public ResponseEntity<Message> mailSend(@RequestBody EmailDto emailDto) {
        log.info("EmailController/mailSend : " + emailDto);
        Message message = new Message();

        try {
            emailService.sendEmail(emailDto.getEmail());
            message.setStatus(StateEnum.OK);
            message.setMessage("인증코드 전송 성공");
            message.setData("인증코드가 발송되었습니다");
        } catch (Exception e) {
            log.error("EmailController/mailSend : " + e.getMessage(), e);
            message.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }

        return ResponseEntity.ok(message);
    }

    // 인증코드 인증
    @PostMapping("/verify")
    public ResponseEntity<Message> verify(@RequestBody EmailDto emailDto) {
        log.info("EmailController/verify : " + emailDto);
        Message message = new Message();

        boolean isVerify = false;
        try {
            isVerify = emailService.verifyEmailCode(emailDto.getEmail(), emailDto.getVerifyCode());
            message.setStatus(isVerify ? StateEnum.OK : StateEnum.BAD_REQUEST);
            message.setMessage(isVerify ? "인증 성공" : "인증 실패");
            message.setData(isVerify);
            return isVerify ? ResponseEntity.ok(message) : ResponseEntity.badRequest().body(message);
        } catch (Exception e) {
            log.error("EmailController/verify : " + e.getMessage(), e);
            message.setMessage(e.getMessage());
            message.setData(isVerify);
            return ResponseEntity.badRequest().body(message);
        }
    }
}
