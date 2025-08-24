package com.loko.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth").addList("x-api-key");


    Info info = new Info().title("Admin Manager").description("This is Admin service").version("v0.0.1");
    SecurityScheme securityScheme = new SecurityScheme().name("bearerAuth")
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer").bearerFormat("JWT");
    // SecurityScheme securityApiKey = new SecurityScheme().name("x-api-key")
    //         .type(SecurityScheme.Type.APIKEY)
    //         .in(SecurityScheme.In.HEADER)
    //         .description("Custom Schema Header");

    Components components = new Components().addSecuritySchemes("bearerAuth", securityScheme);
            // .addSecuritySchemes("x-api-key", securityApiKey);


    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }

}

