package aivle.dog.domain.ai.dto;

import lombok.*;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PredictionOfImageDto {
    private String zipUrl;
    private String image1;
    private String image2;
    private String image3;
    private String csv1;
    private String csv2;
    private List<StatisticSummaryDto> csv_str_st;
    private String folder_name;
}
