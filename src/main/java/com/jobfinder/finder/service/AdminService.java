package com.jobfinder.finder.service;

import com.jobfinder.finder.config.redis.RedisConfiguration;
import com.jobfinder.finder.dto.admin.AdminCreationDto;
import com.jobfinder.finder.dto.admin.AdminPatchDto;
import com.jobfinder.finder.dto.admin.AdminResponseDto;
import com.jobfinder.finder.entity.AdminEntity;
import com.jobfinder.finder.exception.UsernameConflictException;
import com.jobfinder.finder.mapper.AdminMapper;
import com.jobfinder.finder.repository.AdminRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
  private final AdminRepository adminRepository;
  private final AdminMapper adminMapper;
  private final PasswordEncoder passwordEncoder;
  private static final String ADMIN_NOT_FOUND_MESSAGE = "No admin was found with this username: ";

  @Cacheable(cacheNames = RedisConfiguration.CACHE_NAME, keyGenerator = "customRedisKeyGenerator")
  public AdminResponseDto getAdmin(String username) {
    log.info("Fetching admin details for username: {}", username);
    AdminEntity adminEntity = adminRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(ADMIN_NOT_FOUND_MESSAGE + username));
    return adminMapper.toDto(adminEntity);
  }

  @CacheEvict(cacheNames = RedisConfiguration.CACHE_NAME, keyGenerator = "customRedisKeyGenerator")
  public void deleteAdmin(String username) {
    log.info("Deleting an admin with username: {}", username);
    AdminEntity adminEntity = adminRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(ADMIN_NOT_FOUND_MESSAGE + username));
    adminRepository.delete(adminEntity);
  }

  @Transactional
  public AdminResponseDto updateAdmin(String username, AdminPatchDto dto) {
    log.info("Patching an admin with username: {} with data: {}", username, dto);
    AdminEntity adminEntity = adminRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(ADMIN_NOT_FOUND_MESSAGE + username));
    AdminEntity newEntity = new AdminEntity();

    newEntity.setId(adminEntity.getId());
    newEntity.setUsername(adminEntity.getUsername());
    //validate the patch DTO
    validatePatchDto(dto);

    newEntity.setPassword(dto.getPassword() != null ? dto.getPassword() : adminEntity.getPassword());
    newEntity.setEmail(dto.getEmail() != null ? dto.getEmail() : adminEntity.getEmail());

    adminRepository.save(newEntity);
    return adminMapper.toDto(newEntity);
  }

  public AdminResponseDto createAdmin(AdminCreationDto dto) {
    log.info("Creating an admin: {}", dto);
    if (adminRepository.existsByUsername(dto.getUsername())) {
      log.error("Admin with username {} already exists", dto.getUsername());
      throw new UsernameConflictException(dto.getUsername());
    }
    AdminEntity entity = adminMapper.toEntity(dto);
    entity.setPassword(passwordEncoder.encode(entity.getPassword()));
    adminRepository.save(entity);
    return adminMapper.toResponse(dto);
  }

  private void validatePatchDto(AdminPatchDto dto) {

    String password = dto.getPassword();
    String email = dto.getEmail();

    if (password != null && (password.length() < 12 || password.length() > 25)) {
      log.error("Password must be between 12 and 25 characters");
      throw new IllegalArgumentException("Password must be between 12 and 25 characters");
    }
    if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
      log.error("Invalid email format: {}", email);
      throw new IllegalArgumentException("Invalid email format: " + email);
    }
  }

}
