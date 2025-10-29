package com.api.spring_security_demo7.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;


@OpenAPIDefinition(
        info = @Info(
                title = "Spring Security Demo 7 API",
                version = "1.0",
                description = "API exemplo de autenticação e autorização com Spring Security e JWT",
                contact = @Contact(
                    name = "Daniel Penelva de Andrade",
                    email = "d4n.andrade@gmail.com",
                    url = "https://www.linkedin.com/in/daniel-penelva-andrade/"
                )
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
    name = "bearerAuth", 
    description = "JWT auth description", 
    scheme = "bearer", 
    type = SecuritySchemeType.HTTP, 
    bearerFormat = "JWT", 
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

}
