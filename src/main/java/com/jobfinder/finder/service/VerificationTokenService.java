package com.jobfinder.finder.service;

import com.jobfinder.finder.constant.UserStatus;
import com.jobfinder.finder.entity.UserEntity;
import com.jobfinder.finder.entity.VerificationTokenEntity;
import com.jobfinder.finder.repository.UserRepository;
import com.jobfinder.finder.repository.VerificationTokenRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationTokenService {

  @org.springframework.beans.factory.annotation.Value("${verification.token.ttlInHours:24}")
  private static long TOKEN_TTL_IN_HOURS;

  private final VerificationTokenRepository verificationTokenRepository;
  private final UserRepository userRepository;

  public String generateVerificationToken(String email) {
    if (email == null || email.isEmpty()) {
      log.error("Email cannot be null or empty");
      throw new IllegalArgumentException("Email cannot be null or empty");
    }

    Optional<UserEntity> userOptional = userRepository.findByUsernameOrEmail(null, email);
    if (userOptional.isEmpty()) {
      log.error("User with email {} does not exist", email);
      throw new IllegalArgumentException("User with email " + email + " does not exist");
    }

    log.info("Generating verification token for email: {}", email);
    String rawToken = String.format("%s:%s:%s", email, System.currentTimeMillis(), UUID.randomUUID());
    try {
      // Hash the raw token using SHA-256
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
      String hashedToken = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
      // Save the token in the repository with a TTL
      verificationTokenRepository.save(new VerificationTokenEntity(hashedToken, userOptional.get(), TOKEN_TTL_IN_HOURS));
      return hashedToken;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("SHA-256 not available", e);
    }
  }

  public boolean validateVerificationToken(String token) {
    log.info("Validating verification token: {}", token);
    if (token == null || token.isEmpty()) {
      log.warn("Invalid token: Token is null or empty");
      return false;
    }
    Optional<VerificationTokenEntity> tokenOptional = verificationTokenRepository.findByToken(token);
    if (tokenOptional.isEmpty()) {
      log.warn("Invalid token: Token does not exist in the repository");
      return false;
    }
    VerificationTokenEntity tokenEntity = tokenOptional.get();
    if (isExpired(tokenEntity)) {
      log.warn("Invalid token: Token has expired");
      return false;
    }
    //remove token and update user status to verified
    verificationTokenRepository.delete(tokenEntity);
    UserEntity user = tokenEntity.getUser();
    user.setUserStatus(UserStatus.VERIFIED);
    userRepository.save(user);
    return true;
  }

  private boolean isExpired(VerificationTokenEntity tokenEntity) {
    long expirationTime = tokenEntity.getExpiryDate().getTime();
    return System.currentTimeMillis() > expirationTime;
  }
}
