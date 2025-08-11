package com.jobfinder.finder.integrationTest.service;

import com.jobfinder.finder.config.redis.RedisConfiguration;
import com.jobfinder.finder.constant.Roles;
import com.jobfinder.finder.dto.user.AddressDto;
import com.jobfinder.finder.dto.user.UserLoginDto;
import com.jobfinder.finder.dto.user.UserPatchDto;
import com.jobfinder.finder.dto.user.UserRegistrationDto;
import com.jobfinder.finder.dto.user.UserResponseDto;
import com.jobfinder.finder.entity.AddressEntity;
import com.jobfinder.finder.entity.UserEntity;
import com.jobfinder.finder.exception.UsernameConflictException;
import com.jobfinder.finder.mapper.AddressMapper;
import com.jobfinder.finder.mapper.UserMapper;
import com.jobfinder.finder.repository.UserRepository;
import com.jobfinder.finder.service.UserService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootTest
public class UserServiceTest extends CacheTestIntializer {


  @SpyBean
  UserMapper userMapper;


  @SpyBean
  AddressMapper addressMapper;


  @MockBean
  UserRepository userRepository;

  @Autowired
  UserService userService;

  UserEntity applicant = new UserEntity(1L, "applicant", "1234topSecret", "applicant@gmail.com", "mohammed", "yasser", List.of("+20123456789"),
      List.of(new AddressEntity("Egypt", "Cairo", "12511")), LocalDate.of(2000, 1, 1), "https://example.com/profile.jpg", List.of(),
      Roles.APPLICANT);
  UserEntity recruiter = new UserEntity(2L, "recruiter", "very$trongPa$$w0rd", "recruiter@gmail.com", "ahmed", "mamdouh", List.of("+20123456623"),
      List.of(new AddressEntity("Egypt", "Mania", "10213")), LocalDate.of(1993, 10, 23), "https://example.com/profile.jpg", List.of(),
      Roles.RECRUITER);

  //-- Get User Profile tests --//
  @Test
  public void getApplicantProfileTest_happy() {
    // Assign
    String username = "applicant";
    Mockito.when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(applicant));

    // Act
    UserResponseDto response = userService.getUserProfile(username);

