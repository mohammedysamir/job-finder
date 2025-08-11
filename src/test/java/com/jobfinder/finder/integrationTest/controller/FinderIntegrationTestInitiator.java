package com.jobfinder.finder.integrationTest.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobfinder.finder.integrationTest.configuration.JobFinderTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@EnableAutoConfiguration(exclude = {
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
    org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
    RedisAutoConfiguration.class,
    org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration.class
})
@Import(JobFinderTestConfiguration.class)
@ActiveProfiles("test")
public abstract class FinderIntegrationTestInitiator {
  //this class is used to initiate integration tests for the Finder application
  @Autowired
  WebApplicationContext context;

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

}
