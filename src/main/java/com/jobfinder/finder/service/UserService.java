package com.jobfinder.finder.service;

import com.jobfinder.finder.dto.user.UserPatchDto;
import com.jobfinder.finder.dto.user.UserRegistrationDto;
import com.jobfinder.finder.dto.user.UserResponseDto;
import com.jobfinder.finder.entity.UserEntity;
import com.jobfinder.finder.mapper.UserMapper;
import com.jobfinder.finder.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserService(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  public UserResponseDto loginUser(@Valid UserRegistrationDto dto) {
    log.info("Logging in user: {}", dto.toString());
    Optional<UserEntity> optionalUserEntity = userRepository.findByUsernameOrEmail(dto.getUsername(), dto.getEmail());
    if (optionalUserEntity.isPresent()) {
      UserEntity userEntity = optionalUserEntity.get();
      //todo: Here you would typically check the password, but for simplicity, we assume it's correct
      return userMapper.toDto(userEntity);
    } else {
      log.warn("User with username/email not found");
      throw new UsernameNotFoundException("User not found with username/email: " + dto.getUsername() + " or " + dto.getEmail());
    }
  }

  public UserResponseDto registerUser(UserRegistrationDto dto) {
    log.info("Registering a new user: {}", dto.toString());
    if (!dto.getRole().equalsIgnoreCase("APPLICANT") && !dto.getRole().equalsIgnoreCase("RECRUITER")) {
      log.error("Invalid role provided: {}", dto.getRole());
      throw new IllegalArgumentException("Invalid role provided: " + dto.getRole());
    }

    UserEntity entity = userMapper.toEntity(dto);
    userRepository.save(entity);
    return userMapper.toDto(entity);
  }

  @Cacheable
  public UserResponseDto getUserProfile(String username) {
    log.info("Fetching user profile for username: {}", username);
    Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);
    if (optionalUserEntity.isPresent()) {
      UserEntity userEntity = optionalUserEntity.get();
      return userMapper.toDto(userEntity);
    } else {
      log.warn("User with username {} not found", username);
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
  }

  public UserResponseDto updateUserProfile(String username, UserPatchDto dto) {
    log.info("Updating user profile for username: {} with data: {}", username, dto.toString());
    Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);
    if (optionalUserEntity.isPresent()) {
      UserEntity userEntity = optionalUserEntity.get();
      UserEntity updatedEntity = userMapper.toEntity(dto);
      updatedEntity.setId(userEntity.getId());

      userRepository.save(updatedEntity);
      return userMapper.toDto(updatedEntity);
    } else {
      log.warn("User with username {} not found", username);
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
  }

  @CacheEvict(key = "#username")
  public void deleteUser(String username) {
    log.info("Deleting a user for username: {}", username);
    Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);
    if (optionalUserEntity.isPresent()) {
      userRepository.deleteById(optionalUserEntity.get().getId());
    } else {
      log.warn("User with username {} not found", username);
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
  }

}
