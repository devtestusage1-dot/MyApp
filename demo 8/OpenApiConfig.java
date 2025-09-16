package com.test.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        // 400 Bad Request
                        .addResponses("BadRequest", new ApiResponse()
                                .description("Custom 400 message")
                                .content(new Content()
                                        .addMediaType("application/json", new MediaType()
                                                .schema(new ObjectSchema()
                                                        .addProperty("code", new Schema<>().type("string").example("400"))
                                                        .addProperty("message", new Schema<>().type("string").example("Ramesh : Invalid request payload"))
                                                        .addProperty("target", new Schema<>().type("string").example("fieldName"))
                                                )
                                        )
                                )
                        )
                        // 401 Unauthorized
                        .addResponses("Unauthorized", new ApiResponse()
                                .description("Custom 401 message")
                                .content(new Content()
                                        .addMediaType("application/json", new MediaType()
                                                .schema(new ObjectSchema()
                                                        .addProperty("code", new Schema<>().type("string").example("401"))
                                                        .addProperty("message", new Schema<>().type("string").example("Ramesh : Authentication required"))
                                                        .addProperty("target", new Schema<>().type("string").example("token"))
                                                )
                                        )
                                )
                        )
                        // 403 Forbidden
                        .addResponses("Forbidden", new ApiResponse()
                                .description("Custom 403 message")
                                .content(new Content()
                                        .addMediaType("application/json", new MediaType()
                                                .schema(new ObjectSchema()
                                                        .addProperty("code", new Schema<>().type("string").example("403"))
                                                        .addProperty("message", new Schema<>().type("string").example("Ramesh : Access denied"))
                                                        .addProperty("target", new Schema<>().type("string").example("resource"))
                                                )
                                        )
                                )
                        )
                        // 500 Internal Server Error
                        .addResponses("InternalError", new ApiResponse()
                                .description("Custom 500 message")
                                .content(new Content()
                                        .addMediaType("application/json", new MediaType()
                                                .schema(new ObjectSchema()
                                                        .addProperty("code", new Schema<>().type("string").example("500"))
                                                        .addProperty("message", new Schema<>().type("string").example("Ramesh : Internal server error occurred"))
                                                        .addProperty("target", new Schema<>().type("string").example("server"))
                                                )
                                        )
                                )
                        )
                );
    }
}
