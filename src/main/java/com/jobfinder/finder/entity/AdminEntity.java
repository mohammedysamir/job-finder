package com.jobfinder.finder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity(name = "admin")
@Getter
@AllArgsConstructor
public class AdminEntity {
  @GeneratedValue(strategy = GenerationType.UUID)
  @Id
  private Long id;
  @NotBlank
  private String username;
  @NotBlank
  private String password;
  @NotBlank
  private String email;
}
