package aivle.dog.domain.ai.repository;

import aivle.dog.domain.ai.entity.ChatBot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatBotRepository extends JpaRepository<ChatBot, Long>, ChatBotRepositoryCustom {
}
