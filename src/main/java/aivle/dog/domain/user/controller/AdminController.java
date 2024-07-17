package aivle.dog.domain.user.controller;

import aivle.dog.domain.ai.dto.ChatBotListResponseDto;
import aivle.dog.domain.ai.dto.WasteListResponseDto;
import aivle.dog.domain.user.dto.AdminSignupDto;
import aivle.dog.domain.user.dto.UserListResponseDto;
import aivle.dog.domain.user.service.AdminService;
import aivle.dog.global.Message;
import aivle.dog.global.StateEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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

    @GetMapping("/user/list")
    public ResponseEntity<Message> getUserList(@AuthenticationPrincipal UserDetails user) {
        log.info("AdminController/getUserList : " + user.getUsername());

        Message message = new Message();

        try {
            List<UserListResponseDto> userList = adminService.getUserList(user.getUsername());
            message.setStatus(StateEnum.OK);
            message.setMessage("사용자 리스트 조회 성공");
            message.setData(userList);
        } catch (Exception e) {
            log.error("AdminController/getUserList : " + e.getMessage(), e);
            message.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/waste/list")
    public ResponseEntity<Message> getWasteList(@AuthenticationPrincipal UserDetails user) {
        log.info("AdminController/getWasteList : " + user.getUsername());

        Message message = new Message();

        try {
            List<WasteListResponseDto> userList = adminService.getWasteList(user.getUsername());
            message.setStatus(StateEnum.OK);
            message.setMessage("폐기물 리스트 조회 성공");
            message.setData(userList);
        } catch (Exception e) {
            log.error("AdminController/getWasteList : " + e.getMessage(), e);
            message.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/chat-bot/list")
    public ResponseEntity<Message> getChatBotList(@AuthenticationPrincipal UserDetails user) {
        log.info("AdminController/getChatBotList : " + user.getUsername());

        Message message = new Message();

        try {
            List<ChatBotListResponseDto> userList = adminService.getChatBotList(user.getUsername());
            message.setStatus(StateEnum.OK);
            message.setMessage("챗봇 리스트 조회 성공");
            message.setData(userList);
        } catch (Exception e) {
            log.error("AdminController/getChatBotList : " + e.getMessage(), e);
            message.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }
}