package com.streaming.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI streamingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Streaming Platform API")
                        .description("Music streaming platform built with DDD, "
                                + "Clean Architecture and SOLID principles.")
                        .version("1.0.0")
                        .contact(new Contact().name("Streaming Team").email("dev@stream.io"))
                        .license(new License().name("MIT")));
    }
}
