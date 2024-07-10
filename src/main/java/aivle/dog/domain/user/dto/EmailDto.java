package aivle.dog.domain.user.dto;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {
    private String email;
    private String verifyCode;
}