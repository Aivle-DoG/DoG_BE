package aivle.dog.domain.user.controller;

import aivle.dog.domain.user.dto.AdminSignupDto;
import aivle.dog.domain.user.service.AdminService;
import aivle.dog.global.Message;
import aivle.dog.global.StateEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Message> signUp(@RequestBody AdminSignupDto adminSignupDto) {
        log.info("AdminController/signUp : " + adminSignupDto);
        Message message = new Message();

        try {
            int code = adminService.signUp(adminSignupDto);
            message.setStatus(StateEnum.OK);
            message.setMessage("회원가입 성공");
            message.setData(String.valueOf(code));
        } catch (Exception e) {
            log.error("AdminController/signUp : " + e.getMessage(), e);
            message.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }
}