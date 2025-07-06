package com.jobfinder.finder.entity;

import jakarta.persistence.Entity;
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
  //todo: add regex validation for phone number
  private String phoneNumber;
}
