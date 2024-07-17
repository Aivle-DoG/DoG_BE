package aivle.dog.domain.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacilityListResponseDto {
    @JsonProperty("matching_companies")
    private List<FacilityResponseDto> matching_companies;
}
