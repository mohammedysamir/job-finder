package com.jobfinder.finder.service;

import com.jobfinder.finder.dto.UserPatchDto;
import com.jobfinder.finder.dto.UserRegistrationDto;
import com.jobfinder.finder.dto.UserResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

  public UserResponseDto registerUser(UserRegistrationDto dto) {
    log.info("Registering a new user: {}", dto.toString());
    //todo: map to entity and save to database
    return null; // Return the registered user details
  }

  public UserResponseDto getUserProfile(String username) {
    log.info("Fetching user profile for username: {}", username);
    //todo: fetch from database
    return null;
  }

  public UserResponseDto updateUserProfile(String username, UserPatchDto dto) {
    log.info("Updating user profile for username: {} with data: {}", username, dto.toString());
    //todo: map to entity and update in database
    return null;
  }

  public void deleteUser(String username) {
    log.info("Deleting a user for username: {}", username);
    //todo: delete from database
  }
}
