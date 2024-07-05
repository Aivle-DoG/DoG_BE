package aivle.dog.domain.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardResponseDto {
    private String title;
    private String description;
    private String username;
    private LocalDateTime modifiedAt;
    private Long viewCount;
}
