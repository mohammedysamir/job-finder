package com.jobfinder.finder.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity(name = "address")
@AllArgsConstructor
@Getter
public class AddressEntity {
  @NotBlank
  private String username;
  @NotBlank
  private String country;
  @NotBlank
  private String city;
  @NotBlank
  private String postalCode;

}
