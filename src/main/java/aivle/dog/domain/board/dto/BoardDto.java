package aivle.dog.domain.board.dto;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDto {
    private String title;
    private String description;
    private String username;
}
