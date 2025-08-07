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
public class PostUpdateDto {
  private String title;
  private String description;
  private String location;
  private String companyName;
  private EmploymentType employmentType; // e.g., Full-time, Part-time, Contract
  private int minimumExperience;
  private int maximumExperience;
  private List<String> skillsRequired;
  private String recruiterUsername; // username of the recruiter who created the post
}
