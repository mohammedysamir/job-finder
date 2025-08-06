package com.jobfinder.finder.controller;

import com.jobfinder.finder.constant.SubmissionStatus;
import com.jobfinder.finder.dto.submission.SubmissionFilterRequestDto;
import com.jobfinder.finder.dto.submission.SubmissionRequestDto;
import com.jobfinder.finder.dto.submission.SubmissionResponseDto;
import com.jobfinder.finder.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/submit")
@RestController
@RequiredArgsConstructor
public class SubmissionController {
  private final SubmissionService submissionService;

  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "201", description = "Successfully submitted post"),
          @ApiResponse(responseCode = "400", description = "Invalid submission data provided"),
          @ApiResponse(responseCode = "404", description = "Post is closed/suspended or not found"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  @Operation(summary = "Submit Post", description = "Submits a post with the provided submission details.")
  @PostMapping
  public ResponseEntity<SubmissionResponseDto> submitPost(@RequestBody @Valid SubmissionRequestDto submissionRequestDto) {
    log.info("Submitting post with request: {}", submissionRequestDto);
    SubmissionResponseDto response = submissionService.submitPost(submissionRequestDto);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "Successfully fetched submissions"),
          @ApiResponse(responseCode = "400", description = "Invalid filter parameters provided"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  @Operation(summary = "Get Submissions", description = "Fetches submissions based on the provided filter criteria.")
  @GetMapping
  public ResponseEntity<List<SubmissionResponseDto>> getSubmissions(
      @Valid @ModelAttribute SubmissionFilterRequestDto submissionFilterRequestDto, int page, int size) {
    log.info("Fetching submissions with request: {}", submissionFilterRequestDto);
    List<SubmissionResponseDto> response = submissionService.getSubmission(submissionFilterRequestDto, page, size);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "Successfully updated submission status"),
          @ApiResponse(responseCode = "400", description = "Invalid submission status provided"),
          @ApiResponse(responseCode = "404", description = "Submission not found"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  @Operation(summary = "Update Submission Status", description = "Updates the status of a submission.")
  @PatchMapping("/{submissionId}/status")
  public ResponseEntity<SubmissionResponseDto> updateSubmissionStatus(
      @PathVariable String submissionId, @RequestParam SubmissionStatus status) {
    log.info("Updating submission's {} status to: {}", submissionId, status);
    SubmissionResponseDto response = submissionService.updateSubmissionStatus(submissionId, status);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
