package com.jobfinder.finder.integrationTest.service;

import com.jobfinder.finder.config.redis.RedisConfiguration;
import com.jobfinder.finder.constant.EmploymentType;
import com.jobfinder.finder.constant.PostStatus;
import com.jobfinder.finder.constant.Roles;
import com.jobfinder.finder.constant.SubmissionStatus;
import com.jobfinder.finder.dto.submission.SubmissionFilterRequestDto;
import com.jobfinder.finder.dto.submission.SubmissionRequestDto;
import com.jobfinder.finder.dto.submission.SubmissionResponseDto;
import com.jobfinder.finder.entity.PostEntity;
import com.jobfinder.finder.entity.SubmissionEntity;
import com.jobfinder.finder.entity.UserEntity;
import com.jobfinder.finder.mapper.SubmissionMapper;
import com.jobfinder.finder.repository.PostRepository;
import com.jobfinder.finder.repository.SubmissionRepository;
import com.jobfinder.finder.repository.UserRepository;
import com.jobfinder.finder.service.SubmissionService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.User;

@SpringBootTest
public class SubmissionServiceTest extends CacheTestIntializer {


  @SpyBean
  SubmissionMapper submissionMapper;

  @MockBean
  SubmissionRepository submissionRepository;

  @MockBean
  PostRepository postRepository;

  @MockBean
  UserRepository userRepository;

  @Autowired
  SubmissionService submissionService;

  @Autowired
  CacheManager cacheManager;

  SubmissionEntity submittedEntity = new SubmissionEntity(
      UUID.randomUUID().toString(), "mohammed", 1L, "coverLetterUrl", "resumeUrl", SubmissionStatus.SUBMITTED, LocalDate.of(2025, 7, 31));

  SubmissionEntity acceptedEntity = new SubmissionEntity(
      UUID.randomUUID().toString(), "mohammed", 2L, "coverLetterUrl", "resumeUrl", SubmissionStatus.ACCEPTED, LocalDate.of(2025, 7, 24));

  SubmissionEntity rejectedEntity = new SubmissionEntity(
      UUID.randomUUID().toString(), "mohammed", 3L, "coverLetterUrl", "resumeUrl", SubmissionStatus.REJECTED, LocalDate.of(2024, 5, 10));

  SubmissionEntity reviewedEntity = new SubmissionEntity(
      UUID.randomUUID().toString(), "mohammed", 4L, "coverLetterUrl", "resumeUrl", SubmissionStatus.REVIEWED, LocalDate.of(2025, 6, 10));

  //-- Get Submission Tests --//
  @Test
  public void getSubmissionWithFullFilters() {
    SubmissionFilterRequestDto filter = new SubmissionFilterRequestDto("mohammed", 1L, SubmissionStatus.SUBMITTED);

    Page<SubmissionEntity> pageResult = new PageImpl<>(List.of(submittedEntity));
    Mockito.when(submissionRepository.findAll(Mockito.any(Specification.class), Mockito.any(PageRequest.class)))
        .thenReturn(pageResult);

    List<SubmissionResponseDto> result = submissionService.getSubmission(filter, 0, 10);
    Assertions.assertFalse(result.isEmpty());

    assert cacheManager.getCache(RedisConfiguration.CACHE_NAME).get("SubmissionService_getSubmission_mohammed_1_SUBMITTED:0:10") != null;
  }

  @Test
  void getSubmissionWithPartialFilters() {
    SubmissionFilterRequestDto filter = new SubmissionFilterRequestDto("mohammed", null, null);

    Page<SubmissionEntity> pageResult = new PageImpl<>(List.of(submittedEntity));
    Mockito.when(submissionRepository.findAll(Mockito.any(Specification.class), Mockito.any(PageRequest.class)))
        .thenReturn(pageResult);

    List<SubmissionResponseDto> result = submissionService.getSubmission(filter, 0, 10);
    Assertions.assertFalse(result.isEmpty());

    assert cacheManager.getCache(RedisConfiguration.CACHE_NAME).get("SubmissionService_getSubmission_mohammed_null_null:0:10") != null;
  }

