package com.jobfinder.finder.dto;

import com.jobfinder.finder.validator.ValidPhoneNumber;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class AdminPatchDto {
  private String password;
  private String email;
}
