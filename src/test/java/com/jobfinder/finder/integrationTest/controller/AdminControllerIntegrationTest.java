package com.jobfinder.finder.integrationTest.controller;

import com.jobfinder.finder.controller.AdminController;
import com.jobfinder.finder.dto.admin.AdminCreationDto;
import com.jobfinder.finder.dto.admin.AdminPatchDto;
import com.jobfinder.finder.dto.admin.AdminResponseDto;
import com.jobfinder.finder.service.AdminService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
public class AdminControllerIntegrationTest extends FinderIntegrationTestInitiator{

  @MockBean
  private AdminService adminService;

  //-- Get Admin Tests ---
  @Test
  @WithUserDetails("superadmin")
  void getAdmin_superAdmin_happy() throws Exception {
    String username = "admin";
    AdminResponseDto response = new AdminResponseDto(username, "admin@gmail.com");
    Mockito.when(adminService.getAdmin(username)).thenReturn(response);
    mockMvc.perform(MockMvcRequestBuilders.get("/admin/{username}", username))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(username))
        .andExpect(jsonPath("$.email").value("admin@gmail.com"));
  }

  @Test
  @WithUserDetails("admin")
  void getAdmin_admin_happy() throws Exception {
    String username = "admin";
    AdminResponseDto response = new AdminResponseDto(username, "admin@gmail.com");
    Mockito.when(adminService.getAdmin(username)).thenReturn(response);
    mockMvc.perform(MockMvcRequestBuilders.get("/admin/{username}", username))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(username))
        .andExpect(jsonPath("$.email").value("admin@gmail.com"));
  }

  @Test
  @WithUserDetails("applicant")
  void getAdmin_nonAdmin_403() throws Exception {
    String username = "admin";
    AdminResponseDto response = new AdminResponseDto(username, "admin@gmail.com");
    Mockito.when(adminService.getAdmin(username)).thenReturn(response);
    mockMvc.perform(MockMvcRequestBuilders.get("/admin/{username}", username))
        .andExpect(status().isForbidden());
  }

  @Test
  void getAdmin_unauthenticated_401() throws Exception {
    String username = "admin";
    AdminResponseDto response = new AdminResponseDto(username, "admin@gmail.com");
    Mockito.when(adminService.getAdmin(username)).thenReturn(response);
    mockMvc.perform(MockMvcRequestBuilders.get("/admin/{username}", username))
        .andExpect(status().isUnauthorized());
  }


  //-- Delete Admin Tests ---
  @Test
  @WithUserDetails("superadmin")
  void deleteAdmin_superAdmin_happy() throws Exception {
    String username = "admin";
    Mockito.doNothing().when(adminService).deleteAdmin(username);
    mockMvc.perform(MockMvcRequestBuilders.delete("/admin/{username}", username))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithUserDetails("admin")
  void deleteAdmin_nonSuperAdmin_403() throws Exception {
    String username = "admin";
    Mockito.doNothing().when(adminService).deleteAdmin(username);
    mockMvc.perform(MockMvcRequestBuilders.delete("/admin/{username}", username))
        .andExpect(status().isForbidden());
  }


  //-- Create Admin Tests ---
  @Test
  @WithUserDetails("superadmin")
  void createAdmin_superAdmin_happy() throws Exception {
    String username = "admin";
    String mail = "admin@gmail.com";
    AdminCreationDto adminCreationDto = new AdminCreationDto(username, mail, "password1234");
    AdminResponseDto adminResponseDto = new AdminResponseDto(username, mail);
    Mockito.when(adminService.createAdmin(Mockito.any(AdminCreationDto.class))).thenReturn(adminResponseDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/admin", username)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(adminCreationDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value(username))
        .andExpect(jsonPath("$.email").value(mail));
  }

  @Test
  @WithUserDetails("admin")
  void createAdmin_admin_happy() throws Exception {
    String username = "admin";
    String mail = "admin@gmail.com";
    AdminCreationDto adminCreationDto = new AdminCreationDto(username, mail, "password1234");
    AdminResponseDto adminResponseDto = new AdminResponseDto(username, mail);
    Mockito.when(adminService.createAdmin(Mockito.any(AdminCreationDto.class))).thenReturn(adminResponseDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/admin", username)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(adminCreationDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value(username))
        .andExpect(jsonPath("$.email").value(mail));
  }

  @Test
  @WithUserDetails("admin")
  void createAdmin_invalidAttribute_400() throws Exception {
    String username = "admin";
    String mail = "admin@gmail.com";
    AdminCreationDto adminCreationDto = new AdminCreationDto(username, mail, "minPassword");

    mockMvc.perform(MockMvcRequestBuilders.post("/admin", username)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(adminCreationDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("recruiter")
  void createAdmin_nonSuperAdmin_403() throws Exception {
    String username = "admin";
    String mail = "admin@gmail.com";
    AdminCreationDto adminCreationDto = new AdminCreationDto(username, mail, "password1234");

    mockMvc.perform(MockMvcRequestBuilders.post("/admin", username)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(adminCreationDto)))
        .andExpect(status().isForbidden());
  }

  //-- Update Admin Tests ---

  @Test
  @WithUserDetails("admin")
  void updateAdmin_admin_happy() throws Exception {
    String username = "admin";
    String mail = "admin@gmail.com";
    AdminPatchDto adminPatchDto = new AdminPatchDto("password1234", mail);
    AdminResponseDto adminResponseDto = new AdminResponseDto(username, mail);
    Mockito.when(adminService.updateAdmin(Mockito.any(String.class), Mockito.any(AdminPatchDto.class))).thenReturn(adminResponseDto);

    mockMvc.perform(MockMvcRequestBuilders.patch("/admin/{username}", username)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(adminPatchDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(username))
        .andExpect(jsonPath("$.email").value(mail));
  }

  @Test
  @WithUserDetails("superadmin")
  void updateAdmin_superAdmin_happy() throws Exception {
    String username = "admin";
    String mail = "admin@gmail.com";
    AdminPatchDto adminPatchDto = new AdminPatchDto("password1234", mail);
    AdminResponseDto adminResponseDto = new AdminResponseDto(username, mail);
    Mockito.when(adminService.updateAdmin(Mockito.any(String.class), Mockito.any(AdminPatchDto.class))).thenReturn(adminResponseDto);

    mockMvc.perform(MockMvcRequestBuilders.patch("/admin/{username}", username)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(adminPatchDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(username))
        .andExpect(jsonPath("$.email").value(mail));
  }

  @Test
  @WithUserDetails("applicant")
  void updateAdmin_nonAdmin_403() throws Exception {
    String username = "admin";
    String mail = "admin@gmail.com";
    AdminPatchDto adminPatchDto = new AdminPatchDto("password1234", mail);

    mockMvc.perform(MockMvcRequestBuilders.patch("/admin/{username}", username)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(adminPatchDto)))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithUserDetails("admin")
  void updateAdmin_invalidAttribute_400() throws Exception {
    String username = "admin";
    AdminPatchDto adminPatchDto = new AdminPatchDto("password1234", null);

    mockMvc.perform(MockMvcRequestBuilders.patch("/admin/{username}", username)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(adminPatchDto)))
        .andExpect(status().isBadRequest());
  }

}
