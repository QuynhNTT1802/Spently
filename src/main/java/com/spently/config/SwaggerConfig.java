package com.spently.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spently API")
                        .version("1.0")
                        .description("""
                                <details>
                                  <summary><strong> Click here to view API Authentication Guide</strong></summary>
                                
                                <br/>
                                
                                ## API Authentication Guide
                                
                                This API uses JWT Bearer Authentication for protected endpoints.
                                
                                ### 1. Login to get access token
                                
                                First, call the login API:
                                
                                `POST /auth/login`
                                
                                Enter your username and password, then execute the request.
                                
                                Example request:
                                
                                ```json
                                {
                                  "username": "email@gmail.com",
                                  "password": "123456"
                                }
                                ```
                                
                                If login is successful, the API will return an access token.
                                
                                Example response:
                                
                                ```json
                                {
                                  "message": "Login successfully",
                                  "data": {
                                    "username": "email@gmail.com",
                                    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
                                    "refreshToken": "eyJhbGciOiJIUzUxMiJ9..."
                                  }
                                }
                                ```
                                
                                Copy the value of:
                                
                                `data.accessToken`
                                
                                ### 2. Add token to Swagger
                                
                                Click the **Authorize** button at the top-right corner of Swagger UI.
                                
                                In the `BearerAuth` field, paste the copied access token.
                                
                                Usually, you only need to paste the raw token:
                                
                                ```text
                                eyJhbGciOiJIUzUxMiJ9...
                                ```
                                
                                If the request is still unauthorized, try using the full Bearer format:
                                
                                ```text
                                Bearer eyJhbGciOiJIUzUxMiJ9...
                                ```
                                
                                Then click **Authorize** and close the popup.
                                
                                ### 3. Call protected APIs
                                
                                After authorization, open any protected API, click **Try it out**, then click **Execute**.
                                
                                Swagger will automatically send the token in the request header:
                                
                                ```http
                                Authorization: Bearer <accessToken>
                                ```
                                
                                ### 4. Unauthorized case
                                
                                If the API returns `401 Unauthorized`, please check:
                                
                                - The access token is missing or invalid
                                - The access token has expired
                                - You pasted the refresh token instead of the access token
                                - The current account does not have permission to access the API
                                
                                To fix this, login again and update the token using the **Authorize** button.
                                
                                </details>
                                """)
                        .contact(new Contact()
                                .name("QuynhNTT")
                                .email("nt.thuyquynh1802@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)))
                .addSecurityItem(new SecurityRequirement()
                        .addList(SECURITY_SCHEME_NAME));
    }
}
