package com.jobfinder.finder.integrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, properties = "server.port=8081")
@EnableAutoConfiguration(exclude = {RabbitAutoConfiguration.class})
class ActuatorIntegrationTest {
  TestRestTemplate restTemplate = new TestRestTemplate();
  @LocalServerPort
  int port;

  @Test
  @WithMockUser(username = "user", password = "password")
  void actuatorEndpointsAreAccessible() {
    String baseUrl = "http://localhost:" + port + "/actuator";

    // Check if the health endpoint is accessible
    String healthResponse = restTemplate.getForObject(baseUrl + "/health", String.class);
    assert healthResponse != null && healthResponse.contains("UP");

    // Check if the info endpoint is accessible
    String infoResponse = restTemplate.getForObject(baseUrl + "/info", String.class);
    assert infoResponse != null;
  }
}
