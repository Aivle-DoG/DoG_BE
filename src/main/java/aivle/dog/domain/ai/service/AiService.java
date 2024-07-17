package aivle.dog.domain.ai.service;

import aivle.dog.domain.ai.dto.*;
import aivle.dog.domain.ai.entity.ChatBot;
import aivle.dog.domain.ai.entity.WasteImage;
import aivle.dog.domain.ai.repository.ChatBotRepository;
import aivle.dog.domain.ai.repository.WasteImageRepository;
import aivle.dog.domain.user.entity.User;
import aivle.dog.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Log4j2
@Service
public class AiService {
    @Value("${my.ai.base-url}")
    private String myAiBaseUrl;

    private final ChatBotRepository chatBotRepository;
    private final UserRepository userRepository;
    private final WasteImageRepository wasteImageRepository;

    public AiService(WasteImageRepository wasteImageRepository,
                     UserRepository userRepository,
                     ChatBotRepository chatBotRepository) {
        this.wasteImageRepository = wasteImageRepository;
        this.userRepository = userRepository;
        this.chatBotRepository = chatBotRepository;
    }

    @Transactional
    public PredictionOfImageDto predictImage(MultipartFile file, UserDetails user) throws IOException {
        String aiServerUrl = myAiBaseUrl + "/predict";
        RestTemplate restTemplate = new RestTemplate();

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // MultipartFile로 변환
        Resource fileAsResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        // Body 생성
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileAsResource);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // ai 서버로 요청 보내기
        ResponseEntity<PredictionOfImageDto> response = restTemplate.postForEntity(aiServerUrl, requestEntity, PredictionOfImageDto.class);

        // 로그인 후 이용하는 경우 디비에 저장
        if (user != null) {
            User lookupUser = userRepository.findByUsername(user.getUsername())
                    .orElseThrow(() -> new RuntimeException("없는 사용자입니다"));
            wasteImageRepository.save(WasteImage.builder()
                    .user(lookupUser)
                    .csv(Objects.requireNonNull(response.getBody()).getCsv1())
                    .build());
        }
        return response.getBody();
    }

    @Transactional
    public String predictWaste(PredictionOfWasteDto predictionOfWasteDto) {
        String aiServerUrl = myAiBaseUrl + "/predict_waste_num";
        RestTemplate restTemplate = new RestTemplate();

        // 헤더 생성
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("공사명", predictionOfWasteDto.getConstructionSiteName());
        requestBody.put("공사 종류", predictionOfWasteDto.getConstructionSiteType());
        requestBody.put("폐기물 종류", predictionOfWasteDto.getWasteType());
        requestBody.put("시작일", predictionOfWasteDto.getStartDate().toString());
        requestBody.put("종료일", predictionOfWasteDto.getEndDate().toString());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, header);
        WasteResponseDto wasteResponseDto = restTemplate.postForObject(aiServerUrl, requestEntity, WasteResponseDto.class);
        if (wasteResponseDto == null)
            throw new RuntimeException("예측 값이 없습니다");
        return wasteResponseDto.getPredicted_amount();
    }

    @Transactional
    public String askChatBot(String question, UserDetails user) {
        String aiServerUrl = myAiBaseUrl + "/query";
        RestTemplate restTemplate = new RestTemplate();

        // 헤더 생성
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", question);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, header);
        GptResponseDto response = restTemplate.postForObject(aiServerUrl, requestEntity, GptResponseDto.class);
        if (response == null)
            throw new RuntimeException("응답 값이 없습니다");
        else if (response.getResponse() == null)
            throw new RuntimeException("응답 값이 없습니다");
        // 로그인 후 이용하는 경우 디비에 저장
        if (user != null) {
            User lookupUser = userRepository.findByUsername(user.getUsername())
                    .orElseThrow(() -> new RuntimeException("없는 사용자입니다"));
            chatBotRepository.save(ChatBot.builder()
                    .question(question)
                    .answer(response.getResponse())
                    .user(lookupUser)
                    .build());
        }
        log.info(response.toString());
        return response.getResponse();
    }

    @Transactional
    public List<FacilityResponseDto> getFacility(String region) {
        String aiServerUrl = String.format("%s/recommendation?region_city=%s", myAiBaseUrl, region);
        RestTemplate restTemplate = new RestTemplate();

        // 헤더 생성
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));


        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(header);
//        FacilityListResponseDto response = restTemplate.postForObject(aiServerUrl, requestEntity, FacilityListResponseDto.class);
        String jsonResponse = restTemplate.postForObject(aiServerUrl, requestEntity, String.class);
        log.info("AiService/getFacility : " + jsonResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        FacilityListResponseDto response;
        try {
            response = objectMapper.readValue(jsonResponse, FacilityListResponseDto.class);
        } catch (Exception e) {
            log.error("Error while parsing JSON response: ", e);
            throw new RuntimeException("Error while parsing JSON response", e);
        }
        if (response == null)
            throw new RuntimeException("응답 값이 없습니다");
        else if (response.getMatching_companies() == null)
            throw new RuntimeException("응답 값이 없습니다");

        log.info(response.toString());
        return response.getMatching_companies();
    }
}