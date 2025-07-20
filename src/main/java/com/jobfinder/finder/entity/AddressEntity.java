package com.jobfinder.finder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity(name = "address")
@AllArgsConstructor
@Getter
public class AddressEntity {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  @NotBlank
  @Column(unique = true)
  private String username;
  @NotBlank
  private String country;
  @NotBlank
  private String city;
  @NotBlank
  private String postalCode;

}
