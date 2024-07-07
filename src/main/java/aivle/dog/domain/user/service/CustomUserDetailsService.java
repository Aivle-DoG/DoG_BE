package aivle.dog.domain.user.service;

import aivle.dog.domain.user.dto.CustomAdminDetails;
import aivle.dog.domain.user.dto.CustomUserDetails;
import aivle.dog.domain.user.entity.Admin;
import aivle.dog.domain.user.entity.User;
import aivle.dog.domain.user.repository.AdminRepository;
import aivle.dog.domain.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public CustomUserDetailsService(UserRepository userRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername : " + username);
        //DB에서 조회
        //user가 아니면 admin에서 조회
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            Admin admin = adminRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not exist with name :" + username));
            return new CustomAdminDetails(admin);
        } else
            return new CustomUserDetails(user.get());

    }
}
