package com.jobfinder.finder.integrationTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobfinder.finder.constant.EmploymentType;
import com.jobfinder.finder.constant.PostStatus;
import com.jobfinder.finder.controller.PostController;
import com.jobfinder.finder.dto.post.PostDto;
import com.jobfinder.finder.dto.post.PostFilterRequestDto;
import com.jobfinder.finder.integrationTest.configuration.MockUserDetailsManagerConfig;
import com.jobfinder.finder.service.PostService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Find;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerIntegrationTest extends FinderIntegrationTestInitiator {

  @MockBean
  private PostService postService;

  @Test
  @WithUserDetails("applicant")
  void getPosts_happy() throws Exception {

    List<PostDto> response = List.of(
        new PostDto("Title1", "Description1", PostStatus.ACTIVE, "Location1", "Company1", EmploymentType.FULL_TIME, 2, 5, List.of("Java", "Spring"),
            "recruiter1"),
        new PostDto("Title2", "Description2", PostStatus.CLOSED, "Location2", "Company2", EmploymentType.PART_TIME, 1, 3, List.of("Python", "Django"),
            "recruiter2")
    );
    Mockito.when(postService.getPosts(Mockito.any(PostFilterRequestDto.class), Mockito.any(Integer.class), Mockito.any(Integer.class))).thenReturn(
        response
    );
    // When
    mockMvc.perform(
            MockMvcRequestBuilders.get("/post")
                .param("page", "0")
                .param("size", "10")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  @WithUserDetails("applicant")
  void getPosts_withFilters_happy() throws Exception {
    // Given
    PostFilterRequestDto filter = new PostFilterRequestDto(null, "Title1", "Location1", null, 0, 0, null, null);

    Mockito.when(postService.getPosts(Mockito.any(PostFilterRequestDto.class), Mockito.any(Integer.class), Mockito.any(Integer.class))).thenReturn(
        List.of(
            new PostDto("Title1", "Description1", PostStatus.ACTIVE, "Location1", "Company1", EmploymentType.FULL_TIME, 2, 5, List.of("Java", "Spring"),
                "recruiter1")
        )
    );
    // When
    mockMvc.perform(
            MockMvcRequestBuilders.get("/post")
                .param("page", "0")
                .param("size", "10")
                .param("title", "Title1")
                .param("location", "Location1")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].title").value(filter.getTitle()))
        .andExpect(jsonPath("$[0].location").value(filter.getLocation()));
  }

  @Test
  @WithUserDetails("admin")
  void getPosts_unauthorized() throws Exception {
    // Given
    Mockito.when(postService.getPosts(Mockito.any(PostFilterRequestDto.class), Mockito.any(Integer.class), Mockito.any(Integer.class))).thenReturn(
        List.of(
            new PostDto("Title1", "Description1", PostStatus.ACTIVE, "Location1", "Company1", EmploymentType.FULL_TIME, 2, 5, List.of("Java", "Spring"),
                "recruiter1")
        )
    );
    // When
    mockMvc.perform(
        MockMvcRequestBuilders.get("/post")
            .param("page", "0")
            .param("size", "10")
            .param("title", "Title1")
            .param("location", "Location1")
    ).andExpect(status().isForbidden());
  }

  @Test
  @WithUserDetails("recruiter")
  void deletePost_happy() throws Exception {
    // Given
    String postId = "12345";

    Mockito.doNothing().when(postService).deletePost(postId);
    // When
    mockMvc.perform(MockMvcRequestBuilders.delete("/post/{postId}", postId))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithUserDetails("applicant")
  void deletePost_unauthorized_403() throws Exception {
    // Given
    String postId = "12345";

    Mockito.doNothing().when(postService).deletePost(postId);
    // When
    mockMvc.perform(MockMvcRequestBuilders.delete("/post/{postId}", postId))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithUserDetails("recruiter")
  void createPost_happy() throws Exception {
    // Given
    PostDto postDto = new PostDto("Title1", "Description1", PostStatus.ACTIVE, "Location1", "Company1", EmploymentType.FULL_TIME, 2, 5,
        List.of("Java", "Spring"),
        "recruiter1");

    Mockito.when(postService.createPost(Mockito.any(PostDto.class))).thenReturn(postDto);
    // When
    mockMvc.perform(
            MockMvcRequestBuilders.post("/post")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(postDto))
        ).andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("Title1"))
        .andExpect(jsonPath("$.description").value("Description1"));
  }

  @Test
  void createPost_unauthorized_403() throws Exception {
    PostDto postDto = new PostDto("Title1", "Description1", PostStatus.ACTIVE, "Location1", "Company1", EmploymentType.FULL_TIME, 2, 5,
        List.of("Java", "Spring"),
        "recruiter1");
    // Given
    mockMvc.perform(
        MockMvcRequestBuilders.post("/post")
            .with(user("admin").password("admin").roles("admin"))
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(postDto))
    ).andExpect(status().isForbidden());
  }

  @Test
  void updatePost_happy() throws Exception {
    // Given
    String postId = "12345";

    PostDto oldPost = new PostDto("old Title", "old Description", PostStatus.ACTIVE, "old Location", "old Company", EmploymentType.FULL_TIME, 3,
        6, List.of("Java", "Spring Boot"), "recruiter1");

    PostDto postDto = new PostDto("Updated Title", "Updated Description", PostStatus.ACTIVE, "Updated Location", "Updated Company", EmploymentType.FULL_TIME, 3,
        6, List.of("Java", "Spring Boot"), "recruiter1");

    Mockito.when(postService.updatePost(Mockito.any(String.class), Mockito.any(PostDto.class))).thenReturn(postDto);

    // When
    mockMvc.perform(
            MockMvcRequestBuilders.patch("/post/{postId}", postId)
                .with(user("recruiter").password("recruiter").roles("RECRUITER"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(postDto))
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Updated Title"))
        .andExpect(jsonPath("$.description").value("Updated Description"));
  }
}
