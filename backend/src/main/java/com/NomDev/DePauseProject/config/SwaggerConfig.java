package com.NomDev.DePauseProject.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DePause Project API with Swagger")
                        .version("2.0")
                        .description("API Documentation for DePause Project" +
                                " NCI SoftDev , student x23252065"));
    }
}
