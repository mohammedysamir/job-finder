package com.jobfinder.finder.integrationTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobfinder.finder.controller.UserController;
import com.jobfinder.finder.dto.user.AddressDto;
import com.jobfinder.finder.dto.user.UserPatchDto;
import com.jobfinder.finder.dto.user.UserRegistrationDto;
import com.jobfinder.finder.dto.user.UserResponseDto;
import com.jobfinder.finder.integrationTest.configuration.MockUserDetailsManagerConfig;
import com.jobfinder.finder.service.UserService;
import com.jobfinder.finder.validator.PhoneNumberValidator;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(MockUserDetailsManagerConfig.class)
@WebMvcTest(UserController.class)
@EnableAutoConfiguration(exclude = {
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
    org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
    RedisAutoConfiguration.class
})
@ActiveProfiles("test")
public class UserControllerIntegrationTest {
  @Autowired
  WebApplicationContext context;

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  PhoneNumberValidator phoneNumberValidator = new PhoneNumberValidator();

  @MockBean
  private UserService userService;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

  @Test
  @WithUserDetails("applicant")
  void registerUser_permitAll_happy() throws Exception {
    // Given
    List<String> phoneNumbers = List.of("+20 1129550094");
    AddressDto address = new AddressDto("Egypt", "Giza", "12511");
    String username = "applicant";
    String email = "applicant@gmail.com";
    String firstName = "app";
    String lastName = "lastName";
    String password = "password123";
    LocalDate dob = LocalDate.of(2000, 1, 1); // Example date of birth
    String imageUrl = "dummyUrl";
    UserRegistrationDto userRegistrationDto = new UserRegistrationDto(username, password, email, firstName, lastName, phoneNumbers, List.of(address), dob,
        imageUrl);
    UserResponseDto response = new UserResponseDto(username, email, firstName, lastName, phoneNumbers, List.of(address), dob, imageUrl);
    Mockito.when(userService.registerUser(Mockito.any(UserRegistrationDto.class))).thenReturn(
        response
    );
    // When
    mockMvc.perform(
            MockMvcRequestBuilders.post("/user/register")
                .content(objectMapper.writeValueAsString(userRegistrationDto))
                .contentType("application/json")
        ).andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("applicant"))
        .andExpect(jsonPath("$.email").value("applicant@gmail.com"))
        .andExpect(jsonPath("$.firstName").value("app"))
        .andExpect(jsonPath("$.lastName").value("lastName"))
        .andExpect(jsonPath("$.dateOfBirth").value(dob.toString()));
  }

  @Test
  @WithUserDetails("applicant")
  void registerUser_permitAll_missingAttribute_400() throws Exception {
    // Given
    List<String> phoneNumbers = List.of("+201129550094");
    AddressDto address = new AddressDto("Egypt", "Giza", "12511");
    String email = "applicant@gmail.com";
    String firstName = "app";
    String lastName = "lastName";
    String password = "password123";
    LocalDate dob = LocalDate.of(2000, 1, 1); // Example date of birth
    String imageUrl = "dummyUrl";
    UserRegistrationDto userRegistrationDto = new UserRegistrationDto(null, password, email, firstName, lastName, phoneNumbers, List.of(address), dob,
        imageUrl);
    /*
    UserResponseDto response = new UserResponseDto(username, email, firstName, lastName, phoneNumbers, List.of(address), dob, imageUrl);
    Mockito.when(userService.registerUser(Mockito.any(UserRegistrationDto.class))).thenReturn(
        response
    );

     */
    // When
    mockMvc.perform(
        MockMvcRequestBuilders.post("/user/register")
            .content(objectMapper.writeValueAsString(userRegistrationDto))
            .contentType("application/json")
    ).andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("applicant")
  void getUserProfile_authenticated_happy() throws Exception {
    //Given
    List<String> phoneNumbers = List.of("+20 1129550094");
    AddressDto address = new AddressDto("Egypt", "Giza", "12511");
    String username = "applicant";
    String email = "applicant@gmail.com";
    String firstName = "app";
    String lastName = "lastName";
    String password = "password123";
    LocalDate dob = LocalDate.of(2000, 1, 1); // Example date of birth
    String imageUrl = "dummyUrl";
    UserResponseDto response = new UserResponseDto(username, email, firstName, lastName, phoneNumbers, List.of(address), dob, imageUrl);
    Mockito.when(userService.getUserProfile(Mockito.any(String.class))).thenReturn(
        response
    );
    // When
    mockMvc.perform(
            MockMvcRequestBuilders.get("/user/{username}/profile", username)
                .contentType("application/json")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("applicant"))
        .andExpect(jsonPath("$.email").value("applicant@gmail.com"))
        .andExpect(jsonPath("$.firstName").value("app"))
        .andExpect(jsonPath("$.lastName").value("lastName"))
        .andExpect(jsonPath("$.dateOfBirth").value(dob.toString()));
  }

  @Test
  void getUserProfile_unauthenticated_401() throws Exception {
    // When
    String username = "applicant";
    mockMvc.perform(
        MockMvcRequestBuilders.get("/user/{username}/profile", username)
            .contentType("application/json")
    ).andExpect(status().isUnauthorized());
  }

  @Test
  @WithUserDetails("applicant")
  void updateUserProfile_authenticated_happy() throws Exception {
    //Given
    List<String> phoneNumbers = List.of("+20 1129550094");
    AddressDto address = new AddressDto("Egypt", "Giza", "12511");
    String username = "applicant";
    String email = "new_email@gmail.com";
    String firstName = "mohammed";
    String lastName = "yasser";
    String password = "topSecret123";
    LocalDate dob = LocalDate.of(2000, 7, 9); // Example date of birth
    String imageUrl = "realUrl";
    UserResponseDto response = new UserResponseDto(username, email, firstName, lastName, phoneNumbers, List.of(address), dob, imageUrl);
    UserPatchDto userPatchDto = new UserPatchDto(password, email, firstName, lastName, phoneNumbers, List.of(address), dob, imageUrl);
    Mockito.when(userService.updateUserProfile(Mockito.any(String.class), Mockito.any(UserPatchDto.class))).thenReturn(
        response
    );
    // When
    mockMvc.perform(
            MockMvcRequestBuilders.patch("/user/{username}/profile", username)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userPatchDto))
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("applicant"))
        .andExpect(jsonPath("$.email").value("new_email@gmail.com"))
        .andExpect(jsonPath("$.firstName").value("mohammed"))
        .andExpect(jsonPath("$.lastName").value("yasser"))
        .andExpect(jsonPath("$.dateOfBirth").value(dob.toString()));
  }

  @Test
  void updateUserProfile_unauthenticated_401() throws Exception {
    //Given
    List<String> phoneNumbers = List.of("+20 1129550094");
    AddressDto address = new AddressDto("Egypt", "Giza", "12511");
    String username = "applicant";
    String email = "new_email@gmail.com";
    String firstName = "mohammed";
    String lastName = "yasser";
    String password = "topSecret123";
    LocalDate dob = LocalDate.of(2000, 7, 9); // Example date of birth
    String imageUrl = "realUrl";
    UserPatchDto userPatchDto = new UserPatchDto(password, email, firstName, lastName, phoneNumbers, List.of(address), dob, imageUrl);

    // When
    mockMvc.perform(
        MockMvcRequestBuilders.patch("/user/{username}/profile", username)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(userPatchDto))
    ).andExpect(status().isUnauthorized());
  }

  @Test
  @WithUserDetails("applicant")
  void deleteUser_authenticatedApplicant_happy()
      throws Exception { //todo: add verification on the userService to check if the user is an admin or the user itself
    // Given
    String username = "applicant";
    Mockito.doNothing().when(userService).deleteUser(username);
    // When
    mockMvc.perform(
        MockMvcRequestBuilders.delete("/user/{username}", username)
            .contentType("application/json")
    ).andExpect(status().isNoContent());
    // Then
    Mockito.verify(userService, Mockito.times(1)).deleteUser(username);
  }

  @Test
  @WithUserDetails("admin")
  void deleteUser_authenticatedAdmin_happy() throws Exception {
    // Given
    String username = "applicant";
    Mockito.doNothing().when(userService).deleteUser(username);
    // When
    mockMvc.perform(
        MockMvcRequestBuilders.delete("/user/{username}", username)
            .contentType("application/json")
    ).andExpect(status().isNoContent());
    // Then
    Mockito.verify(userService, Mockito.times(1)).deleteUser(username);
  }

  @Test
  @WithUserDetails("recruiter")
  void deleteUser_unauthenticatedRecruiter_403() throws Exception {
    // Given
    String username = "applicant";
    Mockito.doNothing().when(userService).deleteUser(username);
    // When
    mockMvc.perform(
        MockMvcRequestBuilders.delete("/user/{username}", username)
            .contentType("application/json")
    ).andExpect(status().isForbidden());
  }

}
