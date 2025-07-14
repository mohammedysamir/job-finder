package com.jobfinder.finder.controller;

import com.jobfinder.finder.dto.UserPatchDto;
import com.jobfinder.finder.dto.UserRegistrationDto;
import com.jobfinder.finder.dto.UserResponseDto;
import com.jobfinder.finder.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
  // - loginUser
  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRegistrationDto dto) {
    log.info("Registering a new user:{}", dto.toString());
    return new ResponseEntity<>(userService.registerUser(dto), HttpStatus.CREATED);
  }

  @GetMapping("/{username}/profile")
  public ResponseEntity<UserResponseDto> getUserProfile(@PathVariable String username) {
    log.info("Fetching user profile for username: {}", username);
    return new ResponseEntity<>(userService.getUserProfile(username), HttpStatus.OK);
  }

  @PatchMapping("/{username}/profile")
  public ResponseEntity<UserResponseDto> updateUserProfile(@PathVariable String username, @RequestBody UserPatchDto dto) {
    log.info("Updating user profile for username: {} with data: {}", username, dto.toString());
    return new ResponseEntity<>(userService.updateUserProfile(username, dto), HttpStatus.OK);
  }

  @DeleteMapping("/{username}")
  public ResponseEntity<Void> deleteUser(@PathVariable String username) {
    log.info("Deleting a user for username: {}", username);
    userService.deleteUser(username);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
