package aivle.dog.domain.user.service;

import aivle.dog.domain.ai.dto.ChatBotListResponseDto;
import aivle.dog.domain.ai.dto.WasteListResponseDto;
import aivle.dog.domain.ai.repository.ChatBotRepository;
import aivle.dog.domain.ai.repository.WasteImageRepository;
import aivle.dog.domain.user.dto.AdminSignupDto;
import aivle.dog.domain.user.dto.UserListResponseDto;
import aivle.dog.domain.user.entity.Admin;
import aivle.dog.domain.user.repository.AdminRepository;
import aivle.dog.domain.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
public class AdminService {
    private final WasteImageRepository wasteImageRepository;
    private final ChatBotRepository chatBotRepository;

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminService(AdminRepository adminRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                        ChatBotRepository chatBotRepository,
                        WasteImageRepository wasteImageRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.chatBotRepository = chatBotRepository;
        this.wasteImageRepository = wasteImageRepository;
    }

    @Transactional
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

    @Transactional(readOnly = true)
    public List<UserListResponseDto> getUserList(String username) {
        adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("없는 관리자입니다"));

        return userRepository.getUserList();
    }

    @Transactional(readOnly = true)
    public List<WasteListResponseDto> getWasteList(String username) {
        adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("없는 관리자입니다"));
        return wasteImageRepository.getWasteList();
    }

    @Transactional(readOnly = true)
    public List<ChatBotListResponseDto> getChatBotList(String username) {
        adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("없는 관리자입니다"));
        return chatBotRepository.getChatBotList();
    }

}
