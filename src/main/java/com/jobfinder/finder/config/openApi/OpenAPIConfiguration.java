package com.jobfinder.finder.config.openApi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {
  @Bean
  public OpenAPI defineOpenAPI() {
    Contact myContact = new Contact();
    myContact.name("Mohammed Yasser");
    myContact.email("mohammedysamir00@gmail.com");

    Server developmentServer = new Server();
    developmentServer.setUrl("https://localhost:8080");//todo: change this to your production URL
    developmentServer.setDescription("Development Server");

    return new OpenAPI()
        .info(new Info()
            .title("Job Finder API")
            .version("1.0.0")
            .description("API documentation for the Job Finder application")
            .contact(myContact))
        .servers(List.of(developmentServer))
        .components(new Components()
            .addSecuritySchemes("bearerAuth", new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")));
  }
}
