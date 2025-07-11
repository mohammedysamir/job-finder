package com.jobfinder.finder.entity;

import com.jobfinder.finder.validator.ValidPhoneNumber;
import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity(name = "phone_number")
@AllArgsConstructor
@Getter
public class PhoneNumberEntity {
  @NotBlank
  private String username;
  @NotBlank
  @ValidPhoneNumber
  private String phoneNumber;
}
