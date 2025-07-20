package com.jobfinder.finder.controller;

import com.jobfinder.finder.dto.AdminCreationDto;
import com.jobfinder.finder.dto.AdminPatchDto;
import com.jobfinder.finder.dto.AdminResponseDto;
import com.jobfinder.finder.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RequestMapping("/admin")
@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Admin Management Controller", description = "Controller for managing admin operations such as creation, retrieval, updates, and deletion.")
public class AdminController {
  private final AdminService adminService;

  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully retrieved admin details"
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Admin not found"
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error"
          )
      }
  )
  @Operation(
      summary = "Get Admin Details",
      description = "Fetches the details of an admin by their username."
  )
  @GetMapping("/{username}")
  public ResponseEntity<AdminResponseDto> getAdmin(@PathVariable String username) {
    log.info("Fetching admin details for username: {}", username);
    return new ResponseEntity<>(adminService.getAdmin(username), HttpStatus.OK);
  }

  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "204",
              description = "Successfully deleted admin"
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Admin not found"
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error"
          )
      }
  )
  @Operation(
      summary = "Delete Admin",
      description = "Deletes an admin by their username."
  )
  @DeleteMapping("/{username}")
  public ResponseEntity<Void> deleteAdmin(@PathVariable String username) {
    log.info("Deleting an admin with username: {}", username);
    adminService.deleteAdmin(username);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully updated admin details"
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Admin not found"
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error"
          )
      }
  )
  @Operation(
      summary = "Update Admin Details",
      description = "Updates the details of an admin by their username."
  )
  @PatchMapping("/{username}")
  public ResponseEntity<AdminResponseDto> updateAdmin(@PathVariable String username, AdminPatchDto dto) {
    log.info("Patching an admin with username: {} with data: {}", username, dto.toString());
    return new ResponseEntity<>(adminService.updateAdmin(username, dto), HttpStatus.OK);
  }

  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "201",
              description = "Successfully created admin"
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid input data"
          ),
          @ApiResponse(
              responseCode = "409",
              description = "Admin already exists"
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error"
          )
      }
  )
  @Operation(
      summary = "Create Admin",
      description = "Creates a new admin with the provided details."
  )
  @PostMapping
  public ResponseEntity<AdminResponseDto> createAdmin(@RequestBody AdminCreationDto dto) {
    log.info("Creating an admin: {}", dto.toString());
    return new ResponseEntity<>(adminService.createAdmin(dto), HttpStatus.CREATED);
  }
}
