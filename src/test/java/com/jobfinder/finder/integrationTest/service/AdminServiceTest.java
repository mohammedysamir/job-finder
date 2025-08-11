package com.jobfinder.finder.integrationTest.service;

import com.jobfinder.finder.config.redis.RedisConfiguration;
import com.jobfinder.finder.dto.admin.AdminCreationDto;
import com.jobfinder.finder.dto.admin.AdminPatchDto;
import com.jobfinder.finder.dto.admin.AdminResponseDto;
import com.jobfinder.finder.entity.AdminEntity;
import com.jobfinder.finder.mapper.AdminMapper;
import com.jobfinder.finder.repository.AdminRepository;
import com.jobfinder.finder.service.AdminService;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootTest
public class AdminServiceTest extends CacheTestIntializer {


  @SpyBean
  AdminMapper adminMapper;

  @MockBean
  AdminRepository adminRepository;

  @Autowired
  AdminService adminService;

  AdminEntity entity = new AdminEntity(1L, "admin", "", "admin@gmail.com");

  //-- Get Admin tests --//
  @Test
  public void getAdminTest() {
    // Assign
    String username = "admin";
    Mockito.when(adminRepository.findByUsername(username)).thenReturn(java.util.Optional.of(entity));

    // Act
    AdminResponseDto admin = adminService.getAdmin(username);

    // Assert
    Mockito.verify(adminRepository, Mockito.times(1)).findByUsername(username);
    assert admin.getUsername().equals(entity.getUsername());
    assert admin.getEmail().equals(entity.getEmail());
  }

  @Test
  public void getAdminTest_verifyCacheIsWorking() {
    // Assign
    String username = "admin";
    Mockito.when(adminRepository.findByUsername(username)).thenReturn(java.util.Optional.of(entity));

    // Act
    AdminResponseDto admin = adminService.getAdmin(username);
    AdminResponseDto sameAdmin = adminService.getAdmin(username);

    // Assert
    Mockito.verify(adminRepository, Mockito.times(1)).findByUsername(username);
    assert admin.getUsername().equals(entity.getUsername());
    assert admin.getEmail().equals(entity.getEmail());
  }

  @Test
  public void getAdminTest_notFound_404() {
    // Assign
    String username = "admin";
    Mockito.when(adminRepository.findByUsername(username)).thenThrow(UsernameNotFoundException.class);

    // Act
    //assert throws UsernameNotFoundException.class
    Assertions.assertThrows(UsernameNotFoundException.class, () -> {
      adminService.getAdmin(username);
    });
  }

  //-- Delete Admin tests --//

  @Test
  public void deleteAdminTest() {
    // Assign
    String username = "admin";
    cacheManager.getCache(RedisConfiguration.CACHE_NAME).put("AdminService_deleteAdmin_" + username, entity);
    Mockito.when(adminRepository.findByUsername(username)).thenReturn(java.util.Optional.of(entity));

    // Act
    adminService.deleteAdmin(username);

    // Assert
    Mockito.verify(adminRepository, Mockito.times(1)).findByUsername(username);
    assert Objects.requireNonNull(cacheManager.getCache(RedisConfiguration.CACHE_NAME)).get(username) == null;
  }

  @Test
  public void deleteAdminTest_notFound_404() {
    // Assign
    String username = "admin";
    Mockito.when(adminRepository.findByUsername(username)).thenThrow(UsernameNotFoundException.class);

    // Act
    //assert throws UsernameNotFoundException.class
    Assertions.assertThrows(UsernameNotFoundException.class, () -> {
      adminService.deleteAdmin(username);
    });
  }
  //-- Update Admin tests --//

  @Test
  public void updateAdminTest() {
    // Assign
    String username = "admin";
    AdminPatchDto dto = new AdminPatchDto("updatedPassword", "alsoNewEmail@gmail.com");
    AdminEntity newEntity = new AdminEntity(1L, username, dto.getPassword(), dto.getEmail());

    Mockito.when(adminRepository.findByUsername(username)).thenReturn(java.util.Optional.of(entity));
    Mockito.when(adminRepository.save(newEntity)).thenReturn(newEntity);

    // Act
    AdminResponseDto adminResponseDto = adminService.updateAdmin(username, dto);

    // Assert
    Mockito.verify(adminRepository, Mockito.times(1)).findByUsername(username);

    assert adminResponseDto.getUsername().equals(newEntity.getUsername());
    assert adminResponseDto.getEmail().equals(newEntity.getEmail());
  }

  @Test
  public void updateAdminTest_notFound_404() {
    // Assign
    String username = "admin";
    Mockito.when(adminRepository.findByUsername(username)).thenThrow(UsernameNotFoundException.class);

    // Act
    //assert throws UsernameNotFoundException.class
    Assertions.assertThrows(UsernameNotFoundException.class, () -> {
      adminService.updateAdmin(username, new AdminPatchDto("updatedPass", "alsoNewEmail@gmail.com"));
    });
  }

  //-- Create Admin tests --//
  @Test
  public void createAdminTest() {
    // Assign
    String username = "admin";
    String mail = "email@gmail.com";
    String password = "password";

    AdminCreationDto dto = new AdminCreationDto(username, password, mail);
    AdminEntity newEntity = new AdminEntity(1L, username, password, mail);

    Mockito.when(adminRepository.existsByUsername(username)).thenReturn(false);
    Mockito.when(adminRepository.save(newEntity)).thenReturn(newEntity);
    // Act
    AdminResponseDto adminResponseDto = adminService.createAdmin(dto);
    // Assert
    Mockito.verify(adminRepository, Mockito.times(1)).existsByUsername(username);
    assert adminResponseDto.getUsername().equals(newEntity.getUsername());
    assert adminResponseDto.getEmail().equals(newEntity.getEmail());
  }

  @Test
  public void createAdminTest_alreadyExists_409() {
    // Assign
    String username = "admin";
    String mail = "email@gmail.com";
    String password = "password";
    AdminCreationDto dto = new AdminCreationDto(username, password, mail);
    Mockito.when(adminRepository.existsByUsername(username)).thenReturn(true);
    // Act
    //assert throws AdminNotFoundException.class
    Assertions.assertThrows(com.jobfinder.finder.exception.UsernameConflictException.class, () -> {
      adminService.createAdmin(dto);
    });
    // Assert
    Mockito.verify(adminRepository, Mockito.times(1)).existsByUsername(username);
    Mockito.verify(adminRepository, Mockito.times(0)).save(Mockito.any(AdminEntity.class));
  }

}
