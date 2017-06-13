package com.askAnything;

import org.h2.server.web.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@EntityScan("com.askAnything.domain")
@SpringBootApplication
public class AskAnythingApplication {

  public static void main(String[] args) {
    SpringApplication.run(AskAnythingApplication.class, args);
  }

  @Bean
  public ServletRegistrationBean h2servletRegistration() {
    ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
    registration.addUrlMappings("/console/*");

    return registration;
  }
}
