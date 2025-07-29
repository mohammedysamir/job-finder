package com.jobfinder.finder.repository;

import com.jobfinder.finder.entity.AdminEntity;
import com.jobfinder.finder.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
  Optional<AdminEntity> findByUsername(String username);

  Optional<AdminEntity> findByUsernameOrEmail(String username, String email);
}
