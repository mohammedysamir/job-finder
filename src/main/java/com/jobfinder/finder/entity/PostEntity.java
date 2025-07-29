package com.jobfinder.finder.entity;

import com.jobfinder.finder.constant.EmploymentType;
import com.jobfinder.finder.constant.PostStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(
    name = "posts",
    indexes = {
        @jakarta.persistence.Index(name = "idx_post_title", columnList = "title"),
        @jakarta.persistence.Index(name = "idx_post_location", columnList = "location"),
        @jakarta.persistence.Index(name = "idx_post_employment_type", columnList = "employmentType"),
    }
)
@AllArgsConstructor
@Getter
@Setter
public class PostEntity {
  @GeneratedValue(strategy = IDENTITY)
  @Id
  private Long id;
  @NotBlank
  private String title;
  @NotBlank
  private String description;
  @NotBlank
  @Enumerated(value = jakarta.persistence.EnumType.STRING)
  private PostStatus status; // e.g., "active", "closed", "suspended"
  @NotBlank
  private String location;
  @NotBlank
  private String companyName;
  @NotBlank
  @Enumerated(value = jakarta.persistence.EnumType.STRING)
  private EmploymentType employmentType; // e.g., Full-time, Part-time, Contract
  @NotBlank
  @Min(0)
  private int minimumExperience;
  @NotBlank
  private int maximumExperience;
  @NotEmpty
  private List<String> skillsRequired; // Comma-separated list of skills
  @NotBlank
  private String recruiterUsername; // username of the recruiter who created the post
}
