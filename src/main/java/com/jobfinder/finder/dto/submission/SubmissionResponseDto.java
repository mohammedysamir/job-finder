package com.jobfinder.finder.dto.submission;

import com.jobfinder.finder.constant.SubmissionStatus;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SubmissionResponseDto {
  @NotBlank
  private Long postId;
  @NotBlank
  private String resumeUrl;
  private String coverLetter;
  private SubmissionStatus status;
  private LocalDate submissionDate;
}
