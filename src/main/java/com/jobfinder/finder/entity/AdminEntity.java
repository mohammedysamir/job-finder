package com.jobfinder.finder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "admin",
    indexes = {
        @jakarta.persistence.Index(name = "idx_admin_username", columnList = "username", unique = true),
        @jakarta.persistence.Index(name = "idx_admin_email", columnList = "email", unique = true)
    }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
