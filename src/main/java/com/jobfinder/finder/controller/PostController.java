package com.jobfinder.finder.controller;

import com.jobfinder.finder.constant.PostStatus;
import com.jobfinder.finder.dto.post.PostDto;
import com.jobfinder.finder.dto.post.PostFilterRequestDto;
import com.jobfinder.finder.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Post Management Controller", description = "Controller for managing post operations such as creation, retrieval, updates, and deletion.")
public class PostController {
  private final PostService postService;

  //GET /posts?title=Java&page=0&size=10
  @Operation(summary = "Get Posts", description = "Fetches a list of posts based on the provided filter criteria and pagination parameters.")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved posts"),
          @ApiResponse(responseCode = "400", description = "Invalid filter or pagination parameters"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  @GetMapping
  public ResponseEntity<List<PostDto>> getPosts(@ModelAttribute PostFilterRequestDto filter, @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    log.info("Fetching posts");
    return new ResponseEntity<>(postService.getPosts(filter, page, size), HttpStatus.OK);
  }

  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "201", description = "Successfully created a post"),
          @ApiResponse(responseCode = "400", description = "Invalid post data provided"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  @Operation(summary = "Create a new post", description = "Creates a new post with the provided details.")
  @PostMapping
  public ResponseEntity<PostDto> createPost(PostDto dto) {
    log.info("Creating a new post with filter: {}", dto);
    PostDto createdPost = postService.createPost(dto);
    return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
  }

  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "201", description = "Successfully created a post"),
          @ApiResponse(responseCode = "400", description = "Invalid post data provided"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  @Operation(summary = "Update Post by ID", description = "Updates a specific post by its ID and new information.")
  @PatchMapping("/{postId}")
  public ResponseEntity<PostDto> updatePost(@PathVariable String postId, @RequestBody PostDto dto) {
    log.info("Updating post with ID: {} with data: {}", postId, dto);
    PostDto updatedPost = postService.updatePost(postId, dto);
    return new ResponseEntity<>(updatedPost, HttpStatus.OK);
  }

  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "204", description = "Successfully updated post status"),
          @ApiResponse(responseCode = "400", description = "Invalid post ID or status provided"),
          @ApiResponse(responseCode = "404", description = "Post not found"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  @Operation(summary = "Patch Post Status", description = "Updates the status of a specific post by its ID.")
  @PatchMapping("/{postId}/status")
  public ResponseEntity<Void> patchPostStatus(@PathVariable String postId, @RequestParam PostStatus status) {
    log.info("Update post status with ID: {} to {}", postId, status);
    postService.patchPostStatus(postId, status);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "204", description = "Successfully deleted post"),
          @ApiResponse(responseCode = "404", description = "Post not found"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  @Operation(summary = "Delete Post by ID", description = "Deletes a specific post by its ID.")
  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(@PathVariable String postId) {
    log.info("Deleting post with ID: {}", postId);
    postService.deletePost(postId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
