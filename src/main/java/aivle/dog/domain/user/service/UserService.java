package aivle.dog.domain.user.service;

import aivle.dog.domain.user.dto.PasswordDto;
import aivle.dog.domain.user.dto.UserSignupDto;
import aivle.dog.domain.user.entity.User;
import aivle.dog.domain.user.repository.AdminRepository;
import aivle.dog.domain.user.repository.UserRepository;
import aivle.dog.security.util.RedisUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final RedisUtil redisUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, AdminRepository adminRepository, RedisUtil redisUtil, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.redisUtil = redisUtil;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Integer signUp(UserSignupDto userSignupDto) {

        Boolean isExist = userRepository.existsByUsername(userSignupDto.getUsername());
        if (isExist) throw new RuntimeException("중복된 이메일입니다");
        else {
            isExist = adminRepository.existsByUsername(userSignupDto.getUsername());
            if (isExist) throw new RuntimeException("중복된 이메일입니다");
        }

        userRepository.save(User.builder()
                .username(userSignupDto.getUsername())
                .password(bCryptPasswordEncoder.encode(userSignupDto.getPassword()))
                .name(userSignupDto.getName())
                .phoneNumber(userSignupDto.getPhoneNumber()).build());
        return 0;
    }

    public Integer updatePassword(PasswordDto passwordDto) {
        User user = userRepository.findByUsername(passwordDto.getUsername())
                .orElseThrow(() -> new RuntimeException("없는 사용자입니다"));
        String isPossible = redisUtil.getData(passwordDto.getUsername());
        if (!isPossible.equals("true"))
            throw new RuntimeException("비밀번호를 바꿀 수 없습니다");
        userRepository.save(user.updatePassword(
                bCryptPasswordEncoder.encode(passwordDto.getPassword())
        ));
        redisUtil.deleteData(passwordDto.getUsername());
        return 0;
    }

}