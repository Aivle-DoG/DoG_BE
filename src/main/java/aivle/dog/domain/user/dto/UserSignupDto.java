package aivle.dog.domain.user.dto;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSignupDto {
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
}
