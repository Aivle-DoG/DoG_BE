package aivle.dog.domain.ai.dto;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GptRequestDto {
    private String question;
}
