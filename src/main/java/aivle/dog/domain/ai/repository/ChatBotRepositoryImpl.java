package aivle.dog.domain.ai.repository;

import aivle.dog.domain.ai.dto.ChatBotListResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.List;

import static aivle.dog.domain.ai.entity.QChatBot.chatBot;

@Log4j2
@RequiredArgsConstructor
@Repository
public class ChatBotRepositoryImpl implements ChatBotRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<ChatBotListResponseDto> getChatBotList() {
        return queryFactory
                .select(Projections.constructor(ChatBotListResponseDto.class,
                        chatBot.question,
                        chatBot.user.username,
                        chatBot.user.companyName))
                .from(chatBot)
                .orderBy(chatBot.createdAt.asc())
                .fetch();
    }
}
