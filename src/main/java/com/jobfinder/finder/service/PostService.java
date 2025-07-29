package com.jobfinder.finder.service;

import com.jobfinder.finder.constant.PostStatus;
import com.jobfinder.finder.dto.post.PostDto;
import com.jobfinder.finder.dto.post.PostFilterRequestDto;
import com.jobfinder.finder.entity.PostEntity;
import com.jobfinder.finder.mapper.PostMapper;
import com.jobfinder.finder.repository.PostRepository;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
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
public class PostService {
  private final PostRepository postRepository;
  private final PostMapper postMapper;

  @Cacheable
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
    return pageResult.stream().map(postMapper::toDto).filter(dto -> !dto.getStatus().equals(PostStatus.SUSPENDED)).toList();
  }

  public PostDto createPost(PostDto dto) {
    log.info("Creating a new post with data: {}", dto);
    postRepository.save(postMapper.toEntity(dto));
    return dto;
  }

  public PostDto updatePost(String postId, PostDto dto) {
    log.info("Updating post with ID: {} with data: {}", postId, dto);
    PostEntity existingPost = postRepository.findById(Long.valueOf(postId))
        .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));
    PostEntity entity = postMapper.toEntity(dto);
    entity.setId(existingPost.getId());
    postRepository.save(entity);
    return dto;
  }

  public void patchPostStatus(String postId, PostStatus status) {
    log.info("Update post status with ID: {} to {}", postId, status);
    PostEntity existingPost = postRepository.findById(Long.valueOf(postId))
        .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));
    existingPost.setStatus(status);
    postRepository.save(existingPost);
  }

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
}
