package aivle.dog.domain.user.dto;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminSignupDto {
    private String username;
    private String password;
    private String name;
}