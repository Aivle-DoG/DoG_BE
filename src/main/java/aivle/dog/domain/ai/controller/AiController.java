package aivle.dog.domain.ai.controller;

import aivle.dog.domain.ai.dto.FacilityResponseDto;
import aivle.dog.domain.ai.dto.GptRequestDto;
import aivle.dog.domain.ai.dto.PredictionOfImageDto;
import aivle.dog.domain.ai.dto.PredictionOfWasteDto;
import aivle.dog.domain.ai.service.AiService;
import aivle.dog.global.Message;
import aivle.dog.global.StateEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("api/v1/ai")
public class AiController {
    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/image")
    public ResponseEntity<Message> predictImage(@RequestPart(value = "file", required = false) MultipartFile file, @AuthenticationPrincipal UserDetails user) {
        log.info("AiController/predictImage : " + file);
        log.info("AiController/predictImage : " + (user == null ? null : user.getUsername()));

        Message message = new Message();
        try {
            PredictionOfImageDto result = aiService.predictImage(file, user);
            message.setStatus(StateEnum.OK);
            message.setMessage("이미지 예측 완료");
            message.setData(result);
        } catch (Exception e) {
            log.error("AiController/predictImage : " + e.getMessage(), e);
            message.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }

    @PostMapping("/waste")
    public ResponseEntity<Message> predictWaste(@RequestBody PredictionOfWasteDto predictionOfWasteDto) {
        log.info("AiController/predictWaste : " + predictionOfWasteDto);
        Message message = new Message();
        try {
            String result = aiService.predictWaste(predictionOfWasteDto);
            message.setStatus(StateEnum.OK);
            message.setMessage("폐기물 양 예측 완료");
            message.setData(result);
        } catch (Exception e) {
            log.error("AiController/predictWaste : " + e.getMessage(), e);
            message.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }

    @PostMapping("/gpt")
    public ResponseEntity<Message> askChatBot(@RequestBody GptRequestDto question, @AuthenticationPrincipal UserDetails user) {
        log.info("AiController/askChatGPT : " + question);
        log.info("AiController/askChatGPT : " + (user == null ? null : user.getUsername()));

        Message message = new Message();
        try {
            String answer = aiService.askChatBot(question.getQuestion(), user);
            message.setStatus(StateEnum.OK);
            message.setMessage("챗봇 답변 완료");
            message.setData(answer);
        } catch (Exception e) {
            log.error("AiController/askChatGPT : " + e.getMessage(), e);
            message.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/facility")
    public ResponseEntity<Message> getFacility(@RequestParam String region) {
        log.info("AiController/getFacility : " + region);

        Message message = new Message();
        try {
            List<FacilityResponseDto> answer = aiService.getFacility(region);
            message.setStatus(StateEnum.OK);
            message.setMessage("업체 추천 완료");
            message.setData(answer);
        } catch (Exception e) {
            log.error("AiController/getFacility : " + e.getMessage(), e);
            message.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }

}
