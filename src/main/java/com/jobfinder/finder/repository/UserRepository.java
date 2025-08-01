package com.jobfinder.finder.repository;

import com.jobfinder.finder.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsername(String username);
  Optional<UserEntity> findByUsernameOrEmail(String username,String email);
}
