package com.jobfinder.finder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity(name = "admin")
@Getter
@AllArgsConstructor
public class AdminEntity {
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id
  private Long id;
  @NotBlank
  @Column(unique = true)
  private String username;
  @NotBlank
  private String password;
  @NotBlank
  @Email
  @Column(unique = true)
  private String email;
}
