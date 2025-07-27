package com.jobfinder.finder.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AdminPatchDto {
  @NotBlank(message = "Password cannot be blank")
  @Size(min = 12, max = 25, message = "Password must be between 12 and 25 characters")
  private String password;
  @NotBlank
  @Email
  private String email;
}
