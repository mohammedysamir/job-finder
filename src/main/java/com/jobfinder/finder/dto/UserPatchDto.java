package com.jobfinder.finder.dto;

import com.jobfinder.finder.validator.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
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
  @Size(min = 12, max = 25)
  private String password;
  private String email;
  private String firstName;
  private String lastName;
  @ValidPhoneNumber
  private List<String> phoneNumber; // Format: "+countryCode phoneNumber"
  private List<AddressDto> address;
  private LocalDate dateOfBirth; // Format: "yyyy-MM-dd"
  private String imageUrl;
}
