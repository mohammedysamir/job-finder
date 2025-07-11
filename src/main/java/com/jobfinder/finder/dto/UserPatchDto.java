package com.jobfinder.finder.dto;

import com.jobfinder.finder.validator.ValidPhoneNumber;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class UserPatchDto {
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
