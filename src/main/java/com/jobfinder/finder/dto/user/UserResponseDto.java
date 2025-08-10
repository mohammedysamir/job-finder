package com.jobfinder.finder.dto.user;

import java.io.Serializable;
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
public class UserResponseDto implements Serializable {
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private List<String> phoneNumbers;
  private List<AddressDto> addresses;
  private LocalDate dateOfBirth; // Format: "yyyy-MM-dd"
  private String imageUrl;
}
