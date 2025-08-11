package com.jobfinder.finder.dto.submission;

import com.jobfinder.finder.constant.SubmissionStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubmissionStatusQueueMessage {
  private String submissionId;
  private String username;
  private Long postId;
  private SubmissionStatus status;
  private LocalDate submissionDate;

  @Override
  public String toString() {
    return "SubmissionStatusQueueMessage{" +
        "submissionId='" + submissionId + '\'' +
        ", postId='" + postId + '\'' +
        ", status='" + status + '\'' +
        ", username='" + username + '\'' +
        ", submissionDate=" + submissionDate +
        '}';
  }
}
