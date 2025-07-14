package com.jobfinder.finder.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDto {
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private List<String> phoneNumber;
  private List<AddressDto> address;
  private String profilePictureUrl;
}
