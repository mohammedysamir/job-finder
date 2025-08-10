package com.jobfinder.finder.dto.post;

import com.jobfinder.finder.constant.EmploymentType;
import com.jobfinder.finder.constant.PostStatus;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostResponseDto implements Serializable {
  private Long id; // Unique identifier for the post
  private String title;
  private String description;
  private PostStatus status; // e.g., "active", "closed", "suspended"
  private String location;
  private String companyName;
  private EmploymentType employmentType; // e.g., Full-time, Part-time, Contract
  private int minimumExperience;
  private int maximumExperience;
  private List<String> skillsRequired;
  private String recruiterUsername; // username of the recruiter who created the post
}