    // Assert
    Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    assert response.getUsername().equals(applicant.getUsername());
    assert response.getEmail().equals(applicant.getEmail());
    assert response.getFirstName().equals(applicant.getFirstName());
    assert response.getLastName().equals(applicant.getLastName());
  }


  @Test
  public void getRecruiterProfileTest_happy() {
    // Assign
    String username = "recruiter";
    Mockito.when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(recruiter));

    // Act
    UserResponseDto response = userService.getUserProfile(username);

    // Assert
    Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    assert response.getUsername().equals(recruiter.getUsername());
    assert response.getEmail().equals(recruiter.getEmail());
    assert response.getFirstName().equals(recruiter.getFirstName());
    assert response.getLastName().equals(recruiter.getLastName());
  }

  @Test
  public void getProfileTest_notFound() {
    // Assign
    String username = "recruiter";
    Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Act
    Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.getUserProfile(username));

  }

  @Test
  public void getProfileTest_verifyCacheIsWorking() {
    // Assign
    String username = "applicant";
    Mockito.when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(applicant));

    // Act
    UserResponseDto dto1 = userService.getUserProfile(username);
    UserResponseDto dto2 = userService.getUserProfile(username);

    // Assert
    Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    assert dto1.getUsername().equals(dto2.getUsername());
    assert dto1.getEmail().equals(dto2.getEmail());
    assert dto1.getFirstName().equals(dto2.getFirstName());
    assert dto1.getLastName().equals(dto2.getLastName());
  }

  //-- Delete User tests --//
  @Test
  public void deleteApplicantTest_happy() {
    // Assign
    cacheManager.getCache(RedisConfiguration.CACHE_NAME).put("UserService_deleteUser_applicant", applicant);

    String username = "applicant";
    Mockito.when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(applicant));
    Mockito.doNothing().when(userRepository).deleteById(applicant.getId());
    // Act
    userService.deleteUser(username);
    // Assert
    Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    Mockito.verify(userRepository, Mockito.times(1)).deleteById(applicant.getId());
    assert cacheManager.getCache(RedisConfiguration.CACHE_NAME).get("UserService_deleteUser_applicant") == null;
  }

  @Test
  public void deleteRecruiterTest_happy() {
    // Assign
    cacheManager.getCache(RedisConfiguration.CACHE_NAME).put("UserService_deleteUser_recruiter", recruiter);

    String username = "recruiter";
    Mockito.when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(recruiter));
    Mockito.doNothing().when(userRepository).deleteById(recruiter.getId());
    // Act
    userService.deleteUser(username);
    // Assert
    Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    Mockito.verify(userRepository, Mockito.times(1)).deleteById(recruiter.getId());
    assert cacheManager.getCache(RedisConfiguration.CACHE_NAME).get("UserService_deleteUser_recruiter") == null;
  }

  @Test
  public void deleteUserTest_notFound() {
    // Assign
    String username = "notFound";
    Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
    // Act
    Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.deleteUser(username));
  }

  //-- Update User Profile tests --//
  @Test
  public void updateApplicantProfileTest_happy() {
    // Assign
    String username = "applicant";
    Mockito.when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(applicant));
    Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(applicant);
    UserPatchDto updatedDto = new UserPatchDto(
        "newPassword123",
        "applicant@gmail.com",
        "Mohammed",
        "Yasser",
        List.of("+20123456789"),
        List.of(new AddressDto("Egypt", "Cairo", "12511")),
        LocalDate.of(2000, 1, 1),
        "https://example.com/profile.jpg"
    );

    // Act
    UserResponseDto response = userService.updateUserProfile(username, updatedDto);
    // Assert
    Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserEntity.class));
    assert response.getEmail().equals(updatedDto.getEmail());
    assert response.getFirstName().equals(updatedDto.getFirstName());
    assert response.getLastName().equals(updatedDto.getLastName());
  }

  @Test
  public void updateProfileTest_notFound() {
    // Assign
    String username = "notFound";
    Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
    UserPatchDto updatedDto = new UserPatchDto(
        "newPassword123",
        "applicant@gmail.com",
        "Mohammed",
        "Yasser",
        List.of("+20123456789"),
        List.of(new AddressDto("Egypt", "Cairo", "12511")),
        LocalDate.of(2000, 1, 1),
        "https://example.com/profile.jpg"
    );

    // Act
    Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.updateUserProfile(username, updatedDto));
  }

  //-- Register User tests --//
  @Test
  public void registerApplicantTest_happy() {
    // Assign
    UserRegistrationDto dto = new UserRegistrationDto("newApplicant", "newPassword123", "applicant@gmail.com",
        "Mohammed", "Yasser", List.of("+20123456789"),
        List.of(new AddressDto("Egypt", "Cairo", "12511")), LocalDate.of(2000, 1, 1), "https://example.com/profile.jpg",
        "APPLICANT");

    Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(applicant);

    // Act
    UserResponseDto response = userService.registerUser(dto);
    // Assert
    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserEntity.class));
    assert response.getUsername().equals(dto.getUsername());
    assert response.getEmail().equals(dto.getEmail());
    assert response.getFirstName().equals(dto.getFirstName());
    assert response.getLastName().equals(dto.getLastName());
    assert response.getDateOfBirth().equals(dto.getDateOfBirth());
  }

  @Test
  public void registerRecruiterTest_happy() {
    // Assign
    UserRegistrationDto dto = new UserRegistrationDto("newRecruiter", "newPassword123", "recruiter@gmail.com",
        "Mohammed", "Yasser", List.of("+20123456789"),
        List.of(new AddressDto("Egypt", "Cairo", "12511")), LocalDate.of(2000, 1, 1), "https://example.com/profile.jpg",
        "RECRUITER");

    Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(recruiter);

    // Act
    UserResponseDto response = userService.registerUser(dto);
    // Assert
    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserEntity.class));
    assert response.getUsername().equals(dto.getUsername());
    assert response.getEmail().equals(dto.getEmail());
    assert response.getFirstName().equals(dto.getFirstName());
    assert response.getLastName().equals(dto.getLastName());
    assert response.getDateOfBirth().equals(dto.getDateOfBirth());
  }

  @Test
  public void registerUserTest_invalidRole() {
    // Assign
    UserRegistrationDto dto = new UserRegistrationDto("newRecruiter", "newPassword123", "recruiter@gmail.com",
        "Mohammed", "Yasser", List.of("+20123456789"),
        List.of(new AddressDto("Egypt", "Cairo", "12511")), LocalDate.of(2000, 1, 1), "https://example.com/profile.jpg",
        "invalidRole");
    // Act & Assert
    Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(dto));
  }

  @Test
  public void registerUserTest_usernameConflict() {
    // Assign
    UserRegistrationDto dto = new UserRegistrationDto("newRecruiter", "newPassword123", "recruiter@gmail.com",
        "Mohammed", "Yasser", List.of("+20123456789"),
        List.of(new AddressDto("Egypt", "Cairo", "12511")), LocalDate.of(2000, 1, 1), "https://example.com/profile.jpg",
        "RECRUITER");
    Mockito.when(userRepository.findByUsername(dto.getUsername())).thenReturn(Optional.of(recruiter));
    // Act & Assert
    Assertions.assertThrows(UsernameConflictException.class, () -> userService.registerUser(dto));
  }


  //-- Login User tests --//
  @Test
  public void loginApplicantTest_usernameOnly_happy() {
    // Assign
    String username = "applicant";
    String password = "1234topSecret";
    Mockito.when(userRepository.findByUsernameOrEmail(username, "")).thenReturn(java.util.Optional.of(applicant));
    // Act
    UserResponseDto response = userService.loginUser(new UserLoginDto(username, password, ""));
    // Assert
    Mockito.verify(userRepository, Mockito.times(1)).findByUsernameOrEmail(username, "");
    assert response.getUsername().equals(applicant.getUsername());
    assert response.getEmail().equals(applicant.getEmail());
    assert response.getFirstName().equals(applicant.getFirstName());
    assert response.getLastName().equals(applicant.getLastName());
  }

  @Test
  public void loginApplicantTest_emailOnly_happy() {
    // Assign
    String email = "applicant@gmail.com";
    String password = "1234topSecret";
    Mockito.when(userRepository.findByUsernameOrEmail("", email)).thenReturn(java.util.Optional.of(applicant));
    // Act
    UserResponseDto response = userService.loginUser(new UserLoginDto("", password, email));
    // Assert
    Mockito.verify(userRepository, Mockito.times(1)).findByUsernameOrEmail("", email);
    assert response.getUsername().equals(applicant.getUsername());
    assert response.getEmail().equals(applicant.getEmail());
    assert response.getFirstName().equals(applicant.getFirstName());
    assert response.getLastName().equals(applicant.getLastName());
  }

  @Test
  public void loginApplicantTest_usernameAndEmail_happy() {
    // Assign
    String username = "applicant";
    String email = "applicant@gmail.com";
    String password = "1234topSecret";
    Mockito.when(userRepository.findByUsernameOrEmail(username, email)).thenReturn(java.util.Optional.of(applicant));
    // Act
    UserResponseDto response = userService.loginUser(new UserLoginDto(username, password, email));
    // Assert
    Mockito.verify(userRepository, Mockito.times(1)).findByUsernameOrEmail(username, email);
    assert response.getUsername().equals(applicant.getUsername());
    assert response.getEmail().equals(applicant.getEmail());
    assert response.getFirstName().equals(applicant.getFirstName());
    assert response.getLastName().equals(applicant.getLastName());
  }

  @Test
  public void loginApplicantTest_notFound() {
    // Assign
    String username = "notFound";
    String email = "notFound@gmail.com";
    Mockito.when(userRepository.findByUsernameOrEmail(username, email)).thenReturn(Optional.empty());
    // Act & Assert
    Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.loginUser(new UserLoginDto(username, "", email)));
  }
}

