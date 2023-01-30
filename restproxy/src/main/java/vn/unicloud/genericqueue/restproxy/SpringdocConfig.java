package vn.unicloud.genericqueue.restproxy;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringdocConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer", "Bearer").addList("Basic", "Basic"))
                .components(new Components()
                        .addSecuritySchemes("Bearer",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT"))
                        .addSecuritySchemes("Basic",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")
                        ))
                ;
    }
}
