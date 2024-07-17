package aivle.dog.domain.user.dto;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserListResponseDto {
    private String username;
    private String businessNumber;
    private String companyName;
}
