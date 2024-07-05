package aivle.dog.domain.user.repository;

import aivle.dog.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsByUsername(String username);
    User findByUsername(String username);
}