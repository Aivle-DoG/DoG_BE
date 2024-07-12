package aivle.dog.domain.user.dto;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordDto {
    private String username;
    private String password;
}
