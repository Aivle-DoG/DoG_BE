package aivle.dog.domain.user.controller;

import aivle.dog.domain.user.dto.UserSignupDto;
import aivle.dog.domain.user.service.CustomUserDetailsService;
import aivle.dog.domain.user.service.UserService;
import aivle.dog.global.Message;
import aivle.dog.global.StateEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("api/v1")
public class UserController {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    public UserController(UserService userService, CustomUserDetailsService customUserDetailsService) {

        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Message> signUp(@RequestBody UserSignupDto userSignupDto) {
        log.info(userSignupDto.getUsername());

        Message message = new Message();
        HttpHeaders headers = new HttpHeaders();

        try {
            int code = userService.signUp(userSignupDto);
            message.setStatus(StateEnum.OK);
            message.setMessage("회원가입 성공");
            message.setData(String.valueOf(code));
        }catch(Exception e){
            log.error("UserController/signUp : "+e.getMessage(),e);
            message.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(message);
    }
}