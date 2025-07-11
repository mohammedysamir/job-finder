package com.jobfinder.finder.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserResponseDto {
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private List<String> phoneNumber;
  private List<AddressDto> address;
  private String profilePictureUrl;
}
