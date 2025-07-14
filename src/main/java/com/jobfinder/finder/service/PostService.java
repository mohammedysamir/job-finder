package com.jobfinder.finder.service;

import com.jobfinder.finder.constant.PostStatus;
import com.jobfinder.finder.dto.PostDto;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PostService {

  public List<PostDto> getPosts(String filter) {
    log.info("Fetching posts with filter: {}", filter);
    //todo: implement filtering logic
    if (filter != null && !filter.isEmpty()) {
      log.info("Fetching posts with filter: {}", filter);
      return List.of();
    }
    //todo: implement pagination logic
    // todo: map the fetched posts to PostDto
    return List.of();
  }

  public PostDto createPost(PostDto dto) {
    log.info("Creating a new post with data: {}", dto);
    //todo: map to entity and save to database
    return dto;
  }

  public PostDto updatePost(String postId, PostDto dto) {
    log.info("Updating post with ID: {} with data: {}", postId, dto);
    //todo:: map to entity and update in database
    return dto;
  }

  public void patchPostStatus(String postId, PostStatus status) {
    log.info("Update post status with ID: {} to {}", postId, status);
    //map to entity and update in database
  }

  public void deletePost(String postId) {
    log.info("Deleting a post with ID: {}", postId);
    //delete from database
  }
}
