package aivle.dog.domain.ai.dto;

import lombok.*;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WasteResponseDto {
    private List<Double> predicted_amount;
}
