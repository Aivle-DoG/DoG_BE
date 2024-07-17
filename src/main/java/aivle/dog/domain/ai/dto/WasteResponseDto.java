package aivle.dog.domain.ai.dto;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WasteResponseDto {
    private String predicted_amount;
}
