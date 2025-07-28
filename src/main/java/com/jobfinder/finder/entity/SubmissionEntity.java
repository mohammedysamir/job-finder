package com.jobfinder.finder.entity;

import com.jobfinder.finder.constant.SubmissionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "submission")
@AllArgsConstructor
@Getter
@Setter
public class SubmissionEntity {
  @GeneratedValue(strategy = GenerationType.UUID)
  @Id
  private String id;
  @NotBlank
  @Column(unique = true)
  private String username;
  @NotNull
  @Positive
  private Long postId;
  private String coverLetter;
  @NotBlank
  private String resumeUrl;
  @NotBlank
  @Enumerated(value = jakarta.persistence.EnumType.STRING)
  private SubmissionStatus status; // e.g., "submitted", "reviewed", "accepted", "rejected"
  @NotBlank
  @FutureOrPresent
  private LocalDate submissionDate; // ISO 8601 format (e.g., "2023-10-01T12:00:00Z")
}
