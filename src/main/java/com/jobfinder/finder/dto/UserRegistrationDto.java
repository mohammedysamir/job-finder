package com.jobfinder.finder.dto;

import com.jobfinder.finder.validator.ValidPhoneNumber;
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
  private String username;
  private String password;
  private String email;
  private String firstName;
  private String lastName;
  @ValidPhoneNumber
  private List<String> phoneNumber; // Format: "+countryCode phoneNumber"
  private String address;
  private LocalDate dateOfBirth; // Format: "yyyy-MM-dd"
  private String imageUrl;
}
