package aivle.dog.domain.user.repository;

import aivle.dog.domain.user.dto.UserListResponseDto;

import java.util.List;

public interface UserRepositoryCustom {
    List<UserListResponseDto> getUserList();
}
