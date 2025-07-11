package com.jobfinder.finder.controller;

import com.jobfinder.finder.constant.PostStatus;
import com.jobfinder.finder.dto.PostDto;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/post")
@Slf4j
public class PostController {
  @GetMapping
  public ResponseEntity<List<PostDto>> getPosts(@RequestParam String filter) {
    if (filter != null && !filter.isEmpty()) {
      log.info("Fetching posts with filter: {}", filter);
      // Here you would typically call a service to fetch the posts based on the filter
      //todo: implement filtering logic
      return new ResponseEntity<>(List.of(), HttpStatus.OK);
    }
    log.info("Fetching all posts");
    return new ResponseEntity<>(List.of(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<PostDto> createPost(PostDto dto) {
    log.info("Creating a new post with filter: {}", dto);
    // Here you would typically call a service to create the post
    return new ResponseEntity<>(dto, HttpStatus.CREATED);
  }

  @PatchMapping("/{postId}")
  public ResponseEntity<PostDto> updatePost(@PathVariable String postId, PostDto dto) {
    log.info("Updating post with ID: {} with data: {}", postId, dto);
    // Here you would typically call a service to update the post
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  @PatchMapping("/{postId}/status")
  public ResponseEntity<Void> patchPostStatus(@PathVariable String postId, @RequestParam PostStatus status) {
    log.info("Update post status with ID: {} to {}", postId, status);
    // Here you would typically call a service to close the post
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(@PathVariable String postId) {
    log.info("Deleting post with ID: {}", postId);
    // Here you would typically call a service to delete the post
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
