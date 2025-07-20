package com.jobfinder.finder.entity;

import com.jobfinder.finder.validator.ValidPhoneNumber;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity(name = "phone_number")
@AllArgsConstructor
@Getter
public class PhoneNumberEntity {
  //todo: maybe we can define a composite key with username and phone number
  // but for now, we will use a simple primary key
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  @NotBlank
  @Column(unique = true)
  private String username;
  @NotBlank
  @ValidPhoneNumber
  private String phoneNumber;
}
