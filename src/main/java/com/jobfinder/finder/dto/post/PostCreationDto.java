package com.jobfinder.finder.dto.post;

import com.jobfinder.finder.constant.EmploymentType;
import com.jobfinder.finder.constant.PostStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostCreationDto {
  @NotBlank
  private String title;
  @NotBlank
  @Size(max = 500, message = "Description must be less than 500 characters")
  private String description;
  @NotBlank
  private String location;
  @NotBlank
  private String companyName;
  @NotNull
  private EmploymentType employmentType; // e.g., Full-time, Part-time, Contract
  @Min(value = 0, message = "Minimum experience cannot be negative")
  private int minimumExperience;
  @Max(value = 50, message = "Maximum experience cannot exceed 50 years")
  private int maximumExperience;
  @NotEmpty
  private List<String> skillsRequired;
  @NotBlank
  private String recruiterUsername; // username of the recruiter who created the post
}
