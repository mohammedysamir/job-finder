package com.jobfinder.finder.controller;

import com.jobfinder.finder.constant.PostStatus;
import com.jobfinder.finder.dto.PostDto;
import com.jobfinder.finder.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PostController {
  private final PostService postService;

  @GetMapping
  public ResponseEntity<List<PostDto>> getPosts(@RequestParam(required = false) String filter) {
    log.info("Fetching posts");
    return new ResponseEntity<>(postService.getPosts(filter), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<PostDto> createPost(PostDto dto) {
    log.info("Creating a new post with filter: {}", dto);
    PostDto createdPost = postService.createPost(dto);
    return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
  }

  @PatchMapping("/{postId}")
  public ResponseEntity<PostDto> updatePost(@PathVariable String postId, PostDto dto) {
    log.info("Updating post with ID: {} with data: {}", postId, dto);
    PostDto updatedPost = postService.updatePost(postId, dto);
    return new ResponseEntity<>(updatedPost, HttpStatus.OK);
  }

  @PatchMapping("/{postId}/status")
  public ResponseEntity<Void> patchPostStatus(@PathVariable String postId, @RequestParam PostStatus status) {
    log.info("Update post status with ID: {} to {}", postId, status);
    postService.patchPostStatus(postId, status);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(@PathVariable String postId) {
    log.info("Deleting post with ID: {}", postId);
    postService.deletePost(postId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
