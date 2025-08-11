package com.jobfinder.finder.integrationTest.service;

import com.jobfinder.finder.config.redis.RedisConfiguration;
import com.jobfinder.finder.constant.EmploymentType;
import com.jobfinder.finder.constant.PostStatus;
import com.jobfinder.finder.dto.post.PostCreationDto;
import com.jobfinder.finder.dto.post.PostFilterRequestDto;
import com.jobfinder.finder.dto.post.PostResponseDto;
import com.jobfinder.finder.dto.post.PostUpdateDto;
import com.jobfinder.finder.entity.PostEntity;
import com.jobfinder.finder.mapper.PostMapper;
import com.jobfinder.finder.repository.PostRepository;
import com.jobfinder.finder.service.PostService;
import java.util.List;
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

@SpringBootTest
public class PostServiceTest extends CacheTestIntializer {


  @SpyBean
  PostMapper postMapper;

  @MockBean
  PostRepository postRepository;

  @Autowired
  PostService postService;

  @Autowired
  CacheManager cacheManager;

  private PostEntity activeFullTimeEntity = new PostEntity(
      1L,
      "Software Engineer",
      "Develop and maintain software applications.",
      PostStatus.ACTIVE,
      "New York",
      "Tech Company",
      EmploymentType.FULL_TIME,
      2,
      5,
      List.of("Java", "Spring", "SQL"),
      "recruiter123"
  );

  private PostEntity activePartTimeEntity = new PostEntity(
      1L,
      "Data Scientist",
      "Develop and maintain data models and algorithms.",
      PostStatus.ACTIVE,
      "Houston",
      "Financial Company",
      EmploymentType.PART_TIME,
      5,
      8,
      List.of("Python", "Mitlab", "SQL"),
      "recruiter456"
  );

  private PostEntity suspendedEntity = new PostEntity(
      1L,
      "Manager",
      "Oversee team operations and manage projects.",
      PostStatus.SUSPENDED,
      "Berlin",
      "Film Production Company",
      EmploymentType.FULL_TIME,
      17,
      20,
      List.of("Powerpoint", "Excel", "Word"),
      "recruiter931"
  );

  //-- Create Posts Test--//

  @Test
  public void createPostTest() {
    PostCreationDto dto = new PostCreationDto(
        "Software Engineer",
        "Develop and maintain software applications.",
        "New York",
        "Tech Company",
        EmploymentType.FULL_TIME,
        2,
        5,
        List.of("Java", "Spring", "SQL"),
        "recruiter123"
    );
    Mockito.when(postRepository.save(Mockito.any(PostEntity.class))).thenReturn(activeFullTimeEntity);
    PostResponseDto response = postService.createPost(dto);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(activeFullTimeEntity.getTitle(), response.getTitle());
    Assertions.assertEquals(activeFullTimeEntity.getDescription(), response.getDescription());
  }

  //-- Get Posts Test--//
  @Test
  public void getPostsTest_noFilters() {

    Page<PostEntity> page = new PageImpl<>(List.of(activeFullTimeEntity, activePartTimeEntity, suspendedEntity));

    Mockito.when(postRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(page);

    List<PostResponseDto> posts = postService.getPosts(null, 0, 10);

    Assertions.assertNotNull(posts);
    Assertions.assertFalse(posts.isEmpty());
    Assertions.assertEquals(2, posts.size());

    assert cacheManager.getCache(RedisConfiguration.CACHE_NAME).get("PostService_getPosts_0:10") != null;
  }

  @Test
  public void getPostsTest_withFilters() {
    Page<PostEntity> page = new PageImpl<>(List.of(activeFullTimeEntity, activePartTimeEntity, suspendedEntity));

    PostFilterRequestDto filter = new PostFilterRequestDto();
    filter.setEmploymentType(EmploymentType.FULL_TIME);

    Mockito.when(postRepository.findAll(Mockito.any(Specification.class), Mockito.any(PageRequest.class))).thenReturn(page);

    List<PostResponseDto> posts = postService.getPosts(filter, 0, 10);

    Assertions.assertNotNull(posts);
    Assertions.assertFalse(posts.isEmpty());
    Assertions.assertEquals(2, posts.size());

    assert cacheManager.getCache(RedisConfiguration.CACHE_NAME).get(
        "PostService_getPosts_companyName=null&title=null&location=null&employmentType=FULL_TIME&minExperience=0&maxExperience=0&recruiterUsername=null&skillsRequired=null:0:10")
        != null;
  }


  //-- Update Posts Test--//
  @Test
  public void updatePostTest() {
    PostUpdateDto dto = new PostUpdateDto();
    dto.setMinimumExperience(4);

    PostEntity updatedEntity = new PostEntity(
        1L,
        "Software Engineer",
        "Develop and maintain software applications.",
        PostStatus.ACTIVE,
        "New York",
        "Tech Company",
        EmploymentType.FULL_TIME,
        4, // Updated minimum experience
        5,
        List.of("Java", "Spring", "SQL"),
        "recruiter123"
    );

    Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(activeFullTimeEntity));
    Mockito.when(postRepository.save(Mockito.any(PostEntity.class))).thenReturn(updatedEntity);

    PostResponseDto response = postService.updatePost(1L, dto);
    Assertions.assertNotNull(response);
    Assertions.assertEquals("Software Engineer", response.getTitle());
    Assertions.assertEquals(4, response.getMinimumExperience());
  }

  //-- Patch Post Status Test--//
  @Test
  public void patchPostStatusTest() {
    Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(activeFullTimeEntity));

    postService.patchPostStatus(1L, PostStatus.SUSPENDED);

    Mockito.verify(postRepository, Mockito.times(1)).save(Mockito.any(PostEntity.class));
    Assertions.assertEquals(PostStatus.SUSPENDED, activeFullTimeEntity.getStatus());
  }

  //-- Delete Post Test--//
  @Test
  public void deletePostTest() {
    cacheManager.getCache(RedisConfiguration.CACHE_NAME).put("PostService_deletePost_1", activeFullTimeEntity);
    Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(activeFullTimeEntity));
    postService.deletePost(1L);
    Mockito.verify(postRepository, Mockito.times(1)).save(Mockito.any(PostEntity.class));
    Mockito.verify(postRepository, Mockito.times(1)).findById(Mockito.anyLong());
    Assertions.assertNull(cacheManager.getCache(RedisConfiguration.CACHE_NAME).get("PostService_deletePost_1"));
  }
}