package com.jobfinder.finder.dto.user;

import com.jobfinder.finder.validator.ValidPhoneNumber;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserPatchDto {
  private String password;
  private String email;
  private String firstName;
  private String lastName;
  @ValidPhoneNumber
  private List<String> phoneNumbers; // Format: "+countryCode phoneNumber"
  private List<AddressDto> addresses;
  private LocalDate dateOfBirth; // Format: "yyyy-MM-dd"
  private String imageUrl;
}
