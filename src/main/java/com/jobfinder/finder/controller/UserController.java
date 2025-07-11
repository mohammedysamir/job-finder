package com.jobfinder.finder.controller;

import com.jobfinder.finder.dto.UserPatchDto;
import com.jobfinder.finder.dto.UserRegistrationDto;
import com.jobfinder.finder.dto.UserResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
@Slf4j
public class UserController {
  // - loginUser

  @PostMapping
  public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRegistrationDto dto) {
    log.info("Registering a new user:{}", dto.toString());
    return new ResponseEntity<>(new UserResponseDto(), HttpStatus.CREATED);
  }

  @GetMapping("/{username}/profile")
  public ResponseEntity<UserResponseDto> getUserProfile(@PathVariable String username) {
    log.info("Fetching user profile for username: {}", username);
    return new ResponseEntity<>(new UserResponseDto(), HttpStatus.OK);
  }

  @PatchMapping("/{username}/profile")
  public ResponseEntity<UserResponseDto> updateUserProfile(@PathVariable String username, @RequestBody UserPatchDto dto) {
    log.info("Updating user profile for username: {} with data: {}", username, dto.toString());
    return new ResponseEntity<>(new UserResponseDto(), HttpStatus.OK);
  }

  @DeleteMapping("/{username}")
  public ResponseEntity<Void> deleteUser(@PathVariable String username) {
    log.info("Deleting a user for username: {}", username);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
