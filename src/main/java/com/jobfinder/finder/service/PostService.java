package com.jobfinder.finder.service;

import com.jobfinder.finder.config.redis.RedisConfiguration;
import com.jobfinder.finder.constant.PostStatus;
import com.jobfinder.finder.dto.post.PostDto;
import com.jobfinder.finder.dto.post.PostFilterRequestDto;
import com.jobfinder.finder.entity.PostEntity;
import com.jobfinder.finder.mapper.PostMapper;
import com.jobfinder.finder.repository.PostRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
  private final PostRepository postRepository;
  private final PostMapper postMapper;

  @Cacheable(cacheNames = RedisConfiguration.CACHE_NAME, keyGenerator = "customRedisKeyGenerator")
  public List<PostDto> getPosts(PostFilterRequestDto filter, int page, int size) {
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

  public PostDto createPost(@Valid PostDto dto) {
    log.info("Creating a new post with data: {}", dto);
    postRepository.save(postMapper.toEntity(dto));
    return dto;
  }

  public PostDto updatePost(String postId, @Valid PostDto dto) {
    log.info("Updating post with ID: {} with data: {}", postId, dto);
    PostEntity existingPost = postRepository.findById(Long.valueOf(postId))
        .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));
    //update only set field in dto into existingPost
    PostEntity entity = updatePost(existingPost, dto);

    postRepository.save(entity);
    return postMapper.toDto(entity);
  }

  public void patchPostStatus(String postId, PostStatus status) {
    log.info("Update post status with ID: {} to {}", postId, status);
    PostEntity existingPost = postRepository.findById(Long.valueOf(postId))
        .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));
    existingPost.setStatus(status);
    postRepository.save(existingPost);
  }

  @CacheEvict(cacheNames = RedisConfiguration.CACHE_NAME, keyGenerator = "customRedisKeyGenerator")
  public void deletePost(String postId) {
    log.info("Deleting a post with ID: {}", postId);
    PostEntity existingPost = postRepository.findById(Long.valueOf(postId))
        .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));
    existingPost.setStatus(PostStatus.SUSPENDED);
    postRepository.save(existingPost);
  }

  private Specification<PostEntity> withFilters(PostFilterRequestDto filter) {
    return (root, query, criteriaBuilder) -> {
      assert query != null;
      Predicate predicates = criteriaBuilder.conjunction();
      if (filter.getLocation() != null) {
        predicates.getExpressions().add(
            criteriaBuilder.equal(root.get("location"), filter.getLocation())
        );
      }
      if (filter.getTitle() != null) {
        predicates.getExpressions().add(
            criteriaBuilder.equal(root.get("title"), filter.getTitle())
        );
      }
      if (filter.getCompanyName() != null) {
        predicates.getExpressions().add(
            criteriaBuilder.equal(root.get("companyName"), filter.getCompanyName())
        );
      }
      if (filter.getEmploymentType() != null) {
        predicates.getExpressions().add(
            criteriaBuilder.equal(root.get("employmentType"), filter.getEmploymentType())
        );
      }
      if (filter.getMinExperience() != 0) {
        predicates.getExpressions().add(
            criteriaBuilder.equal(root.get("minimumExperience"), filter.getMinExperience())
        );
      }
      if (filter.getMaxExperience() != 0) {
        predicates.getExpressions().add(
            criteriaBuilder.equal(root.get("maximumExperience"), filter.getMaxExperience())
        );
      }
      if (!filter.getSkillsRequired().isEmpty()) {
        predicates.getExpressions().add(
            root.get("skillsRequired").in(filter.getSkillsRequired())
        );
      }
      return predicates; // Return an empty conjunction for now
    };
  }

  private PostEntity updatePost(PostEntity existingPost, PostDto dto) {
    PostEntity entity = postMapper.toEntity(dto);
    entity.setId(existingPost.getId());
    entity.setTitle(dto.getTitle() == null? existingPost.getTitle() : dto.getTitle());
    entity.setDescription(dto.getDescription() == null? existingPost.getDescription() : dto.getDescription());
    entity.setLocation(dto.getLocation() == null? existingPost.getLocation() : dto.getLocation());
    entity.setCompanyName(dto.getCompanyName() == null? existingPost.getCompanyName() : dto.getCompanyName());
    entity.setEmploymentType(dto.getEmploymentType() == null? existingPost.getEmploymentType() : dto.getEmploymentType());
    entity.setMinimumExperience(dto.getMinimumExperience() == 0? existingPost.getMinimumExperience() : dto.getMinimumExperience());
    entity.setMaximumExperience(dto.getMaximumExperience() == 0? existingPost.getMaximumExperience() : dto.getMaximumExperience());
    entity.setSkillsRequired(dto.getSkillsRequired() == null || dto.getSkillsRequired().isEmpty()? existingPost.getSkillsRequired() : dto.getSkillsRequired());
    entity.setRecruiterUsername(dto.getRecruiterUsername() == null? existingPost.getRecruiterUsername() : dto.getRecruiterUsername());
    entity.setStatus(dto.getStatus() == null? existingPost.getStatus() : dto.getStatus());

    return entity;
  }
}
