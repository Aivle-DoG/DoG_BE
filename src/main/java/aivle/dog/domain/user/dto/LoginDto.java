package aivle.dog.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDto {
    private String username;
    private String password;
}
