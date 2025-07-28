package com.jobfinder.finder.dto.submission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SubmissionRequestDto {
  @NotBlank
  private String username;
  @NotNull
  @Positive
  private Long postId;
  private String coverLetter;
  @NotBlank
  private String resumeUrl;
}
