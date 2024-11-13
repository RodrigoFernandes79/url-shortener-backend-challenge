package com.tds.url_shortener.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("URL Shortener API")
                        .description("API documentation for shortening URLs")
                        .version("v1")
                        .contact(new Contact()
                                .name("Rodrigo Fernandes")
                                .email("rodrigohf79@hotmail.com")
                                .url("https://www.linkedin.com/in/rodrigo-fernandes-b12b7a169/")
                        )
                        .license(new License()
                                .name("For Evaluation Purposes Only")));
    }
}
