package aivle.dog.domain.ai.dto;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatBotListResponseDto {
    private String question;
    private String username;
    private String companyName;
}
