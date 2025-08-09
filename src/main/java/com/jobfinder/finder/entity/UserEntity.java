package com.jobfinder.finder.entity;

import com.jobfinder.finder.constant.Roles;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_username", columnList = "username", unique = true),
        @Index(name = "idx_email", columnList = "email", unique = true),
        @Index(name = "idx_role", columnList = "role")
    }
)
@AllArgsConstructor
@NoArgsConstructor
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

  @NotEmpty
  @JoinTable(
      name = "user_phone_numbers",
      joinColumns = @JoinColumn(name = "id"),
      inverseJoinColumns = @JoinColumn(name = "phone_number")
  )
  private List<String> phoneNumbers;

  @NotEmpty
  @ElementCollection
  private List<AddressEntity> addresses;

  @Past
  @NotNull
  private LocalDate dateOfBirth;

  @NotBlank
  private String imageUrl;

  @OneToMany(mappedBy = "username")
  List<SubmissionEntity> submissions;

  @NotNull
  Roles role; // "Applicant" or "Recruiter"
  //todo: add user status, e.g. CREATED,VERIFIED , SUSPENDED, DELETED
  /*
  @NotNull
  UserStatus status;

   */
}
