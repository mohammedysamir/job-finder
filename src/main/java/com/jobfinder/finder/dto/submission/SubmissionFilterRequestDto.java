package com.jobfinder.finder.dto.submission;

import com.jobfinder.finder.constant.SubmissionStatus;
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
public class SubmissionFilterRequestDto {
  @NotBlank
  private String username;
  @Positive
  private Long postId;
  private SubmissionStatus submissionStatus;
}
