package aivle.dog.domain.ai.dto;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacilityRequestDto {
    private String region;
    private String folderName;
}
