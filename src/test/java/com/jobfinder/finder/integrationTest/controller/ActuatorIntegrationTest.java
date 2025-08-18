package com.jobfinder.finder.integrationTest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {
    RabbitAutoConfiguration.class,
    org.springframework.boot.actuate.autoconfigure.amqp.RabbitHealthContributorAutoConfiguration.class,
    RedisAutoConfiguration.class
})
@Profile("test")
class ActuatorIntegrationTest extends FinderIntegrationTestInitiator {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  UserDetailsService userDetailsService;

  @Test
  @WithUserDetails("admin")
  void actuatorEndpointsAreAccessible() throws Exception {
    mockMvc.perform(
            MockMvcRequestBuilders.get("/actuator/health")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("UP"));

    mockMvc.perform(
            MockMvcRequestBuilders.get("/actuator/info")
        )
        .andExpect(status().isOk());
  }
}