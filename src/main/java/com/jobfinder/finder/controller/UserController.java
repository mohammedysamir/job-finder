package com.jobfinder.finder.controller;

import com.jobfinder.finder.dto.user.UserLoginDto;
import com.jobfinder.finder.dto.user.UserPatchDto;
import com.jobfinder.finder.dto.user.UserRegistrationDto;
import com.jobfinder.finder.dto.user.UserResponseDto;
import com.jobfinder.finder.service.UserService;
import com.jobfinder.finder.service.VerificationTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User Management Controller", description = "Controller for managing user operations such as registration, profile retrieval, updates, and deletion.")
public class UserController {
  private final UserService userService;
  private final VerificationTokenService verificationTokenService;

  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "User logged in successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid input data"),
          @ApiResponse(responseCode = "401", description = "Unauthorized access"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  @Operation(
      summary = "User Login",
      description = "Endpoint for user login. The user must provide valid credentials including username and password."
  )
  @PostMapping("/login")
  public ResponseEntity<UserResponseDto> loginUser(@RequestBody @Valid UserLoginDto dto) {
    log.info("Logging in user:{}", dto.toString());
    return new ResponseEntity<>(userService.loginUser(dto), HttpStatus.OK);
  }

  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "201", description = "User registered successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid input data"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  @Operation(
      summary = "Register a new user",
      description = "Endpoint to register a new user in the system. The user must provide valid registration details including username, password, and other required fields."
  )
  @PostMapping("/register")
  public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRegistrationDto dto) {
    log.info("Registering a new user:{}", dto.toString());
    return new ResponseEntity<>(userService.registerUser(dto), HttpStatus.CREATED);
  }

  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
          @ApiResponse(responseCode = "404", description = "User not found"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  @Operation(
      summary = "Get User Profile",
      description = "Endpoint to retrieve the profile of a user by their username. Returns user details including username, email, and other profile information."
  )
  @GetMapping("/{username}/profile")
  public ResponseEntity<UserResponseDto> getUserProfile(@PathVariable String username) {
    log.info("Fetching user profile for username: {}", username);
    return new ResponseEntity<>(userService.getUserProfile(username), HttpStatus.OK);
  }

  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "User profile updated successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid input data"),
          @ApiResponse(responseCode = "404", description = "User not found"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  @Operation(
      summary = "Update User Profile",
      description = "Endpoint to update the profile of a user by their username. The user can update fields such as email, phone number, and other profile details."
  )
  @PatchMapping("/{username}/profile")
  public ResponseEntity<UserResponseDto> updateUserProfile(@PathVariable String username,
      @RequestBody UserPatchDto dto) {
    log.info("Updating user profile for username: {} with data: {}", username, dto.toString());
    return new ResponseEntity<>(userService.updateUserProfile(username, dto), HttpStatus.OK);
  }

  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "204", description = "User profile updated successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid input data"),
          @ApiResponse(responseCode = "404", description = "User not found"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  @Operation(
      summary = "Delete User",
      description = "Endpoint to delete a user by their username. This operation will remove the user and all associated data from the system."
  )
  @DeleteMapping("/{username}")
  public ResponseEntity<Void> deleteUser(@PathVariable String username) {
    log.info("Deleting a user for username: {}", username);
    userService.deleteUser(username);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/verify")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "User is verified"),
          @ApiResponse(responseCode = "400", description = "Invalid token"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  @Operation(
      summary = "Verify User Account",
      description = "Endpoint to verify user access using a token. The token must be provided as a request parameter."
  )
  public ResponseEntity<String> verifyUser(@RequestParam String token) {
    log.info("Verifying user access");
    if (token == null || token.isEmpty()) {
      log.warn("Invalid token: Token is null or empty");
      return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);
    }

    log.info("Verifying user with token: {}", token);
    boolean tokenIsValid = verificationTokenService.validateVerificationToken(token);

    if (!tokenIsValid) {
      log.warn("Invalid token: Token does not exist or has expired");
      return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>("User is verified", HttpStatus.OK);
  }
}
