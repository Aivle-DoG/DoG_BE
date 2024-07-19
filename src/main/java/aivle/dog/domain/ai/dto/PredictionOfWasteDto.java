package aivle.dog.domain.ai.dto;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PredictionOfWasteDto {
    private String totalFloorArea;
    private String totalFloors;
    private String constructionPeriod;
    private String structure;
    private String usage;
}
