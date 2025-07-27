package com.jobfinder.finder.service;

import com.jobfinder.finder.dto.admin.AdminCreationDto;
import com.jobfinder.finder.dto.admin.AdminPatchDto;
import com.jobfinder.finder.dto.admin.AdminResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminService {
  public AdminResponseDto getAdmin(String username) {
    log.info("Fetching admin details for username: {}", username);
    // todo: fetch from database and map
    return null;
  }

  public void deleteAdmin(String username) {
    log.info("Deleting an admin with username: {}", username);
    //todo: delete from database
  }

  public AdminResponseDto updateAdmin(String username, AdminPatchDto dto) {
    log.info("Patching an admin with username: {} with data: {}", username, dto);
    //map and update in database.
    return null;
  }

  public AdminResponseDto createAdmin(AdminCreationDto dto) {
    log.info("Creating an admin: {}", dto);
    //map and save to database.
    return null;
  }

}
