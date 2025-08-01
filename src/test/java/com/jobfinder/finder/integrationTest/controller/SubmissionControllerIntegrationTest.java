package com.jobfinder.finder.integrationTest.controller;

import com.jobfinder.finder.constant.SubmissionStatus;
import com.jobfinder.finder.controller.SubmissionController;
import com.jobfinder.finder.dto.submission.SubmissionFilterRequestDto;
import com.jobfinder.finder.dto.submission.SubmissionRequestDto;
import com.jobfinder.finder.dto.submission.SubmissionResponseDto;
import com.jobfinder.finder.service.SubmissionService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubmissionController.class)
public class SubmissionControllerIntegrationTest extends FinderIntegrationTestInitiator {

  @MockBean
  private SubmissionService submissionService;

  Long postId = 1L;
  LocalDate submittedAt = LocalDate.of(2025, 1, 1);
  String resumeUrl = "http://example.com/resume.pdf";
  String coverLetter = "This is a cover letter.";

  //-- Post Submissions Tests ---
  @Test
  @WithUserDetails("applicant")
  void submitPost_happy() throws Exception {
    // Given
    SubmissionRequestDto submissionRequestDto = new SubmissionRequestDto(
        "applicant",
        postId,
        resumeUrl,
        coverLetter
    );
    SubmissionResponseDto response = new SubmissionResponseDto(
        postId,
        resumeUrl,
        coverLetter,
        SubmissionStatus.SUBMITTED,
        submittedAt
    );

    Mockito.when(submissionService.submitPost(Mockito.any(SubmissionRequestDto.class))).thenReturn(
        response
    );
    // When
    mockMvc.perform(
            MockMvcRequestBuilders.post("/submit")
                .content(objectMapper.writeValueAsString(submissionRequestDto))
                .contentType("application/json")
        ).andExpect(status().isCreated())
        .andExpect(jsonPath("$.postId").value(postId))
        .andExpect(jsonPath("$.status").value(SubmissionStatus.SUBMITTED.name()))
        .andExpect(jsonPath("$.submissionDate").value(submittedAt.toString()));
  }

