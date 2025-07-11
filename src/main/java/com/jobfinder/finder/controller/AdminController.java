package com.jobfinder.finder.controller;

import com.jobfinder.finder.dto.AdminCreationDto;
import com.jobfinder.finder.dto.AdminResponseDto;
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

@RestController("/admin")
@Slf4j
public class AdminController {
  @GetMapping("/{username}")
  public ResponseEntity<AdminResponseDto> getAdmin(@PathVariable String username) {
    log.info("Fetching admin details for username: {}", username);
    return new ResponseEntity<>(null, HttpStatus.OK);
  }

  @DeleteMapping("/{username}")
  public ResponseEntity<AdminResponseDto> deleteAdmin(@PathVariable String username) {
    log.info("Deleting an admin with username: {}", username);
    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
  }

  @PatchMapping("/{username}")
  public ResponseEntity<AdminResponseDto> updateAdmin(@PathVariable String username) {
    log.info("Patching an admin with username: {}", username);
    return new ResponseEntity<>(null, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<AdminResponseDto> createAdmin(@RequestBody AdminCreationDto dto) {
    log.info("Creating an admin: {}", dto.toString());
    return new ResponseEntity<>(null, HttpStatus.CREATED);
  }
}
