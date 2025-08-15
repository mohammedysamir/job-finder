package com.jobfinder.finder.repository;

import com.jobfinder.finder.entity.UserEntity;
import com.jobfinder.finder.entity.VerificationTokenEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, Long> {
  Optional<VerificationTokenEntity> findByToken(String token);

  Optional<VerificationTokenEntity> findByUser(UserEntity user);
}
