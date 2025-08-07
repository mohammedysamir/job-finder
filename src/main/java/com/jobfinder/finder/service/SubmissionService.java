package com.jobfinder.finder.service;

import com.jobfinder.finder.config.redis.RedisConfiguration;
import com.jobfinder.finder.constant.PostStatus;
import com.jobfinder.finder.constant.SubmissionStatus;
import com.jobfinder.finder.dto.submission.SubmissionFilterRequestDto;
import com.jobfinder.finder.dto.submission.SubmissionRequestDto;
import com.jobfinder.finder.dto.submission.SubmissionResponseDto;
import com.jobfinder.finder.entity.PostEntity;
import com.jobfinder.finder.entity.SubmissionEntity;
import com.jobfinder.finder.mapper.SubmissionMapper;
import com.jobfinder.finder.repository.PostRepository;
import com.jobfinder.finder.repository.SubmissionRepository;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubmissionService {
  private final SubmissionRepository submissionRepository;
  private final PostRepository postRepository;
  private final SubmissionMapper submissionMapper;

  @Cacheable(cacheNames = RedisConfiguration.CACHE_NAME, keyGenerator = "customRedisKeyGenerator")
  public List<SubmissionResponseDto> getSubmission(SubmissionFilterRequestDto filter, int page, int size) {
    log.info("Fetching posts with filter: {}", filter);
    PageRequest pageRequest = PageRequest.of(page, size);

    Page<SubmissionEntity> pageResult;
    if (null != filter) {
      log.info("Fetching posts with filter: {}", filter);
      Specification<SubmissionEntity> spec = withFilters(filter);
      pageResult = submissionRepository.findAll(spec, pageRequest);
    } else {
      log.info("Fetching posts without filters");
      pageResult = submissionRepository.findAll(pageRequest);
    }
    return pageResult.stream().map(submissionMapper::toDto).toList();
  }

  //todo: ensure the authenticated user is the one who submitted the post
  public SubmissionResponseDto submitPost(SubmissionRequestDto dto) {
    log.info("Submit a post with data: {}", dto);
    Optional<PostEntity> optionalPostEntity = postRepository.findById(dto.getPostId());

    if (optionalPostEntity.isEmpty()) {
      log.error("Post with ID {} not found", dto.getPostId());
      throw new IllegalArgumentException("Post not found");
    }
    if (!optionalPostEntity.get().getStatus().equals(PostStatus.ACTIVE)) {
      log.error("Post with ID {} is not active", dto.getPostId());
      throw new IllegalArgumentException("Post is closed or suspended");
    }

    SubmissionEntity entity = submissionMapper.toEntity(dto);
    entity.setStatus(SubmissionStatus.SUBMITTED);
    entity.setSubmissionDate(LocalDate.now());

    submissionRepository.save(entity);

    log.info("Post with ID {} submitted successfully", dto.getPostId());
    return submissionMapper.toResponse(dto, LocalDate.now(), SubmissionStatus.SUBMITTED);
  }

  public SubmissionResponseDto updateSubmissionStatus(String submissionId, SubmissionStatus status) {
    Optional<SubmissionEntity> optionalSubmission = submissionRepository.findById(submissionId);
    if (optionalSubmission.isEmpty()) {
      log.error("Submission with ID {} not found", submissionId);
      throw new IllegalArgumentException("Submission not found");
    }
    Long postId = optionalSubmission.get().getPostId();
    PostEntity post = postRepository.findById(postId).orElse(null);// Ensure the post exists

    if (post == null || !post.getStatus().equals(PostStatus.ACTIVE)) {
      log.error("Post with ID {} is not active", postId);
      throw new IllegalArgumentException("Post is closed or suspended");
    }

    SubmissionEntity submissionEntity = optionalSubmission.get();
    submissionEntity.setStatus(status);
    submissionRepository.save(submissionEntity);

    log.info("Submission with ID {} updated to status {}", submissionId, status);
    return submissionMapper.toDto(submissionEntity);
  }

  private Specification<SubmissionEntity> withFilters(SubmissionFilterRequestDto filter) {
    return (root, query, criteriaBuilder) -> {
      Predicate predicate = criteriaBuilder.conjunction();
      assert query != null;
      if (filter.getPostId() != null) {
        predicate.getExpressions().add(criteriaBuilder.like(root.get("postId"), "%" + filter.getPostId() + "%"));
      }
      if (filter.getUsername() != null) {
        predicate.getExpressions().add(criteriaBuilder.like(root.get("username"), "%" + filter.getUsername() + "%"));
      }
      if (filter.getSubmissionStatus() != null) {
        predicate.getExpressions().add(criteriaBuilder.like(root.get("submissionStatus"), "%" + filter.getSubmissionStatus() + "%"));
      }

      return predicate; // Return an empty conjunction for now
    };
  }

}
