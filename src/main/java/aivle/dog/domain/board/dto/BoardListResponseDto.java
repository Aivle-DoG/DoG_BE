package aivle.dog.domain.board.dto;

import aivle.dog.domain.board.entity.InquiryType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListResponseDto {
    private Long id;
    private String inquiryType;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private Long viewCount;

    public BoardListResponseDto(Long id, InquiryType inquiryType, String title, LocalDateTime createdAt, Long viewCount) {
        this.id = id;
        this.inquiryType = inquiryType.getDesc();
        this.title = title;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
    }
}