  @Test
  @WithUserDetails("applicant")
  void submitPost_missingUsername_400() throws Exception {
    // Given
    SubmissionRequestDto submissionRequestDto = new SubmissionRequestDto(
        null,
        postId,
        resumeUrl,
        coverLetter
    );
    // When
    mockMvc.perform(
        MockMvcRequestBuilders.post("/submit")
            .content(objectMapper.writeValueAsString(submissionRequestDto))
            .contentType("application/json")
    ).andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("applicant")
  void submitPost_invalidPostId_400() throws Exception {
    // Given
    SubmissionRequestDto submissionRequestDto = new SubmissionRequestDto(
        "applicant",
        -1L,
        resumeUrl,
        coverLetter
    );
    // When
    mockMvc.perform(
        MockMvcRequestBuilders.post("/submit")
            .content(objectMapper.writeValueAsString(submissionRequestDto))
            .contentType("application/json")
    ).andExpect(status().isBadRequest());
  }

  @Test
  void submitPost_unauthenticated_401() throws Exception {
    // Given
    SubmissionRequestDto submissionRequestDto = new SubmissionRequestDto(
        "applicant",
        postId,
        resumeUrl,
        coverLetter
    );
    // When
    mockMvc.perform(
        MockMvcRequestBuilders.post("/submit")
            .content(objectMapper.writeValueAsString(submissionRequestDto))
            .contentType("application/json")
    ).andExpect(status().isUnauthorized());
  }

  @Test
  @WithUserDetails("admin")
  void submitPost_unauthorized_403() throws Exception {
    // Given
    SubmissionRequestDto submissionRequestDto = new SubmissionRequestDto(
        "applicant",
        postId,
        resumeUrl,
        coverLetter
    );
    // When
    mockMvc.perform(
        MockMvcRequestBuilders.post("/submit")
            .content(objectMapper.writeValueAsString(submissionRequestDto))
            .contentType("application/json")
    ).andExpect(status().isForbidden());
  }

  //-- Get Submissions Tests ---

  @Test
  @WithUserDetails("applicant")
  void getSubmission_usernameAndStatusAndNullPostId_happy() throws Exception {
    // Given
    SubmissionFilterRequestDto submissionFilterRequestDto = new SubmissionFilterRequestDto(
        "applicant",
        null,
        SubmissionStatus.ACCEPTED
    );

    List<SubmissionResponseDto> response = List.of(
        new SubmissionResponseDto(
            20134L,
            resumeUrl,
            coverLetter,
            SubmissionStatus.ACCEPTED,
            LocalDate.of(2025, 1, 5)
        ),
        new SubmissionResponseDto(
            39L,
            resumeUrl,
            coverLetter,
            SubmissionStatus.ACCEPTED,
            LocalDate.of(2025, 5, 21)
        )
    );

    Mockito.when(submissionService.getSubmission(Mockito.any(SubmissionFilterRequestDto.class), Mockito.any(Integer.class), Mockito.any(Integer.class)))
        .thenReturn(
            response
        );

    // When
    mockMvc.perform(
            MockMvcRequestBuilders.get("/submit")
                .param("username", "applicant")
                .param("submissionStatus", SubmissionStatus.ACCEPTED.name())
                .param("page", "0")
                .param("size", "10")
                .contentType("application/json")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].postId").value(20134L))
        .andExpect(jsonPath("$[0].status").value(submissionFilterRequestDto.getSubmissionStatus().name()))
        .andExpect(jsonPath("$[0].submissionDate").value(LocalDate.of(2025, 1, 5).toString()))
        .andExpect(jsonPath("$[1].postId").value(39L))
        .andExpect(jsonPath("$[1].status").value(submissionFilterRequestDto.getSubmissionStatus().name()))
        .andExpect(jsonPath("$[1].submissionDate").value(LocalDate.of(2025, 5, 21).toString()));
  }

  @Test
  @WithUserDetails("applicant")
  void getSubmission_postId_happy() throws Exception {
    // Given
    SubmissionFilterRequestDto submissionFilterRequestDto = new SubmissionFilterRequestDto(
        "applicant",
        20134L,
        SubmissionStatus.ACCEPTED
    );

    List<SubmissionResponseDto> response = List.of(
        new SubmissionResponseDto(
            20134L,
            resumeUrl,
            coverLetter,
            SubmissionStatus.ACCEPTED,
            LocalDate.of(2025, 1, 5)
        )
    );

    Mockito.when(submissionService.getSubmission(Mockito.any(SubmissionFilterRequestDto.class), Mockito.any(Integer.class), Mockito.any(Integer.class)))
        .thenReturn(
            response
        );

    // When
    mockMvc.perform(
            MockMvcRequestBuilders.get("/submit")
                .param("username", "applicant")
                .param("postId", "20134")
                .param("page", "0")
                .param("size", "10")
                .contentType("application/json")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].postId").value(20134L))
        .andExpect(jsonPath("$[0].status").value(submissionFilterRequestDto.getSubmissionStatus().name()))
        .andExpect(jsonPath("$[0].submissionDate").value(LocalDate.of(2025, 1, 5).toString()));
  }

  @Test
  @WithUserDetails("admin")
  void getSubmission_unauthorized_403() throws Exception {
    // When
    mockMvc.perform(
        MockMvcRequestBuilders.get("/submit")
            .param("username", "applicant")
            .param("postId", "20134")
            .param("page", "0")
            .param("size", "10")
            .contentType("application/json")
    ).andExpect(status().isForbidden());
  }


  //-- Update Submission Status Tests ---
  @Test
  @WithUserDetails("recruiter")
  void updateSubmissionStatus_happy() throws Exception {
    // Given
    String submissionId = UUID.randomUUID().toString();
    SubmissionStatus status = SubmissionStatus.ACCEPTED;

    SubmissionResponseDto response = new SubmissionResponseDto(
        postId,
        resumeUrl,
        coverLetter,
        status,
        submittedAt
    );

    Mockito.when(submissionService.updateSubmissionStatus(Mockito.anyString(), Mockito.any(SubmissionStatus.class)))
        .thenReturn(response);

    // When
    mockMvc.perform(
            MockMvcRequestBuilders.patch("/submit/" + submissionId + "/status")
                .param("status", status.name())
                .contentType("application/json")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.postId").value(postId))
        .andExpect(jsonPath("$.status").value(status.name()))
        .andExpect(jsonPath("$.submissionDate").value(submittedAt.toString()));
  }

  @Test
  @WithUserDetails("recruiter")
  void updateSubmissionStatus_invalidStatus() throws Exception {
    // Given
    String submissionId = UUID.randomUUID().toString();
    SubmissionStatus status = SubmissionStatus.ACCEPTED;

    SubmissionResponseDto response = new SubmissionResponseDto(
        postId,
        resumeUrl,
        coverLetter,
        status,
        submittedAt
    );

    Mockito.when(submissionService.updateSubmissionStatus(Mockito.anyString(), Mockito.any(SubmissionStatus.class)))
        .thenReturn(response);

    // When
    mockMvc.perform(
        MockMvcRequestBuilders.patch("/submit/" + submissionId + "/status")
            .param("status", "blabla")
            .contentType("application/json")
    ).andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("applicant")
  void updateSubmissionStatus_unauthorized() throws Exception {
    // Given
    long submissionId = 1L;
    SubmissionStatus status = SubmissionStatus.ACCEPTED;

    // When
    mockMvc.perform(
        MockMvcRequestBuilders.patch("/submit/" + submissionId + "/status")
            .param("status", status.name())
            .contentType("application/json")
    ).andExpect(status().isForbidden());
  }
}