  @Test
  public void getSubmissionWithOutFilters() {

    Page<SubmissionEntity> pageResult = new PageImpl<>(List.of(submittedEntity));
    Mockito.when(submissionRepository.findAll(Mockito.any(PageRequest.class)))
        .thenReturn(pageResult);

    List<SubmissionResponseDto> result = submissionService.getSubmission(null, 0, 10);
    Assertions.assertFalse(result.isEmpty());

    assert cacheManager.getCache(RedisConfiguration.CACHE_NAME).get("SubmissionService_getSubmission_0:10") != null;
  }

  //-- Update Submission Status Tests --//
  @Test
  public void updateSubmissionStatusToAccepted() {
    Mockito.when(submissionRepository.findById(Mockito.any(String.class)))
        .thenReturn(java.util.Optional.of(submittedEntity));
    Mockito.when(submissionRepository.save(Mockito.any(SubmissionEntity.class)))
        .thenReturn(acceptedEntity);

    Optional<PostEntity> postEntityOptional = Optional.of(
        new PostEntity(1L, "Software Engineer", "Software engineer description", PostStatus.ACTIVE, "New York", "tech-company", EmploymentType.FULL_TIME, 2, 5,
            List.of("Java", "Spring"), "recruiterUser"));

    Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(postEntityOptional);

    SubmissionResponseDto result = submissionService.updateSubmissionStatus(submittedEntity.getSubmissionId(), SubmissionStatus.ACCEPTED);
    Assertions.assertEquals(SubmissionStatus.ACCEPTED, result.getStatus());
  }

  @Test
  public void updateSubmissionStatusToRejected() {
    Mockito.when(submissionRepository.findById(Mockito.any(String.class)))
        .thenReturn(java.util.Optional.of(submittedEntity));
    Mockito.when(submissionRepository.save(Mockito.any(SubmissionEntity.class)))
        .thenReturn(rejectedEntity);

    Optional<PostEntity> postEntityOptional = Optional.of(
        new PostEntity(1L, "Software Engineer", "Software engineer description", PostStatus.ACTIVE, "New York", "tech-company", EmploymentType.FULL_TIME, 2, 5,
            List.of("Java", "Spring"), "recruiterUser"));

    Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(postEntityOptional);

    SubmissionResponseDto result = submissionService.updateSubmissionStatus(submittedEntity.getSubmissionId(), SubmissionStatus.REJECTED);
    Assertions.assertEquals(SubmissionStatus.REJECTED, result.getStatus());
  }

  //-- Post Submission Tests --//
  @Test
  public void submitPost() {
    Mockito.when(submissionRepository.save(Mockito.any(SubmissionEntity.class)))
        .thenReturn(submittedEntity);

    Optional<PostEntity> postEntityOptional = Optional.of(
        new PostEntity(1L, "Software Engineer", "Software engineer description", PostStatus.ACTIVE, "New York", "tech-company", EmploymentType.FULL_TIME, 2, 5,
            List.of("Java", "Spring"), "recruiterUser"));

    Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(postEntityOptional);
    Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(
        Optional.of(new UserEntity(1L,"mohammed", "password", "mohammedre4a@gmail.com","Mohammed","Yasser",List.of("+20 123456789"),
            List.of(), LocalDate.of(2000, 1, 1), "imageUrl",List.of(), Roles.APPLICANT))
    );
    SubmissionResponseDto result = submissionService.submitPost(
        new SubmissionRequestDto("mohammed", 1L, "coverLetterUrl", "resumeUrl"));

    Assertions.assertEquals(SubmissionStatus.SUBMITTED, result.getStatus());
    Assertions.assertEquals(LocalDate.now(), result.getSubmissionDate());
    Assertions.assertEquals(1L, result.getPostId());
  }
}