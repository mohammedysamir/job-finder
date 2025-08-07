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
public class UserRegistrationDto {
  @NotBlank(message = "Username cannot be blank")
  private String username;
  @NotBlank(message = "Password cannot be blank")
  private String password;
  @Email
  @NotBlank(message = "Email cannot be blank")
  private String email;
  @NotBlank(message = "First name cannot be blank")
  private String firstName;
  @NotBlank(message = "Last name cannot be blank")
  private String lastName;
  @ValidPhoneNumber
  private List<String> phoneNumbers;// Format: "+countryCode phoneNumber"
  private List<AddressDto> addresses;
  private LocalDate dateOfBirth; // Format: "yyyy-MM-dd"
  private String imageUrl;
  private String role; // "Applicant" or "Recruiter"
}
