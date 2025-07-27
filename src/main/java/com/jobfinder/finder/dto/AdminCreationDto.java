package com.jobfinder.finder.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminCreationDto {
  @NotBlank(message = "Username cannot be blank")
  private String username;
  @Email
  @NotBlank(message = "Email cannot be blank")
  private String email;
  @NotBlank(message = "Password cannot be blank")
  @Size(min = 12, max = 25, message = "Password must be between 12 and 25 characters")
  private String password;
}
