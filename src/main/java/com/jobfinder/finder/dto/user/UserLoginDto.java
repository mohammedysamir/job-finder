package com.jobfinder.finder.dto.user;

import com.jobfinder.finder.validator.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserLoginDto {
  private String username;
  @NotBlank(message = "Password cannot be blank")
  private String password;
  @Email
  private String email;
}
