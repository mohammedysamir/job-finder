package com.jobfinder.finder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AdminCreationDto {
  private String username;
  private String email;
  private String password;
}
