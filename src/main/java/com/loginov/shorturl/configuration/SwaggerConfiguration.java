package com.loginov.shorturl.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Short URL API")
                                .version("1.0.0")
                                .contact(
                                        new Contact()
                                                .email("Loginov.l93@gmail.com")
                                                .name("Loginov Leonid")
                                )
                );
    }
}
