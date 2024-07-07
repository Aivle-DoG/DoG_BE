package aivle.dog.domain.user.repository;

import aivle.dog.domain.user.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByUsername(String username);

    Boolean existsByUsername(String username);
}