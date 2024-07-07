package aivle.dog.domain.user.service;

import aivle.dog.domain.user.dto.UserSignupDto;
import aivle.dog.domain.user.entity.User;
import aivle.dog.domain.user.repository.AdminRepository;
import aivle.dog.domain.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, AdminRepository adminRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
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
}