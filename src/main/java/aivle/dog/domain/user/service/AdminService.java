package aivle.dog.domain.user.service;

import aivle.dog.domain.user.dto.AdminSignupDto;
import aivle.dog.domain.user.entity.Admin;
import aivle.dog.domain.user.repository.AdminRepository;
import aivle.dog.domain.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminService(AdminRepository adminRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Integer signUp(AdminSignupDto adminSignupDto) {
        Boolean isExist = adminRepository.existsByUsername(adminSignupDto.getUsername());
        if (isExist) throw new RuntimeException("중복된 이메일입니다");
        else {
            isExist = userRepository.existsByUsername(adminSignupDto.getUsername());
            if (isExist) throw new RuntimeException("중복된 이메일입니다");
        }

        adminRepository.save(Admin.builder()
                .username(adminSignupDto.getUsername())
                .password(bCryptPasswordEncoder.encode(adminSignupDto.getPassword()))
                .name(adminSignupDto.getName()).build());
        return 0;
    }
}
