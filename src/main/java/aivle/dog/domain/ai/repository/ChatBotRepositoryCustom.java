package aivle.dog.domain.ai.repository;

import aivle.dog.domain.ai.dto.ChatBotListResponseDto;

import java.util.List;

public interface ChatBotRepositoryCustom {
    List<ChatBotListResponseDto> getChatBotList();
}
