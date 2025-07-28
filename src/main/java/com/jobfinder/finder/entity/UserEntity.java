package com.jobfinder.finder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

//todo: create index on username and email
@Entity(name = "user")
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {
  @GeneratedValue(strategy = IDENTITY)
  @Id
  private Long id;
  @NotBlank
  @Column(unique = true)
  private String username;
  @NotBlank
  private String password;
  @Email
  @NotBlank
  @Column(unique = true)
  private String email;
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @OneToMany(mappedBy = "username")
  @NotEmpty
  @JoinTable(
      name = "user_phone_numbers",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "phone_number_id")
  )
  private List<String> phoneNumbers;
  @OneToMany(mappedBy = "username")
  @NotEmpty
  private List<AddressEntity> addresses;
  @Past
  @NotNull
  private LocalDate dateOfBirth;
  @NotBlank
  private String imageUrl;
  @OneToMany(mappedBy = "username")
  List<SubmissionEntity> submissions;
}
