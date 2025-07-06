package com.jobfinder.finder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "user")
@AllArgsConstructor
@Getter
public class UserEntity {
  @GeneratedValue(strategy = IDENTITY)
  @Id
  private Long id;
  @NotBlank
  private String username;
  @NotBlank
  private String password;
  @Email
  private String email;
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @OneToMany(mappedBy = "user")
  @JoinColumn(name = "username")
  @NotEmpty
  private List<PhoneNumberEntity> phoneNumbers;
  @OneToMany(mappedBy = "user")
  @JoinColumn(name = "username")
  @NotEmpty
  private List<AddressEntity> addresses;
  @Past
  @NotNull
  private LocalDate dateOfBirth;
  @NotBlank
  private String imageUrl;
  @OneToMany(mappedBy = "user")
  @JoinColumn(name = "username")
  List<SubmissionEntity> submissions;
}
