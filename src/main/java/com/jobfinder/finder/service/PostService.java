package com.jobfinder.finder.service;

import com.jobfinder.finder.config.rabbitMq.RabbitMQConstants;
import com.jobfinder.finder.config.redis.RedisConfiguration;
import com.jobfinder.finder.constant.PostStatus;
import com.jobfinder.finder.dto.post.PostCreationDto;
import com.jobfinder.finder.dto.post.PostResponseDto;
import com.jobfinder.finder.dto.post.PostFilterRequestDto;
import com.jobfinder.finder.dto.post.PostStatusChangeQueueMessage;
import com.jobfinder.finder.dto.post.PostUpdateDto;
import com.jobfinder.finder.dto.submission.SubmissionStatusQueueMessage;
import com.jobfinder.finder.entity.PostEntity;
import com.jobfinder.finder.entity.SubmissionEntity;
import com.jobfinder.finder.exception.PostNoLongerExistsException;
import com.jobfinder.finder.mapper.PostMapper;
import com.jobfinder.finder.repository.PostRepository;
import com.jobfinder.finder.repository.SubmissionRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
  private final PostRepository postRepository;
  private final PostMapper postMapper;
  private final SubmissionRepository submissionRepository;
  private final RabbitTemplate rabbitTemplate;

  @Cacheable(cacheNames = RedisConfiguration.CACHE_NAME, keyGenerator = "customRedisKeyGenerator")
  public List<PostResponseDto> getPosts(PostFilterRequestDto filter, int page, int size) {
    log.info("Fetching posts with filter: {}", filter);
    PageRequest pageRequest = PageRequest.of(page, size);

    Page<PostEntity> pageResult;
    if (null != filter) {
      log.info("Fetching posts with filter: {}", filter);
      Specification<PostEntity> spec = withFilters(filter);
      pageResult = postRepository.findAll(spec, pageRequest);
    } else {
      log.info("Fetching posts without filters");
      pageResult = postRepository.findAll(pageRequest);
    }
    return pageResult.stream().filter(entity -> !entity.getStatus().equals(PostStatus.SUSPENDED)).map(postMapper::toDto).toList();
  }

  @PreAuthorize("dto.username == authentication.username and hasRole('RECRUITER')")
  public PostResponseDto createPost(@Valid PostCreationDto dto) {
    log.info("Creating a new post with data: {}", dto);
    PostEntity entity = postMapper.toEntity(dto);
    entity.setStatus(PostStatus.ACTIVE);
    postRepository.save(entity);
    return postMapper.toPostDto(dto);
  }

  @PreAuthorize("dto.username == authentication.username and hasRole('RECRUITER')")
  public PostResponseDto updatePost(Long postId, PostUpdateDto dto) {
    log.info("Updating post with ID: {} with data: {}", postId, dto);
    PostEntity existingPost = postRepository.findById(Long.valueOf(postId))
        .orElseThrow(() -> new PostNoLongerExistsException("Post not found with ID: " + postId));
    //update only set field in dto into existingPost
    PostEntity entity = updatePost(existingPost, dto);

    postRepository.save(entity);
    return postMapper.toDto(entity);
  }

  @PreAuthorize("hasRole('RECRUITER')")
  public void patchPostStatus(Long postId, PostStatus status) {
    log.info("Update post status with ID: {} to {}", postId, status);
    PostEntity existingPost = postRepository.findById(postId)
        .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));

    existingPost.setStatus(status);
    postRepository.save(existingPost);
    // Notify applicants of the post status change
    notifyApplicantsOfPostStatusChange(postId, status);
  }

  @CacheEvict(cacheNames = RedisConfiguration.CACHE_NAME, keyGenerator = "customRedisKeyGenerator")
  @PreAuthorize("hasRole('RECRUITER')")
  public void deletePost(Long postId) {
    log.info("Deleting a post with ID: {}", postId);
    PostEntity existingPost = postRepository.findById(Long.valueOf(postId))
        .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));
    existingPost.setStatus(PostStatus.SUSPENDED);
    postRepository.save(existingPost);
  }

  private Specification<PostEntity> withFilters(PostFilterRequestDto filter) {
    return (root, query, criteriaBuilder) -> {
      assert query != null;
      List<Predicate> predicates = new ArrayList<>();
      if (filter.getLocation() != null) {
        predicates.add(criteriaBuilder.equal(root.get("location"), filter.getLocation()));
      }
      if (filter.getTitle() != null) {
        predicates.add(criteriaBuilder.equal(root.get("title"), filter.getTitle()));
      }
      if (filter.getCompanyName() != null) {
        predicates.add(criteriaBuilder.equal(root.get("companyName"), filter.getCompanyName()));
      }
      if (filter.getEmploymentType() != null) {
        predicates.add(criteriaBuilder.equal(root.get("employmentType"), filter.getEmploymentType()));
      }
      if (filter.getMinExperience() != 0) {
        predicates.add(criteriaBuilder.equal(root.get("minimumExperience"), filter.getMinExperience()));
      }
      if (filter.getMaxExperience() != 0) {
        predicates.add(criteriaBuilder.equal(root.get("maximumExperience"), filter.getMaxExperience()));
      }
      if (filter.getSkillsRequired() != null && !filter.getSkillsRequired().isEmpty()) {
        predicates.add(root.get("skillsRequired").in(filter.getSkillsRequired()));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  private PostEntity updatePost(PostEntity existingPost, PostUpdateDto dto) {
    PostEntity entity = postMapper.toEntity(dto);
    entity.setId(existingPost.getId());
    entity.setTitle(dto.getTitle() == null ? existingPost.getTitle() : dto.getTitle());
    entity.setDescription(dto.getDescription() == null ? existingPost.getDescription() : dto.getDescription());
    entity.setLocation(dto.getLocation() == null ? existingPost.getLocation() : dto.getLocation());
    entity.setCompanyName(dto.getCompanyName() == null ? existingPost.getCompanyName() : dto.getCompanyName());
    entity.setEmploymentType(dto.getEmploymentType() == null ? existingPost.getEmploymentType() : dto.getEmploymentType());
    entity.setMinimumExperience(dto.getMinimumExperience() == 0 ? existingPost.getMinimumExperience() : dto.getMinimumExperience());
    entity.setMaximumExperience(dto.getMaximumExperience() == 0 ? existingPost.getMaximumExperience() : dto.getMaximumExperience());
    entity.setSkillsRequired(dto.getSkillsRequired() == null || dto.getSkillsRequired().isEmpty() ? existingPost.getSkillsRequired() : dto.getSkillsRequired());
    entity.setRecruiterUsername(dto.getRecruiterUsername() == null ? existingPost.getRecruiterUsername() : dto.getRecruiterUsername());
    entity.setStatus(existingPost.getStatus()); // Preserve the existing status
    return entity;
  }

  private void notifyApplicantsOfPostStatusChange(Long postId, PostStatus status) {
    log.info("Notifying applicants of post status change for post ID: {} and status: {}", postId, status);
    List<SubmissionEntity> byPostId = submissionRepository.findByPostId(postId);
    for (SubmissionEntity entity : byPostId) {
      rabbitTemplate.convertAndSend(
          RabbitMQConstants.POST_STATUS_EXCHANGE,
          RabbitMQConstants.POST_STATUS_CHANGED_ROUTING_KEY,
          new PostStatusChangeQueueMessage(
              postId,
              status,
              entity.getUsername()
          )
      );
      log.info("Notified applicant {} of post status change for post ID: {}", entity.getUsername(), postId);
    }
  }
}
