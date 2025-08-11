package com.jobfinder.finder.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddressEntity {
  @NotBlank
  private String country;
  @NotBlank
  private String city;
  @NotBlank
  private String postalCode;

}
