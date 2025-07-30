package com.jobfinder.finder.service;

import com.jobfinder.finder.config.redis.RedisConfiguration;
import com.jobfinder.finder.dto.admin.AdminCreationDto;
import com.jobfinder.finder.dto.admin.AdminPatchDto;
import com.jobfinder.finder.dto.admin.AdminResponseDto;
import com.jobfinder.finder.entity.AdminEntity;
import com.jobfinder.finder.entity.UserEntity;
import com.jobfinder.finder.exception.AdminNotFoundException;
import com.jobfinder.finder.exception.UsernameConflictException;
import com.jobfinder.finder.mapper.AdminMapper;
import com.jobfinder.finder.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
  private final AdminRepository adminRepository;
  private final AdminMapper adminMapper;

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

  public AdminResponseDto updateAdmin(String username, AdminPatchDto dto) {
    log.info("Patching an admin with username: {} with data: {}", username, dto);
    AdminEntity adminEntity = adminRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(ADMIN_NOT_FOUND_MESSAGE + username));
    AdminEntity newEntity = adminMapper.toEntity(dto);

    newEntity.setId(adminEntity.getId());
    newEntity.setUsername(adminEntity.getUsername());

    adminRepository.save(newEntity);
    return adminMapper.toDto(newEntity);
  }

  public AdminResponseDto createAdmin(AdminCreationDto dto) {
    log.info("Creating an admin: {}", dto);
    if (adminRepository.existsByUsername(dto.getUsername())) {
      log.error("Admin with username {} already exists", dto.getUsername());
      throw new UsernameConflictException(dto.getUsername());
    }
    adminRepository.save(adminMapper.toEntity(dto));
    return adminMapper.toResponse(dto);
  }

}
