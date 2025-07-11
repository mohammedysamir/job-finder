package com.jobfinder.finder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddressDto {
  private String country;
  private String city;
  private String postalCode;
}
