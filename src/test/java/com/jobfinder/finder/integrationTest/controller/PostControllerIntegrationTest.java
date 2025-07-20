package com.jobfinder.finder.integrationTest.controller;

import com.jobfinder.finder.constant.EmploymentType;
import com.jobfinder.finder.constant.PostStatus;
import com.jobfinder.finder.controller.PostController;
import com.jobfinder.finder.dto.PostDto;
import com.jobfinder.finder.dto.PostFilterRequestDto;
import com.jobfinder.finder.integrationTest.configuration.MockUserDetailsManagerConfig;
import com.jobfinder.finder.mapper.PostMapper;
import com.jobfinder.finder.service.PostService;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;

@Import(MockUserDetailsManagerConfig.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration(exclude = {
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
    org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
    RedisAutoConfiguration.class
})
@ActiveProfiles("test")
public class PostControllerIntegrationTest {
  private TestRestTemplate testRestTemplate;
  @MockBean
  private PostService postService;
  @MockBean
  private PostMapper postMapper;
  @InjectMocks
  private PostController postController;
  @LocalServerPort
  private int port;

  @Test
  void getPosts_noFilters_happy() {
    // Given
    testRestTemplate = new TestRestTemplate("applicant", "applicant");
    String url = "http://localhost:" + port + "/post";
    Mockito.when(postService.getPosts(null,0,10)).thenReturn(
        List.of(
            new PostDto("Title1", "Description1", PostStatus.ACTIVE, "Location1", "Company1", EmploymentType.FULL_TIME, 2, 5, List.of("Java", "Spring"), "recruiter1"),
            new PostDto("Title2", "Description2", PostStatus.CLOSED, "Location2", "Company2", EmploymentType.PART_TIME, 1, 3, List.of("Python", "Django"), "recruiter2")
        )
    );
    // When
    ResponseEntity<List<PostDto>> response = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    // Then
    assert response.getStatusCode().is2xxSuccessful();
    assert response.getBody() != null;
    assert response.getBody().size() == 2;
  }

  @Test
  void getPosts_withFilters_happy() {
    // Given
    testRestTemplate = new TestRestTemplate("applicant", "applicant");
    PostFilterRequestDto filter = new PostFilterRequestDto(null, "Title1", "Location1", null, 0, 0, null, null);
    String url = "http://localhost:" + port + "/post?" + filter.toString();

    Mockito.when(postService.getPosts(filter,0,10)).thenReturn(
        List.of(
            new PostDto("Title1", "Description1", PostStatus.ACTIVE, "Location1", "Company1", EmploymentType.FULL_TIME, 2, 5, List.of("Java", "Spring"), "recruiter1")
        )
    );
    // When
    ResponseEntity<List<PostDto>> response = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    // Then
    assert response.getStatusCode().is2xxSuccessful();
    assert response.getBody() != null;
    assert response.getBody().size() == 1;
  }

  @Test
  void deletePost_happy() {
    // Given
    testRestTemplate = new TestRestTemplate("recruiter", "recruiter");
    String postId = "12345";
    String url = "http://localhost:" + port + "/post/" + postId;

    Mockito.doNothing().when(postService).deletePost(postId);
    // When
    ResponseEntity<Void> response = testRestTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
    assert response.getStatusCode().is2xxSuccessful();
    assert response.getStatusCodeValue() == 204;
  }

  @Test
  void deletePost_unauthorized_403() {
    // Given
    testRestTemplate = new TestRestTemplate("applicant", "applicant");
    String postId = "12345";
    String url = "http://localhost:" + port + "/post/" + postId;

    Mockito.doNothing().when(postService).deletePost(postId);
    // When
    ResponseEntity<Void> response = testRestTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
    assert response.getStatusCode().is4xxClientError();
    assert response.getStatusCodeValue() == 403;
  }

  @Test
  void createPost_happy() {
    // Given
    testRestTemplate = new TestRestTemplate("recruiter", "recruiter");
    String url = "http://localhost:" + port + "/post";
    PostDto postDto = new PostDto("Title1", "Description1", PostStatus.ACTIVE, "Location1", "Company1", EmploymentType.FULL_TIME, 2, 5, List.of("Java", "Spring"),
        "recruiter1");

    Mockito.when(postService.createPost(Mockito.any(PostDto.class))).thenReturn(postDto);
    // When
    ResponseEntity<PostDto> response = testRestTemplate.postForEntity(URI.create(url), postDto, PostDto.class);

    // Then
    assert response.getStatusCode().is2xxSuccessful();
    assert response.getBody() != null;
    assert response.getBody().getTitle().equals("Title1");
  }

  @Test
  void createPost_unauthorized_403() {
    // Given
    testRestTemplate = new TestRestTemplate("admin", "admin");
    String url = "http://localhost:" + port + "/post";
    PostDto postDto = new PostDto("Title1", "Description1", PostStatus.ACTIVE, "Location1", "Company1", EmploymentType.FULL_TIME, 2, 5, List.of("Java", "Spring"),
        "recruiter1");

    Mockito.when(postService.createPost(postDto)).thenReturn(postDto);
    // When

    Assertions.assertThrows(RestClientException.class, () -> testRestTemplate.postForEntity(URI.create(url), postDto, PostDto.class));
  }

  @Test
  void updatePost_happy() {
    // Given
    testRestTemplate = new TestRestTemplate("recruiter", "recruiter");
    String postId = "12345";

    String url = "http://localhost:" + port + "/post/" + postId;
    PostDto oldPost = new PostDto("old Title", "old Description", PostStatus.ACTIVE, "old Location", "old Company", EmploymentType.FULL_TIME, 3,
        6, List.of("Java", "Spring Boot"), "recruiter1");

    PostDto postDto = new PostDto("Updated Title", "Updated Description", PostStatus.ACTIVE, "Updated Location", "Updated Company", EmploymentType.FULL_TIME, 3,
        6, List.of("Java", "Spring Boot"), "recruiter1");

    Mockito.when(postService.updatePost(Mockito.any(String.class), Mockito.any(PostDto.class))).thenReturn(postDto);

    // When
    PostDto response = testRestTemplate.patchForObject(url, oldPost, PostDto.class);

    // Then
    assert response != null;
    assert response.getTitle().equals("Updated Title");
  }
}
